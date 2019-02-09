package com.nadim.csedashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nadim.csedashboard.loadimage.ImageDownloader;

import java.io.File;

public class NoticeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        String dlNoticeImage = getIntent().getStringExtra("dlimage");
        String noticeTitle = getIntent().getStringExtra("ntitle");

        final ImageView noticeImageBig = findViewById(R.id.imageView_notice_big);
        Button dlButton =  findViewById(R.id.button_downloadNotice);
        TextView notice_title_big = findViewById(R.id.textView_notice_big);

        notice_title_big.setText(noticeTitle);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        final StorageReference pathReference = storageRef.child("images/"+dlNoticeImage);
        Log.d("picture",dlNoticeImage+" at "+ "nactivity");

 /*       new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if(internet)
                {
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("dlpicture",uri+" at "+ "Nactivity");
                            Glide.with(NoticeActivity.this)
                                    .load(uri)
                                    .apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .placeholder(R.drawable.loading))
                                    .into(noticeImageBig);
                        }

                    });
                }

                else
                {
                    Glide.with(NoticeActivity.this)
                            .asGif()
                            .load(R.drawable.loading)
                            .into(noticeImageBig);
                }
            }
        });*/

        //======================================================
        final String filename = dlNoticeImage;
        String dirname = "CSE DashBoard";
        final File file = new File(Environment.getExternalStorageDirectory() + File.separator + dirname + File.separator + filename);

        if (file.exists() && isImage(file)) {
            //imageView.setVisibility(View.VISIBLE);

            Glide.with(NoticeActivity.this)
                    .load(file)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.violate))
                    .into(noticeImageBig);

        }

        else {

            try {

                //imageView.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(R.drawable.violate)
                        .into(noticeImageBig);

                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("dlpicture", uri + " at " );

                        Intent intent = new Intent(NoticeActivity.this, ImageDownloader.class);
                        // Add extras to the bundle
                        intent.putExtra("url", String.valueOf(uri));
                        intent.putExtra("filename", filename);
                        // Start the service
                        startService(intent);


                        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

                            @Override
                            public void onReceive(Context arg0, Intent intent) {
                                String action = intent.getAction();
                                if (action.equals("finish_activity")) {
                            /*Toast.makeText(activity,"pos"+String.valueOf(i),Toast.LENGTH_LONG).show();*/
                                    Glide.with(NoticeActivity.this)
                                            .load(file)
                                            .apply(new RequestOptions()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.violate))
                                            .into(noticeImageBig);
                                }
                            }
                        };

                        registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));




                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                //imageView.setVisibility(View.GONE);
            }

        }
        //======================================================


        dlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(uri);
                        startActivity(i);
                    }
                });
            }
        });




    }

    boolean isImage(File file) {
        if (file == null || !file.exists()) {
            return false;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getPath(), options);
        return options.outWidth != -1 && options.outHeight != -1;
    }
}
