package com.nadim.csedashboard.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nadim.csedashboard.R;
import com.nadim.csedashboard.dataset.CrData;

import java.util.List;

/**
 * Created by d3stR0y3r on 11/25/2018.
 */
public class CRAdapter extends BaseAdapter {
    Activity activity;
    List<CrData> crDatas;



    public CRAdapter(Activity activity, List<CrData> crDatas) {
        this.activity = activity;
        this.crDatas = crDatas;
    }

    @Override
    public int getCount() {
        return crDatas.size();
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

        final View v = layoutInflater.inflate(R.layout.model_class_representative,null,true);

        TextView name,batch,session,mobile,emaill;
        ImageView callto, emailto,smsto;

        name = v.findViewById(R.id.crname);
        batch = v.findViewById(R.id.crbatch);
        session = v.findViewById(R.id.crsession);
        mobile = v.findViewById(R.id.textView_CRmobile);
        emaill = v.findViewById(R.id.textView_CrEmail);

        callto = v.findViewById(R.id.imageView_callto);
        emailto = v.findViewById(R.id.imageView_emailto);
        smsto = v.findViewById(R.id.imageView_smsto);



        name.setText("Name: "+crDatas.get(i).getName());
        batch.setText("Batch: "+crDatas.get(i).getBatch());
        session.setText("Session "+crDatas.get(i).getSession());
        mobile.setText("Mobile: "+crDatas.get(i).getMobile());
        emaill.setText("Email: "+ crDatas.get(i).getEmail());


        callto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("tel:" + crDatas.get(i).getMobile());
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                activity.startActivity(it);

            }
        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("tel:" + crDatas.get(i).getMobile());
                Intent it = new Intent(Intent.ACTION_DIAL, uri);
                activity.startActivity(it);

            }
        });

        emailto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("mailto:" + crDatas.get(i).getEmail());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                activity.startActivity(it);

            }
        });

        emaill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("mailto:" + crDatas.get(i).getEmail());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                activity.startActivity(it);
            }
        });

        smsto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:" +  crDatas.get(i).getMobile());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                activity.startActivity(it);
            }
        });




        return v;
    }
}
