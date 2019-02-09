package com.nadim.csedashboard;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.nadim.csedashboard.adapters.NoticeAdapter;
import com.nadim.csedashboard.dataset.NoticeData;
import com.nadim.csedashboard.dataset.UserProfileSet;
import com.nadim.csedashboard.dataset.Users;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String username = "", userType = "", usersession = "", uname = "";
    String LastPostKey = "", Lastmostkey = "";
    List<NoticeData> noticeDatas = new ArrayList<>();
    NoticeAdapter noticeAdapter;
    TextView textView_ut;
    String dirname = "CSE DashBoard";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);


        ActivityCompat.requestPermissions(DashBoardActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);


        File dir =  new  File( Environment.getExternalStorageDirectory(),dirname);

        if(!dir.exists())
        {
            if(dir.mkdirs())
            {
                Log.d("dir",String.valueOf(dir) + "Has done yay!!");
            }
            else Log.d("dir",String.valueOf(dir) + "has failed");
        }


        setClassReminder(this);

        permissionToDrawOverlays();

        textView_ut = findViewById(R.id.textView_uname_and_type);

        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if (internet) {
                    FirebaseMessaging.getInstance().subscribeToTopic("notice").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "subs to notice Success", Toast.LENGTH_LONG).show();
                        }
                    });

                    Toast.makeText(DashBoardActivity.this, "Internet Found", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DashBoardActivity.this, "Internet not Found", Toast.LENGTH_LONG).show();
                }

            }
        });


        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //if (user != null)
        final SharedPreferences settings = getSharedPreferences("user_data", 0);
        final String ss = settings.getString("session","");
        final String id = settings.getString("userid","");

        {
            DatabaseReference uref = FirebaseDatabase.getInstance().getReference().child("dashboarduser").child(ss).child(id);

            uref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    final UserProfileSet users = dataSnapshot.getValue(UserProfileSet.class);

                    if (users != null) {
                        textView_ut.setText("Logged in as: " + users.getName() + "(" + users.getSession() + ")");

                        userType = users.getUsertype();
                        username = users.getName();
                        usersession = users.getSession();
                        uname = users.getId();
                        FirebaseMessaging.getInstance().subscribeToTopic(users.getUsertype())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(DashBoardActivity.this, "subscription to " + users.getUsertype() + " Success", Toast.LENGTH_LONG).show();

                                    }
                                });


                        // subscribe to user Session
                        FirebaseDatabase.getInstance().getReference().child("sessions")
                                .addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                        String ses = dataSnapshot.getValue(String.class);

                                        if (ses != null) {
                                            if (ses.equals(users.getSession())) {
                                                FirebaseMessaging.getInstance().subscribeToTopic(users.getSession()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(DashBoardActivity.this, "subscription to " + users.getSession() + " Success", Toast.LENGTH_LONG).show();
                                                    }
                                                });

                                            } else {
                                                FirebaseMessaging.getInstance().unsubscribeFromTopic(ses);
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
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listViewnotice = findViewById(R.id.listView_notice);

        DatabaseReference noticeRef = FirebaseDatabase.getInstance().getReference().child("noticeboard").child("posts");

        Query noticeQuery = noticeRef.orderByChild("timestamp").limitToFirst(5);

        noticeQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                NoticeData n = dataSnapshot.getValue(NoticeData.class);
                noticeDatas.add(n);
                noticeAdapter.notifyDataSetChanged();
                LastPostKey = dataSnapshot.getKey();

                //Toast.makeText(DashBoardActivity.this,"Current Key:"+LastPostKey,Toast.LENGTH_LONG).show();
                Log.d("texting", "current Key: " + LastPostKey);
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


        Query lastKey = noticeRef.orderByKey().limitToFirst(1);

        lastKey.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Lastmostkey = dataSnapshot.getKey();

                Log.d("texting", "Last Key: " + Lastmostkey);


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


        noticeAdapter = new NoticeAdapter(this, noticeDatas);
        listViewnotice.setAdapter(noticeAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View loadMore = inflater.inflate(R.layout.more, null);

        listViewnotice.addFooterView(loadMore);

        Button loadmoredata = loadMore.findViewById(R.id.button_loadmore);

        loadmoredata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("texting", "this but is working inside" + noticeDatas.size());

                Log.d("texting", "current Key after click: " + LastPostKey);
                Log.d("texting", "last Key after click: " + Lastmostkey);

                DatabaseReference noticeRefu = FirebaseDatabase.getInstance().getReference().child("noticeboard").child("posts");
                Query noticeQuery = noticeRefu.orderByChild("timestamp").limitToFirst(noticeDatas.size() + 5);

                if (!LastPostKey.equals(Lastmostkey)) {
                    noticeDatas.clear();
                    noticeQuery.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            NoticeData n = dataSnapshot.getValue(NoticeData.class);
                            noticeDatas.add(n);
                            Log.d("texting", "this is working inside" + noticeDatas.size());
                            noticeAdapter.notifyDataSetChanged();
                            LastPostKey = dataSnapshot.getKey();
                            Log.d("texting", "current Key after Change: " + LastPostKey);
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
                } else {
                    Toast.makeText(DashBoardActivity.this, "No more notice to load", Toast.LENGTH_LONG).show();
                }
            }
        });


        listViewnotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DashBoardActivity.this, NoticeActivity.class);
                intent.putExtra("dlimage", noticeDatas.get(i).getNotice_picture());
                intent.putExtra("ntitle", noticeDatas.get(i).getNoticetitle());
                startActivity(intent);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(26)
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DashBoardActivity.this,SignUp.class));

