package com.nadim.csedashboard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nadim.csedashboard.dataset.ClassModel;
import com.nadim.csedashboard.dataset.Users;

import java.util.Calendar;

/**
 * Created by d3stR0y3r on 11/14/2018.
 */
public class ClassReceiver extends BroadcastReceiver {


String TAG = "fakxcall";


    @Override
    public void onReceive(final Context context, final Intent intent) {

        Log.d(TAG, "message Received");

        if (intent.getStringExtra("message").equals("init")) {
            Log.d(TAG, "initialized");
            Log.d(TAG, intent.getStringExtra("getday"));

            Toast.makeText(context, "initialized", Toast.LENGTH_SHORT).show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                DatabaseReference uref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());

                uref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Users users = dataSnapshot.getValue(Users.class);

                        if (users != null) {
                            if (users.getType().equals("teacher") ||users.getType().equals("Teacher"))
                            {
                                DatabaseReference tref = FirebaseDatabase.getInstance().getReference().child("classroutine");

                                tref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot session : dataSnapshot.getChildren()) {
                                            for (DataSnapshot day : session.getChildren()) {
                                                if (day != null) {
                                                    if (String.valueOf(day.getKey()).equals( intent.getStringExtra("getday"))) {
                                                        for (DataSnapshot classes : day.getChildren()) {

                                                            ClassModel classModel = classes.getValue(ClassModel.class);
                                                            if (classModel != null) {
                                                                if (classModel.getCtname().equals(users.getId())) {


                                                                    Log.d("HSTeachery", "className : " + String.valueOf(classModel.getCcode()));
                                                                    Log.d("HSTeachery", "session: " + String.valueOf(session.getKey()));
                                                                    Log.d("HSTeachery", "day: " + String.valueOf(day.getKey()));
                                                                    Log.d("HSTeachery", "time : " + String.valueOf(classModel.getCtime()));

                                                                    String[] split = classModel.getCtime().split(":");
                                                                    int hour = Integer.parseInt(split[0]);
                                                                    int min = Integer.parseInt(split[1]);

                                                                    Log.d("HSTeachery", "hour : " + hour);
                                                                    Log.d("HSTeachery", "min : " + min);

                                                                    Log.d("HSTeachery", "min processed : " + processMin(min));

                                                                    setClassReminder(context,processHour(hour,min),processMin(min),String.valueOf(session.getKey()),String.valueOf(classModel.getCcode()),String.valueOf(classModel.getCtime()));

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
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }


        } else if (intent.getStringExtra("message").equals("call")) {

            Intent fakecall = new Intent(context, FakeCallWindow.class);
            fakecall.putExtra("batchname", intent.getStringExtra("batchname"));
            fakecall.putExtra("classname", intent.getStringExtra("classname"));
            fakecall.putExtra("classtime", intent.getStringExtra("classtime"));

            fakecall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(fakecall);
        }

        Log.d("fakecallx", "call received");

        Toast.makeText(context, "message received", Toast.LENGTH_SHORT).show();

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


    }

    public void setClassReminder(Context context, int hour, int min,String batch,String classn,String classtim) {
        Intent alarmIntent = new Intent(context, ClassReceiver.class);
        //alarmIntent.setAction(String.valueOf(hour*100+min));
        alarmIntent.putExtra("batchname",batch );
        alarmIntent.putExtra("classname",classn );
        alarmIntent.putExtra("classtime",classtim );
        alarmIntent.putExtra("message", "call");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, hour * 100 + min, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 1);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public int processMin(int min)
    {
        if(min - 10 >= 0 )
            return (min -10);
        else
            return  (min - 10 + 60);

    }

    public int processHour(int hour,int min)
    {
        if(min - 10 >= 0 )
            return hour;
        else
            return (hour - 1);

    }
}
