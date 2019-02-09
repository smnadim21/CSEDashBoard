package com.nadim.csedashboard;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nadim.csedashboard.adapters.CRAdapter;
import com.nadim.csedashboard.dataset.CrData;

import java.util.ArrayList;
import java.util.List;

public class ClassRepresentatives extends BaseActivity {

    List<CrData> crDatas = new ArrayList<>();
    CRAdapter crAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_representatives);

        FloatingActionButton f_cr = findViewById(R.id.fab_cr);

        f_cr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showaddCR();

            }
        });

        ListView listView_cr = findViewById(R.id.listView_cr);

        FirebaseDatabase.getInstance().getReference().child("classmonitor")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        CrData c = dataSnapshot.getValue(CrData.class);
                        crDatas.add(c);
                        crAdapter.notifyDataSetChanged();

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

        crAdapter = new CRAdapter(ClassRepresentatives.this,crDatas);
        listView_cr.setAdapter(crAdapter);


    }





    public void showaddCR() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addcr);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setLayout( WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);

        final EditText name,batch,session,mobile,email;
        Button cr_save;

        name = dialog.findViewById(R.id.editText_CrName);
        batch = dialog.findViewById(R.id.editText_CrBatch);
        session = dialog.findViewById(R.id.editText_CrSession);
        mobile = dialog.findViewById(R.id.editText_CrMobile);
        email = dialog.findViewById(R.id.editText_CrEmail);

        cr_save = dialog.findViewById(R.id.button_CrSave);


        cr_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(TextUtils.isEmpty(name.getText().toString()))
                {
                    name.setError("name can not be empty");
                    name.requestFocus();
                }
                else if(TextUtils.isEmpty(batch.getText().toString()))
                {
                    batch.setError("batch can not be empty");
                    batch.requestFocus();
                }
                else if(TextUtils.isEmpty(session.getText().toString()))
                {
                    session.setError("session can not be empty");
                    session.requestFocus();
                }else if(TextUtils.isEmpty(mobile.getText().toString()))
                {
                    mobile.setError("mobile can not be empty");
                    mobile.requestFocus();
                }else if(TextUtils.isEmpty(email.getText().toString()))
                {
                    email.setError("email can not be empty");
                    email.requestFocus();
                }

                else
                {
                    CrData crData = new CrData(name.getText().toString(),batch.getText().toString(),session.getText().toString(),mobile.getText().toString(),email.getText().toString());

                    FirebaseDatabase.getInstance().getReference().child("classmonitor").push().setValue(crData)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(ClassRepresentatives.this,"Saved Succesfully",Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                    crAdapter.notifyDataSetChanged();

                                }
                            });
                }




            }
        });

        dialog.show();
    }

}