/*                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(DashBoardActivity.this,"hi");

                mBuilder.setSmallIcon(R.drawable.ic_menu_gallery);
                mBuilder.setContentTitle("initialized");
                mBuilder.setContentText("detail");
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
                {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel("1000","abc",importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.RED);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    assert mNotificationManager != null;
                    mBuilder.setChannelId("1000");
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }

                mNotificationManager.notify(0, mBuilder.build());*/
            }
        });

/*
        fab.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                startActivity(new Intent(DashBoardActivity.this,UserRequest.class));

                return false;
            }
        });
*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void permissionToDrawOverlays() {
        if (android.os.Build.VERSION.SDK_INT >= 23) {   //Android M Or Over
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1234);
            }
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_logout).setTitle("SignOut (" + username + ")");
        return super.onPrepareOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(DashBoardActivity.this, EditUserProfile.class);
            intent.putExtra("utype", userType);
            intent.putExtra("uname", username);
            intent.putExtra("usession", usersession);
            intent.putExtra("uid", uname);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_logout) {
            //FirebaseAuth.getInstance().signOut();
            SharedPreferences settings = getSharedPreferences("user_data", 0);
            SharedPreferences.Editor editor = settings.edit();
            FirebaseDatabase.getInstance().getReference().child("dashboarduser").child(settings.getString("session","")).child(settings.getString("userid","")).child("loggedin").setValue("false");
            editor.putBoolean("loggedin",false);
            editor.apply();
            Intent intent = new Intent(DashBoardActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(DashBoardActivity.this, "Signed Out.",
                    Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.action_back) {
                startActivity(new Intent(DashBoardActivity.this,NotificationStack.class));

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_class_routine) {

            Intent intent = new Intent(this, ViewClass.class);
            intent.putExtra("utype", userType);
            intent.putExtra("uname", username);
            intent.putExtra("usession", usersession);
            intent.putExtra("uid", uname);

            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.nav_test_fake_call) {

            Intent intent = new Intent(this, FakeCallTest.class);
            startActivity(intent);

            // Handle the camera action
        } else if (id == R.id.nav_faculty_member) {

            startActivity(new Intent(DashBoardActivity.this, FacultyMembers.class));
            return true;
        } else if (id == R.id.nav_cr) {

            startActivity(new Intent(DashBoardActivity.this, ClassRepresentatives.class));
            return true;
        }
        else if (id == R.id.nav_member_request) {

            if (userType.equals("admin")) {
                startActivity(new Intent(DashBoardActivity.this, UserRequest.class));
            } else {
                Toast.makeText(this,"You are not an admin!!",Toast.LENGTH_LONG).show();
            }
            return true;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setClassReminder(Context context) {
        Intent alarmIntent = new Intent(context, ClassReceiver.class);
        alarmIntent.putExtra("message", "init");
        alarmIntent.putExtra("getday", today());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 19);
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND, 1);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    public String today()

    {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        if (day == Calendar.SUNDAY)
            return "sun";
        else if (day == Calendar.MONDAY)
            return "mon";
        else if (day == Calendar.TUESDAY)
            return "tue";
        else if (day == Calendar.WEDNESDAY)
            return "wed";
        else if (day == Calendar.THURSDAY)
            return "thu";

        else return "noclass";
    }


}
