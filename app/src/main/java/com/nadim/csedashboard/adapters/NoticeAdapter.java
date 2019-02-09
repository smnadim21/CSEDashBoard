package com.nadim.csedashboard.adapters;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nadim.csedashboard.InternetCheck;
import com.nadim.csedashboard.R;
import com.nadim.csedashboard.dataset.NoticeData;
import com.nadim.csedashboard.loadimage.ImageDownloader;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by d3stR0y3r on 9/19/2018.
 */
public class NoticeAdapter extends BaseAdapter {
    Activity activity;
/*    private String[] noticeTitle;
    private String[] noticeDescription;*/
    private List<NoticeData> noticedata;
    String dirname = "CSE DashBoard";


/*    public NoticeAdapter(Activity activity, String[] noticeTitle, String[] noticeDescription) {
        this.activity = activity;
        this.noticeTitle = noticeTitle;
        this.noticeDescription = noticeDescription;
    }*/

    public NoticeAdapter(Activity activity, List<NoticeData> noticeDatas) {
        this.activity = activity;
        this.noticedata = noticeDatas;
        Collections.reverse(noticedata);

    }

    @Override
    public int getCount() {
        return noticedata.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View v = layoutInflater.inflate(R.layout.demonotice,null,true);

        TextView noticetitle = v.findViewById(R.id.textView_notice_title);
        TextView notice_category =  v.findViewById(R.id.textView_category);
        TextView notice_session = v.findViewById(R.id.textView_session);
        TextView timestamp = v.findViewById(R.id.textView_timestamp);
        final ImageView notice_image = v.findViewById(R.id.image_notice);


        //imageView.setShadowColor(Color.parseColor(color));

        noticetitle.setText(noticedata.get(i).getNoticetitle());

        notice_session.setText("Session: "+noticedata.get(i).getSession());
        timestamp.setText("Published on: "+publishedTime(noticedata.get(i).getTimestamp()));
        notice_category.setText(noticedata.get(i).getCategories());


        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        final StorageReference pathReference = storageRef.child("images/"+noticedata.get(i).getNotice_picture());
        Log.d("picture",noticedata.get(i).getNotice_picture()+" at "+ i);







/*        new InternetCheck(new InternetCheck.Consumer() {
            @Override
            public void accept(Boolean internet) {
                if(internet)
                {
                    pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.d("dlpicture",uri+" at "+ i);
                            Glide.with(activity)
                                    .load(uri)
                                    .apply(new RequestOptions()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                                    .into(notice_image);
                        }
                    });
                }
                else
                {
                    Glide.with(activity)
                            .asGif()
                            .load(R.drawable.violate)
                            .into(notice_image);
                }


            }
        });*/



        //final File filename = new File(noticedata.get(i).getNotice_picture());
        final File file = new File(Environment.getExternalStorageDirectory() + File.separator + dirname + File.separator + noticedata.get(i).getNotice_picture());

        if (file.exists() && isImage(file)) {
            //imageView.setVisibility(View.VISIBLE);

            Glide.with(activity)
                    .load(file)
                    .apply(new RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.violate))
                    .into(notice_image);

        }

        else {

            try {

                //imageView.setVisibility(View.VISIBLE);
                Glide.with(activity)
                        .load(R.drawable.violate)
                        .into(notice_image);

                pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("dlpicture", uri + " at " + i);

                        Intent intent = new Intent(activity, ImageDownloader.class);
                        // Add extras to the bundle
                        intent.putExtra("url", String.valueOf(uri));
                        intent.putExtra("filename", noticedata.get(i).getNotice_picture());
                        // Start the service
                        activity.startService(intent);


                        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

                            @Override
                            public void onReceive(Context arg0, Intent intent) {
                                String action = intent.getAction();
                                if (action.equals("finish_activity")) {
                            /*Toast.makeText(activity,"pos"+String.valueOf(i),Toast.LENGTH_LONG).show();*/
                                    Glide.with(activity)
                                            .load(file)
                                            .apply(new RequestOptions()
                                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                                    .placeholder(R.drawable.violate))
                                            .into(notice_image);
                                }
                            }
                        };

                        activity.registerReceiver(broadcastReceiver, new IntentFilter("finish_activity"));




                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                //imageView.setVisibility(View.GONE);
            }

        }



        return v;

    }

    String publishedTime(long mills)
    {
        DateFormat formatter = new SimpleDateFormat("EEEEEEE,dd MMM yyyy 'at' hh:mm a ",Locale.getDefault());
        //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(new Date(mills*-1));
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
