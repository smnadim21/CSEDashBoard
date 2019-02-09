package com.nadim.csedashboard;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nadim.csedashboard.dataset.TeacherData;

public class FacultyMemberProfile extends BaseActivity {

    TextView a, b, c, d, e, f ,g,h;
    ImageView profile;
    String callTo = "" , smsTo = "" , emailTo = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_faculty_member_profile);

        String index = getIntent().getStringExtra("key");

        final LinearLayout interest = (LinearLayout) findViewById(R.id.lin_interest);
        final LinearLayout publications = (LinearLayout) findViewById(R.id.lin_publications);

        a = (TextView) findViewById(R.id.user_profile_name);
        b = (TextView) findViewById(R.id.user_profile_short_bio);
        c = (TextView) findViewById(R.id.user_phone);
        d = (TextView) findViewById(R.id.user_email);
        e = (TextView) findViewById(R.id.textView_deptname);
        f = (TextView) findViewById(R.id.textView_deptHead);
        g = (TextView) findViewById(R.id.textView_intrest);
        h = (TextView) findViewById(R.id.textView_Publications);

        profile = (ImageView) findViewById(R.id.user_profile_photo);

        StorageReference pref = FirebaseStorage.getInstance().getReference().child("profile").child("cse"+index+".png");

        pref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(FacultyMemberProfile.this)
                        .load(uri)
                        .into(profile);
            }
        });


        DatabaseReference tref = FirebaseDatabase.getInstance().getReference().child("cse").child("facultymember").child(index);


        tref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                TeacherData t = dataSnapshot.getValue(TeacherData.class);
                if(t != null)
                {
                    callTo = t.getMobile();
                    emailTo = t.getEmail();

                    a.setText(t.getTeacher());
                    b.setText(t.getPost());
                    c.setText("Mobile: " + t.getMobile());
                    d.setText("Email: " + t.getEmail());
                    e.setText(t.getDept());
                    if (t.getHead().equals("1")) {
                        f.setText("Head of The Department");
                    }
                    if(t.getInterest()!=null)
                    {
                        g.setText("Research Interests: " + "\n" + t.getInterest());
                    }
                    else
                    {
                        interest.setVisibility(LinearLayout.GONE);
                    }

                    if(t.getPublications()!=null)
                    {
                        h.setText("Publications: " + "\n" + t.getPublications());
                    }
                    else
                    {
                        publications.setVisibility(LinearLayout.GONE);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void sendSMSTO(View v) {
        Uri uri = Uri.parse("smsto:" + callTo);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(it);
    }

    public void callTo(View v) {
        Uri uri = Uri.parse("tel:" + callTo);
        Intent it = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(it);
    }

    public void emailto(View v) {
        Uri uri = Uri.parse("mailto:" + emailTo);
        Intent it = new Intent(Intent.ACTION_SENDTO, uri);
        startActivity(it);
    }


}
