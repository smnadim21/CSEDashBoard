package com.nadim.csedashboard;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nadim.csedashboard.adapters.ClassRoutiuneAdapter;
import com.nadim.csedashboard.adapters.TeacherRoutineAdapter;
import com.nadim.csedashboard.dataset.ClassModel;
import com.nadim.csedashboard.dataset.UserProfileSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewClass extends AppCompatActivity {

    List<ClassModel> classModels = new ArrayList<>();
    List<String> ckeys = new ArrayList<>();
    List<String> sessionlist = new ArrayList<>();
    ClassRoutiuneAdapter classRoutiuneAdapter;
    TeacherRoutineAdapter teacherRoutineAdapter;
    String username = "", userType = "", usersession = "", uid = "";
    TextView uname;
    TextView viewSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userType = getIntent().getStringExtra("utype");
        username = getIntent().getStringExtra("uname");
        usersession = getIntent().getStringExtra("usession");
        uid = getIntent().getStringExtra("uid");

        if (userType != null) {
            if (userType.equals("teacher") || userType.equals("Teacher")) {
                setContentView(R.layout.activity_addclass_teacher);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                assert getSupportActionBar() != null;
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                setTitle("Class Routine for " + userType);

                final String[] days =
                        {
                                "sun",
                                "mon",
                                "tue",
                                "wed",
                                "thu"
                        };

                uname = findViewById(R.id.textView_utname);
                uname.setText("Logged in as: " + username + "(" + userType + ")");

                final ListView listViewClass = findViewById(R.id.listView_tclassroutine);
                Spinner spinner_days = findViewById(R.id.spinner_tdays);

                final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_teacher_addclass);


                final ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewClass.this, android.R.layout.simple_spinner_dropdown_item, days);
                spinner_days.setAdapter(adapter);

                spinner_days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, final int index, long l) {
                        classModels.clear();
                        sessionlist.clear();

                        fab.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                showaddClassboxTeacher(days[index], uid);
                            }
                        });

                        DatabaseReference tref = FirebaseDatabase.getInstance().getReference().child("classroutine");

                        tref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot session : dataSnapshot.getChildren()) {
                                    for (DataSnapshot day : session.getChildren()) {
                                        if (day != null) {
                                            if (String.valueOf(day.getKey()).equals(days[index])) {
                                                for (DataSnapshot classes : day.getChildren()) {

                                                    ClassModel classModel = classes.getValue(ClassModel.class);
                                                    if (classModel != null) {
                                                        if (classModel.getCtname().equals(uid)) {
                                                            ckeys.add(classes.getKey());
                                                            classModels.add(classModel);
                                                            sessionlist.add(session.getKey());
                                                            teacherRoutineAdapter.notifyDataSetChanged();

                                                            Log.d("HSTeachertot", "className : " + String.valueOf(classModel.getCcode()));
                                                            Log.d("HSTeachertot", "session: " + String.valueOf(session.getKey()));
                                                            Log.d("HSTeachertot", "day: " + String.valueOf(day.getKey()));
                                                            Log.d("HSTeachertot", "classes : " + String.valueOf(classes.getKey()));
                                                        }
                                                    }

                                                }


                                            }
                                        }

                                    }


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        teacherRoutineAdapter = new TeacherRoutineAdapter(ViewClass.this, classModels, sessionlist);
                        listViewClass.setAdapter(teacherRoutineAdapter);


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            } else {

                /// ======================================================= Students Class Routine =============================================

                setContentView(R.layout.activity_add_class);
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                assert getSupportActionBar() != null;
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                setTitle("Class Routine");


                uname = findViewById(R.id.textView_usname);
                uname.setText("Logged in as: " + username + "(" + userType + ")");


                final TextView viewSession = findViewById(R.id.textView_Viewsession);


                final String[] days =
                        {
                                "sun",
                                "mon",
                                "tue",
                                "wed",
                                "thu",
                                "fri",
                                "sat"
                        };


                final ListView listViewClass = findViewById(R.id.listView_class);
                Spinner spinner_days = findViewById(R.id.spinner_classday);

                final ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewClass.this, android.R.layout.simple_spinner_dropdown_item, days);
                spinner_days.setAdapter(adapter);
                spinner_days.setSelection(currentDay());

                spinner_days.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                        // final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        classModels.clear();
                        ckeys.clear();
                        //  if (user != null)
                        {
                            final SharedPreferences settings = getSharedPreferences("user_data", 0);
                            final String ss = settings.getString("session", "");
                            final String id = settings.getString("userid", "");
                            DatabaseReference uref = FirebaseDatabase.getInstance().getReference().child("dashboarduser").child(ss).child(id);

                            uref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final UserProfileSet users = dataSnapshot.getValue(UserProfileSet.class);
                                    if (users != null) {
                                        final DatabaseReference cref = FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("classroutine")
                                                .child(users.getSession())
                                                .child(days[i]);

                                        viewSession.setText("Session: " + users.getSession());

                                        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                                        if (users.getUsertype().equals("moderator") || users.getUsertype().equals("admin")) {
                                            fab.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    showaddClassbox(days[i], users.getSession());
                                                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                                                            .setAction("Action", null).show();
                                                }
                                            });
                                        } else {
                                            fab.setVisibility(View.GONE);
                                        }

                                        if (users.getUsertype().equals("teacher") || users.getUsertype().equals("Teacher")) {
                                            DatabaseReference tref = FirebaseDatabase.getInstance().getReference().child("classroutine");

                                            tref.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot session : dataSnapshot.getChildren()) {
                                                        for (DataSnapshot day : session.getChildren()) {
                                                            if (day != null) {
                                                                if (String.valueOf(day.getKey()).equals(days[i])) {
                                                                    for (DataSnapshot classes : day.getChildren()) {

                                                                        ClassModel classModel = classes.getValue(ClassModel.class);
                                                                        if (classModel != null) {
                                                                            if (classModel.getCtname().equals("HS")) {
                                                                                ckeys.add(classes.getKey());
                                                                                classModels.add(classModel);
                                                                                classRoutiuneAdapter.notifyDataSetChanged();
                                                                                Log.d("HSTeachertot", "className : " + String.valueOf(classModel.getCcode()));
                                                                                Log.d("HSTeachertot", "session: " + String.valueOf(session.getKey()));
                                                                                Log.d("HSTeachertot", "day: " + String.valueOf(day.getKey()));
                                                                                Log.d("HSTeachertot", "classes : " + String.valueOf(classes.getKey()));
                                                                            }
                                                                        }

                                                                    }


                                                                }
                                                            }

                                                        }


                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                        cref.addChildEventListener(new ChildEventListener() {
                                            @Override
                                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                                Log.d("is this working??", String.valueOf(dataSnapshot));
                                                ClassModel classModel = dataSnapshot.getValue(ClassModel.class);
                                                ckeys.add(dataSnapshot.getKey());
                                                classModels.add(classModel);
                                                classRoutiuneAdapter.notifyDataSetChanged();

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



                                        classRoutiuneAdapter = new ClassRoutiuneAdapter(ViewClass.this, classModels);
                                        listViewClass.setAdapter(classRoutiuneAdapter);

                                        listViewClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                                Toast.makeText(ViewClass.this, ckeys.get(i), Toast.LENGTH_LONG).show();

                                            }
                                        });

                                        if (users.getUsertype().equals("moderator") || users.getUsertype().equals("admin")) {

                                            listViewClass.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                @Override
                                                public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int index, long l) {

                                                    AlertDialog.Builder builder = new AlertDialog.Builder(adapterView.getContext());
                                                    builder.setMessage("Do you want to delete?")
                                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    cref.child(ckeys.get(index)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            Toast.makeText(ViewClass.this, "Class deleted successfully", Toast.LENGTH_LONG).show();
                                                                            ckeys.remove(index);
                                                                            classModels.remove(index);
                                                                            classRoutiuneAdapter.notifyDataSetChanged();
                                                                        }
                                                                    });

                                                                }
                                                            })
                                                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    dialogInterface.dismiss();
                                                                }
                                                            }).show();


                                                    return false;
                                                }
                                            });

                                        }

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

        }


    }

    public void showaddClassboxTeacher(final String day, final String utname) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addclass);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        final List<String> getSessionlist = new ArrayList<>();
        TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        final Button saveClass = dialog.findViewById(R.id.button_addclass);
        final Spinner teacherspinner = dialog.findViewById(R.id.spinner_teacher);

        final EditText cedit = dialog.findViewById(R.id.editText_ccode);


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, final int i, final int i1) {
                getSessionlist.clear();
                String h = "";
                if (i < 10) h = "0" + String.valueOf(i);
                else h = String.valueOf(i);
                String m = "";
                if (i1 < 10) m = "0" + String.valueOf(i1);
                else m = String.valueOf(i1);

                final String finalH = h;
                final String finalM = m;
                DatabaseReference tref = FirebaseDatabase.getInstance().getReference().child("sessions");


                tref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String usessions = dataSnapshot.getValue(String.class);
                        getSessionlist.add(usessions);
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


                tref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewClass.this, android.R.layout.simple_spinner_dropdown_item, getSessionlist);
                        teacherspinner.setAdapter(adapter);

                        teacherspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {

                                saveClass.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        ClassModel classModel = new ClassModel(finalH + ":" + finalM, cedit.getText().toString(), utname);
                                        DatabaseReference cmref
                                                = FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("classroutine")
                                                .child(getSessionlist.get(i))
                                                .child(day);

                                        cmref.push().setValue(classModel)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        teacherRoutineAdapter.notifyDataSetChanged();
                                                        Toast.makeText(ViewClass.this, "Class added at " + finalH + ":" + finalM, Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                        dialog.cancel();

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


            }
        });

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }


    public void showaddClassbox(final String day, final String session) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addclass);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        TimePicker timePicker = dialog.findViewById(R.id.timePicker);
        final Button saveClass = dialog.findViewById(R.id.button_addclass);
        final Spinner teacherspinner = dialog.findViewById(R.id.spinner_teacher);
        final List<String> teachers = new ArrayList<>();
        final EditText cedit = dialog.findViewById(R.id.editText_ccode);
        final EditText cedit_name = dialog.findViewById(R.id.editText_cname);


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, final int i, final int i1) {
                teachers.clear();
                String h = "";
                if (i < 10) h = "0" + String.valueOf(i);
                else h = String.valueOf(i);
                String m = "";
                if (i1 < 10) m = "0" + String.valueOf(i1);
                else m = String.valueOf(i1);

                final String finalH = h;
                final String finalM = m;
                DatabaseReference tref = FirebaseDatabase.getInstance().getReference().child("teachers");

                tref.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String teacher = dataSnapshot.getValue(String.class);
                        teachers.add(teacher);
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

                tref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewClass.this, android.R.layout.simple_spinner_dropdown_item, teachers);
                        teacherspinner.setAdapter(adapter);

                        teacherspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {

                                saveClass.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        ClassModel classModel = new ClassModel(finalH + ":" + finalM, cedit.getText().toString(), teachers.get(i), cedit_name.getText().toString(), String.valueOf(new Date().getTime()));
                                        DatabaseReference cmref
                                                = FirebaseDatabase
                                                .getInstance()
                                                .getReference()
                                                .child("classroutine")
                                                .child(session)
                                                .child(day);

                                        cmref.push().setValue(classModel)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        classModels.clear();
                                                        classRoutiuneAdapter.notifyDataSetChanged();
                                                        Toast.makeText(ViewClass.this, "Class added at " + finalH + ":" + finalM, Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                        dialog.cancel();

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


            }
        });

        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    int currentDay() {
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (Calendar.SUNDAY == dayOfWeek) return 0;
        else if (Calendar.MONDAY == dayOfWeek) return 1;
        else if (Calendar.TUESDAY == dayOfWeek) return 2;
        else if (Calendar.WEDNESDAY == dayOfWeek) return 3;
        else if (Calendar.THURSDAY == dayOfWeek) return 4;
        else if (Calendar.FRIDAY == dayOfWeek) return 5;
        else return 6;
    }

}
