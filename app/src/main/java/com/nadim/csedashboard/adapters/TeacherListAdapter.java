package com.nadim.csedashboard.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nadim.csedashboard.R;
import com.nadim.csedashboard.dataset.TeacherData;

/**
 * Created by D3str0yeR on 1/26/2018.
 */

public class TeacherListAdapter extends BaseAdapter {

    private final Activity context;
    private final List <TeacherData> teacherDatas;

    //private final Typeface tf;

    public TeacherListAdapter(Activity context, List<TeacherData> teacherDatas) {
        // TODO Auto-generated constructor stub

        this.context=context;
        this.teacherDatas=teacherDatas;

        //this.tf = Typeface.createFromAsset(context.getAssets(), "fonts/LatoRegular.ttf");

    }

    @Override
    public int getCount() {
        return teacherDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater;
        View rowView;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowView = inflater.inflate(R.layout.teacherlayout, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.teachername);
       final ImageView imageView = (ImageView) rowView.findViewById(R.id.propic);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.teacherpost);

        titleText.setText(teacherDatas.get(position).getTeacher());
        subtitleText.setText(teacherDatas.get(position).getPost());

  /*      StorageReference pref = FirebaseStorage.getInstance().getReference().child("profile").child("cse"+String.valueOf(position+1)+".png");

        pref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri)
                        .into(imageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("dlprofile","cse"+String.valueOf(position+1)+".png"+" Load Failed");

                StorageReference pref = FirebaseStorage.getInstance().getReference().child("profile").child("cse"+String.valueOf(position+1)+".jpg");

                pref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(context)
                                .load(uri)
                                .into(imageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("dlprofile","cse"+String.valueOf(position+1)+".jpg"+" Load Failed");
                    }
                });


            }
        });*/

        return rowView;

    };
}