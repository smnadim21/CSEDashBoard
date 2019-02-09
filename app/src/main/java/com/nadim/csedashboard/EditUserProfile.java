package com.nadim.csedashboard;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nadim.csedashboard.dataset.Users;

import java.util.ArrayList;
import java.util.List;

public class EditUserProfile extends BaseActivity {

    EditText uname, roll, regno, email, mobile;
    List<String> sessions = new ArrayList<>(), location = new ArrayList<>(), designation = new ArrayList<>();
    TextView type, tuid,femail;
    Button saveUser;
    Spinner spinner_location;
    String username = "", userType = "", usersession = "", uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userType = getIntent().getStringExtra("utype");
        username = getIntent().getStringExtra("uname");
        usersession = getIntent().getStringExtra("usession");
        uid = getIntent().getStringExtra("uid");

        if (userType != null) {
            if (userType.equals("teacher") || userType.equals("Teacher")) {
                setContentView(R.layout.activity_teacher_profile);

                uname = findViewById(R.id.editText_tudname);
                tuid = findViewById(R.id.textView_tuname);
                email = findViewById(R.id.editText_tuemail);
                type = findViewById(R.id.textView_tutype);
                saveUser = findViewById(R.id.button_tusave);
                mobile = findViewById(R.id.editText_tumobile);
                spinner_location = findViewById(R.id.spinner_tuLocation);
                femail = findViewById(R.id.textView_tfemail);


                location.add("Teacher Dormitory");
                location.add("Inside Campus");
                location.add("Beside Campus");
                location.add("Inside Trishal");
                location.add("Outside Campus");


                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    femail.setText(user.getEmail());
                    DatabaseReference uref = FirebaseDatabase.getInstance().getReference().child("users");

                    uref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Users users = dataSnapshot.getValue(Users.class);
                            if (users != null) {

                                uname.setText(users.getName());
                                tuid.setText(users.getId());
                                email.setText(users.getEmail());
                                type.setText(users.getType());
                                mobile.setText(users.getMobile());

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                //==================== spinner Initialize =======================================

                final Spinner spinner_designation = findViewById(R.id.spinner_tdesignation);

                designation.add("Professor");
                designation.add("Associate Professor");
                designation.add("Assistant Professor");
                designation.add("Lecturer");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditUserProfile.this, android.R.layout.simple_spinner_dropdown_item, designation);

                spinner_designation.setAdapter(adapter);

                spinner_designation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, final int index, long l) {


                        ArrayAdapter<String> adapter_loc = new ArrayAdapter<>(EditUserProfile.this, android.R.layout.simple_spinner_dropdown_item, location);

                        spinner_location.setAdapter(adapter_loc);

                        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {

                                saveUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        View focusView = null;
                                        boolean cancel = false;

                                        if (TextUtils.isEmpty(uname.getText().toString())) {
                                            uname.setError("Name can not be Empty");
                                            focusView = uname;
                                            cancel = true;
                                        }else if (TextUtils.isEmpty(email.getText().toString())) {
                                            email.setError("Email can not be Empty");
                                            focusView = email;
                                            cancel = true;
                                        }else if (TextUtils.isEmpty(mobile.getText().toString())) {
                                            mobile.setError("Mobile No. can not be Empty");
                                            focusView = mobile;
                                            cancel = true;
                                        }
                                        if (cancel) {
                                            focusView.requestFocus();
                                        }
                                        else {
                                            Users users = new Users(uname.getText().toString(), designation.get(index), tuid.getText().toString(), "0000", email.getText().toString(), user.getUid(), type.getText().toString(), mobile.getText().toString(), location.get(i),user.getEmail());
                                            DatabaseReference uref = FirebaseDatabase.getInstance().getReference().child("users");
                                            uref.child(user.getUid()).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(EditUserProfile.this, "User Profile Saved Succesfully", Toast.LENGTH_LONG).show();

                                                    Intent intent = new Intent(EditUserProfile.this, DashBoardActivity.class);
                                                    startActivity(intent);
                                                }
                                            });

                                        }
                                    }
                                });


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });


            }

            else {

                showUserProfile();
            }

        }


        else {

            showUserProfile();

        }


    }


    void showUserProfile()

    {
        setContentView(R.layout.activity_user_profile);

        uname = findViewById(R.id.editText_uname);
        roll = findViewById(R.id.editText_uroll);
        regno = findViewById(R.id.editText_uregno);
        email = findViewById(R.id.editText_uemail);
        type = findViewById(R.id.textView_utype);
        saveUser = findViewById(R.id.button_usave);
        mobile = findViewById(R.id.editText_umobile);
        spinner_location = findViewById(R.id.spinner_uLocation);
        femail = findViewById(R.id.textView_femail);


        location.add("Hall");
        location.add("Inside Campus");
        location.add("Beside Campus");
        location.add("Inside Trishal");
        location.add("Outside Campus");

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            femail.setText(user.getEmail());
            // User is signed in
            DatabaseReference uref = FirebaseDatabase.getInstance().getReference().child("users");
            uref.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Users users = dataSnapshot.getValue(Users.class);
                    if (users != null) {
                        uname.setText(users.getName());
                        roll.setText(users.getId());
                        regno.setText(users.getRegno());
                        email.setText(users.getEmail());
                        type.setText(users.getType());
                        mobile.setText(users.getMobile());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        //==================== spinner Initialize =======================================
        final Spinner spinner_session = findViewById(R.id.spinner_usession);


        DatabaseReference sref = FirebaseDatabase.getInstance().getReference().child("sessions");

        sref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String session = dataSnapshot.getValue(String.class);
                sessions.add(session);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        sref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditUserProfile.this, android.R.layout.simple_spinner_dropdown_item, sessions);

                spinner_session.setAdapter(adapter);

                spinner_session.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, final int index, long l) {

                        ArrayAdapter<String> adapter_loc = new ArrayAdapter<>(EditUserProfile.this, android.R.layout.simple_spinner_dropdown_item, location);

                        spinner_location.setAdapter(adapter_loc);

                        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {

                                saveUser.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        View focusView = null;
                                        boolean cancel = false;

                                        if (TextUtils.isEmpty(uname.getText().toString())) {
                                            uname.setError("Name can not be Empty");
                                            focusView = uname;
                                            cancel = true;
                                        }
                                        if (TextUtils.isEmpty(roll.getText().toString())) {
                                            roll.setError("Roll can not be Empty");
                                            focusView = roll;
                                            cancel = true;
                                        }
                                        if (TextUtils.isEmpty(regno.getText().toString())) {
                                            regno.setError("Reg No. can not be Empty");
                                            focusView = regno;
                                            cancel = true;
                                        }
                                        if (TextUtils.isEmpty(email.getText().toString())) {
                                            email.setError("Email can not be Empty");
                                            focusView = email;
                                            cancel = true;
                                        }
                                        if (cancel) {
                                            focusView.requestFocus();
                                        } else {
                                            Users users = new Users(uname.getText().toString(), sessions.get(index), roll.getText().toString(), regno.getText().toString(), email.getText().toString(), user.getUid(), type.getText().toString(), mobile.getText().toString(), location.get(i),user.getEmail());
                                            DatabaseReference uref = FirebaseDatabase.getInstance().getReference().child("users");
                                            uref.child(user.getUid()).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(EditUserProfile.this, "User Profile Saved Succesfully", Toast.LENGTH_LONG).show();

                                                    Intent intent = new Intent(EditUserProfile.this, DashBoardActivity.class);
                                                    startActivity(intent);
                                                }
                                            });

                                        }


                                    }
                                });
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //==================== spinner Initialize =======================================

    }

}

