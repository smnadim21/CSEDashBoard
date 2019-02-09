package com.nadim.csedashboard;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nadim.csedashboard.adapters.TeacherListAdapter;
import com.nadim.csedashboard.dataset.TeacherData;

import java.util.ArrayList;
import java.util.List;

public class FacultyMembers extends BaseActivity {

    TextView headName, headPost;
    ImageView headImage;
    List<TeacherData> teacherDatas = new ArrayList<>();
    List<String> teacherKey = new ArrayList<>();
    TeacherListAdapter teacherListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_members);



        headName = (TextView) findViewById(R.id.textView_headname);
        headPost = (TextView) findViewById(R.id.textView_HeadPost);
        headImage = (ImageView) findViewById(R.id.imageView_headPic);
        ListView list = findViewById(R.id.listViewTeacher);


        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("cse").child("facultymember");

        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TeacherData t = dataSnapshot.getValue(TeacherData.class);
                teacherDatas.add(t);
                teacherKey.add(dataSnapshot.getKey());
                teacherListAdapter.notifyDataSetChanged();
                Log.d("teacherList",String.valueOf(dataSnapshot));

                if (t != null)
                {
                    if(t.getHead().equals("1"))
                    {
                        headName.setText(t.getTeacher());
                        headPost.setText(t.getPost());
                    }
                }
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

        teacherListAdapter = new TeacherListAdapter(this,teacherDatas);
        list.setAdapter(teacherListAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Intent intent = new Intent(FacultyMembers.this,FacultyMemberProfile.class);
                intent.putExtra("key",teacherKey.get(i));
                startActivity(intent);

                //Toast.makeText(FacultyMembers.this,teacherKey.get(i),Toast.LENGTH_LONG).show();

            }
        });



    }
}
