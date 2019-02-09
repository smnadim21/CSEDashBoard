package com.nadim.csedashboard.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nadim.csedashboard.R;

public class FakeCallService extends Service  implements View.OnTouchListener{

    private WindowManager windowManager;
    Ringtone ringtone;

    private View floatyView;
    String getIntentData;
    public FakeCallService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        getIntentData = intent.getStringExtra("batchname");

        Log.d("console",getIntentData);
        throw new UnsupportedOperationException("Not yet implemented");


    }

    @Override
    public void onCreate() {

        super.onCreate();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        addOverlayView();


    }

    private void addOverlayView() {

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(),uri);
        ringtone.play();

        WindowManager.LayoutParams params;

        if (Build.VERSION.SDK_INT >= 26) {
            params = new WindowManager.LayoutParams(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                            0,
                            PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.CENTER | Gravity.START;
            params.x = 0;
            params.y = 0;
        }
        else
        {
            params = new WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    0,
                    PixelFormat.TRANSLUCENT);

            params.gravity = Gravity.CENTER | Gravity.START;
            params.x = 0;
            params.y = 0;
        }



        LinearLayout interceptorLayout = new LinearLayout(this) {

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {

                // Only fire on the ACTION_DOWN event, or you'll get two events (one for _DOWN, one for _UP)
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    // Check if the HOME button is pressed
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

                        //Log.v(TAG, "BACK Button Pressed");

                        // As we've taken action, we'll return true to prevent other apps from consuming the event as well
                        return true;
                    }
                }

                // Otherwise don't intercept the event
                return super.dispatchKeyEvent(event);
            }
        };

        floatyView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.fake_call_layout, interceptorLayout);

        floatyView.setOnTouchListener(this);

        Button btn_attend = floatyView.findViewById(R.id.button_attend_class);
        Button btn_no_attend = floatyView.findViewById(R.id.button_no_class);
        Button btn_class_later = floatyView.findViewById(R.id.button_class_later);

        btn_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ringtone.stop();
                Log.d("console","this is working");
                onDestroy();
                stopSelf();
            }
        });

        btn_no_attend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ringtone.stop();
                onDestroy();
                stopSelf();
            }
        });

        btn_class_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ringtone.stop();
                DatabaseReference mDatabase;
                mDatabase = FirebaseDatabase.getInstance().getReference().child("noticeboard/class/");

                class Notice{
                    private String ntitle;
                    private String nbody;

                    Notice(){}

                    public Notice(String ntitle, String nbody) {
                        this.ntitle = ntitle;
                        this.nbody = nbody;
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
                }

                Notice notice = new Notice("message from Habiba mam","omuk Class has been postponed");

                mDatabase.push().setValue(notice);

                onDestroy();
                stopSelf();
            }
        });



        windowManager.addView(floatyView, params);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return true;
    }

    @Override
    public void onDestroy() {

        if (floatyView != null) {

            windowManager.removeView(floatyView);

            floatyView = null;
        }
        stopSelf();
        super.onDestroy();
    }
}
