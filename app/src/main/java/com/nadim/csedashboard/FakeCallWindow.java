package com.nadim.csedashboard;

import android.app.ActionBar;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class FakeCallWindow extends Activity {

    Ringtone ringtone;
    String getIntentData;
    Thread thread;
    Boolean vib =  true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.fake_call_layout);

        getIntentData = getIntent().getStringExtra("batchname");

        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        wakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getApplicationContext().getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();


        final String  batchfrom = getIntent().getStringExtra("batchname");
        final String  classfrom = getIntent().getStringExtra("classname");
        String  timefrom = getIntent().getStringExtra("classtime");


        TextView batch = findViewById(R.id.tv_batchname);
        final TextView classname =  findViewById(R.id.tv_classname);
        TextView classtime =  findViewById(R.id.tv_classtime);

        batch.setText("Calling from "+batchfrom);

        classname.setText("Subject :"+classfrom);

        classtime.setText("Time :"+timefrom);



        final Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);


        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(),uri);
        ringtone.play();

        thread = new Thread(){
            @Override
            public void run() {

                while (vib)
                {
                    if (Build.VERSION.SDK_INT >= 26) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                        try {
                            sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        };

        thread.start();

        Button btn_attend = findViewById(R.id.button_attend_class);
        Button btn_no_attend = findViewById(R.id.button_no_class);
        Button btn_class_later = findViewById(R.id.button_class_later);

        btn_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vib =false;
                thread.interrupt();
                ringtone.stop();

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("noticeboard/class/");

                class Notice{
                    private String ntitle;
                    private String nbody;
                    long time;

                    Notice(){}

                    public Notice(String ntitle, String nbody,long time) {
                        this.ntitle = ntitle;
                        this.nbody = nbody;
                        this.time =  time;
                    }

                    public String getNtitle() {
                        return ntitle;
                    }

                    public void setNtitle(String ntitle) {
                        this.ntitle = ntitle;
                    }

                    public String getNbody() {
                        return nbody;
                    }

                    public void setNbody(String nbody) {
                        this.nbody = nbody;
                    }

                    public long getTime() {
                        return time;
                    }

                    public void setTime(long time) {
                        this.time = time;
                    }
                }



                Notice notice = new Notice("message from DashBoard", classfrom +" class has been granted",new Date().getTime()*-1);

                mDatabase.push().setValue(notice);
                Log.d("console","this is working");
                exitfromcall();

            }
        });

        btn_no_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vib = false;
                thread.interrupt();
                ringtone.stop();

                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("noticeboard/class/");

                class Notice{
                    private String ntitle;
                    private String nbody;
                    long time;

                    Notice(){}

                    public Notice(String ntitle, String nbody,long time) {
                        this.ntitle = ntitle;
                        this.nbody = nbody;
                        this.time =  time;
                    }

                    public String getNtitle() {
                        return ntitle;
                    }

                    public void setNtitle(String ntitle) {
                        this.ntitle = ntitle;
                    }

                    public String getNbody() {
                        return nbody;
                    }

                    public void setNbody(String nbody) {
                        this.nbody = nbody;
                    }

                    public long getTime() {
                        return time;
                    }

                    public void setTime(long time) {
                        this.time = time;
                    }
                }



                Notice notice = new Notice("message from DashBoard", classfrom +" class has been rejected",new Date().getTime()*-1);

                mDatabase.push().setValue(notice);
                exitfromcall();

            }
        });

        btn_class_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vib = false;
                ringtone.stop();
                thread.interrupt();
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("noticeboard/class/");

                class Notice{
                    private String ntitle;
                    private String nbody;
                    long time;

                    Notice(){}

                    public Notice(String ntitle, String nbody,long time) {
                        this.ntitle = ntitle;
                        this.nbody = nbody;
                        this.time =  time;
                    }

                    public String getNtitle() {
                        return ntitle;
                    }

                    public void setNtitle(String ntitle) {
                        this.ntitle = ntitle;
                    }

                    public String getNbody() {
                        return nbody;
                    }

                    public void setNbody(String nbody) {
                        this.nbody = nbody;
                    }

                    public long getTime() {
                        return time;
                    }

                    public void setTime(long time) {
                        this.time = time;
                    }
                }



                Notice notice = new Notice("message from DashBoard", classfrom +" class has been postponed",new Date().getTime()*-1);

                mDatabase.push().setValue(notice);

                exitfromcall();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    void exitfromcall()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        }
        else {
            finish();
        }
    }
}
