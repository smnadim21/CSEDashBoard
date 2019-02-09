package com.nadim.csedashboard;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class FakeCallTest extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fake_call_test);

        TimePicker fakccallTime = findViewById(R.id.timePicker_fakeCall);

        final Button addFakeCall =  findViewById(R.id.button_addCall);
        addFakeCall.setVisibility(View.GONE);

        fakccallTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, final int h, final int m) {
                addFakeCall.setVisibility(View.VISIBLE);
                addFakeCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setClassReminder(FakeCallTest.this,h,m);

                        Toast.makeText(FakeCallTest.this,"Call set up at "+h+":"+m,Toast.LENGTH_LONG ).show();
                    }
                });


            }
        });




    }

    public void setClassReminder(Context context, int hour, int min) {
        Intent alarmIntent = new Intent(context, ClassReceiver.class);
        //alarmIntent.setAction(String.valueOf(hour*100+min));
        alarmIntent.putExtra("message","call");
        alarmIntent.putExtra("batchname",String.valueOf(hour*100+min));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, hour*100+min, alarmIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 1);
        manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
