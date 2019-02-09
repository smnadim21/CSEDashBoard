package com.nadim.csedashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nadim.csedashboard.dataset.UserProfileSet;
import com.nadim.csedashboard.dataset.Users;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


   // private FirebaseAuth mAuth;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    boolean loggedin = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseDatabase.getInstance().getReference().keepSynced(true);

        SharedPreferences settings = getSharedPreferences("user_data", 0);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.ID);

        try {
            mEmailView.setText(settings.getString("userid",""));
        } catch (Exception e) {
            e.printStackTrace();
        }


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        Button b_signup = (Button) findViewById(R.id.button_la_su);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                new InternetCheck(new InternetCheck.Consumer() {
                    @Override
                    public void accept(Boolean internet) {
                        if(internet)
                        {
                            attemptLogin();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this,"No Internet!!",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        b_signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUp.class));
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */

    @Override
    public void onStart() {
        super.onStart();
        final SharedPreferences settings = getSharedPreferences("user_data", 0);
        final String ss = settings.getString("session","");
        final String id = settings.getString("userid","");

        Log.d("ssid",ss+id);

        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if(internet)
                {
                    if (!ss.equals("")&&!id.equals("")) {
                        FirebaseDatabase.getInstance().getReference().child("dashboarduser").child(ss).child(id).child("loggedin")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String s = dataSnapshot.getValue(String.class);
                                        if(s!= null && s.equals("true"))
                                        {
                                            startActivity(new Intent(LoginActivity.this,DashBoardActivity.class));
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                    }

                }

                else
                {
                    if(settings.getBoolean("loggedin",false))
                    {
                        startActivity(new Intent(LoginActivity.this,DashBoardActivity.class));
                    }
                }
            }
        });

    }

    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

           DatabaseReference lref = FirebaseDatabase.getInstance().getReference().child("dashboarduser");
            lref.keepSynced(true);
            lref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot session : dataSnapshot.getChildren())
                            {
                                for (DataSnapshot id : session.getChildren())
                                {
                                    if(id.getKey().equals(mEmailView.getText().toString()))
                                    {
                                       if(id.child("password").getValue().equals(mPasswordView.getText().toString()))
                                       {
                                           if(id.child("editconf").getValue().equals("approve"))
                                           {
                                               FirebaseDatabase.getInstance().getReference().child("dashboarduser").child(session.getKey()).child(id.getKey()).child("loggedin").setValue("true");
                                               SharedPreferences settings = getSharedPreferences("user_data", 0);
                                               SharedPreferences.Editor editor = settings.edit();
                                               editor.putString("session",session.getKey());
                                               editor.putString("userid",id.getKey());
                                               editor.putBoolean("loggedin",true);
                                               editor.apply();
                                               startActivity(new Intent(LoginActivity.this,DashBoardActivity.class));
                                               loggedin = true;
                                               break;
                                           }
                                           else
                                           {
                                               Toast.makeText(LoginActivity.this,"Wait for Approval",Toast.LENGTH_LONG).show();
                                               showProgress(false);
                                               break;
                                           }
                                       }
                                        else
                                       {
                                           Toast.makeText(LoginActivity.this,"Login Failed!!",Toast.LENGTH_LONG).show();
                                           showProgress(false);
                                           break;
                                       }

                                    }
                                }
                            }

                            if(!loggedin)
                            {
                                Toast.makeText(LoginActivity.this,"Login Failed!!",Toast.LENGTH_LONG).show();
                                showProgress(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


            /*mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                           *//*     isFirstStart = getPrefs.getBoolean("firstStart", true);*//*
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("csexx", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(LoginActivity.this, "Login Success.",
                                        Toast.LENGTH_LONG).show();
                                if(user != null)
                                {
                                    DatabaseReference uref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

                                    uref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            final Users users = dataSnapshot.getValue(Users.class);
                                            if (users != null)
                                            {
                                                Intent intent = new Intent(LoginActivity.this,DashBoardActivity.class);
                                                startActivity(intent);
                                                showProgress(false);
                                            }

                                            else
                                            {
                                                Intent intent = new Intent(LoginActivity.this,EditUserProfile.class);
                                                startActivity(intent);
                                                showProgress(false);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                }


                            } else {
                                Log.w("csexx", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }

                            // ...
                        }
                    });*/

        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4 ;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

