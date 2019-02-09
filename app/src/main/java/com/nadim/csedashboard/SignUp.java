package com.nadim.csedashboard;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nadim.csedashboard.dataset.UserProfileSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SignUp extends AppCompatActivity {

    boolean found = false;

    List<String> session = new ArrayList<>();
    List<String> location = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button b_nxt1 , b_nxt2 , b_prev1,b_prev2, b_signup;
        final LinearLayout ll_1,ll_2,ll_3;
        final Spinner ss,spinner_loc;
        final EditText e_id,e_pass,e_name,e_regno,e_email,e_mobile;


        b_nxt1 = findViewById(R.id.button_next_s_1);
        b_nxt2 = findViewById(R.id.button_next_s_2);
        b_signup = findViewById(R.id.button_next_s_3);

        b_prev1 = findViewById(R.id.button_prev_s_1);
        b_prev2 = findViewById(R.id.button_prev_s_2);

        ll_1 = findViewById(R.id.lin_signup1);
        ll_2 = findViewById(R.id.lin_signup2);
        ll_3 = findViewById(R.id.lin_signup3);


        ss =  findViewById(R.id.spinner_signup_session);

        e_id = findViewById(R.id.editText_sign_id);
        e_pass = findViewById(R.id.editText_sign_pass);

        e_name = findViewById(R.id.editText_su_name);
        e_regno = findViewById(R.id.editText_su_regno);
        e_email = findViewById(R.id.editText_su_email);
        e_mobile = findViewById(R.id.editText_su_mobile);


        spinner_loc =  findViewById(R.id.spinner_su_loc);

        location.add("Hall");
        location.add("Beside Campus");
        location.add("Inside Campus");
        location.add("Outside Campus");

        final ArrayAdapter<String> locadapter = new ArrayAdapter<>(SignUp.this, android.R.layout.simple_spinner_dropdown_item, location);
        spinner_loc.setAdapter(locadapter);


// next  button action==============================================================
        b_nxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new InternetCheck(new InternetCheck.Consumer() {
                    @Override
                    public void accept(Boolean internet) {

                        if (internet)
                        {
                            ll_1.setVisibility(View.GONE);
                            ll_2.setVisibility(View.VISIBLE);
                        }
                        else Toast.makeText(SignUp.this,"No Internet!",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        b_nxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                FirebaseDatabase.getInstance().getReference().child("dashboarduser")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot session : dataSnapshot.getChildren())
                                {
                                    for (DataSnapshot id : session.getChildren())
                                    {
                                        if(id.getKey().equals(e_id.getText().toString()))
                                        {
                                            e_id.requestFocus();
                                            e_id.setError("ID Already Exists!!");
                                            found = true;
                                            break;
                                        }
                                    }
                                }

                                if(!found)
                                {
                                    ll_2.setVisibility(View.GONE);
                                    ll_3.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });



            }
        });



        // previous button action==============================================================
        b_prev1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_1.setVisibility(View.VISIBLE);
                ll_2.setVisibility(View.GONE);
            }
        });

        b_prev2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ll_2.setVisibility(View.VISIBLE);
                ll_3.setVisibility(View.GONE);
            }
        }); // previous button action==============================================================

        DatabaseReference sessionref = FirebaseDatabase.getInstance().getReference().child("session");

        sessionref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot s : dataSnapshot.getChildren())
                {
                    session.add(String.valueOf(s.getValue()));
                }

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(SignUp.this, android.R.layout.simple_spinner_dropdown_item, session);
                ss.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        b_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                UserProfileSet userProfileSet = new UserProfileSet(e_name.getText().toString(),e_id.getText().toString(),ss.getSelectedItem().toString(),spinner_loc.getSelectedItem().toString(),e_mobile.getText().toString(),e_email.getText().toString(),"req","demo","none",String.valueOf(Calendar.getInstance().getTime()),e_pass.getText().toString());
                FirebaseDatabase.getInstance().getReference()
                        .child("dashboarduser").child(ss.getSelectedItem().toString()).child(e_id.getText().toString()).setValue(userProfileSet)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(SignUp.this,e_name.getText().toString()+"Signed UP",Toast.LENGTH_LONG).show();
                                onBackPressed();
                            }
                        });
            }
        });



    }
}
