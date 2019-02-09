package com.nadim.csedashboard.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nadim.csedashboard.R;
import com.nadim.csedashboard.dataset.ClassModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by d3stR0y3r on 11/11/2018.
 */
public class ClassRoutiuneAdapter extends BaseAdapter {
    Activity activity;
    private List<ClassModel> classModels;


    public ClassRoutiuneAdapter(Activity activity, List<ClassModel> classModels) {
        this.activity = activity;
        this.classModels = classModels;
    }

    @Override
    public int getCount() {

        return classModels.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View v = layoutInflater.inflate(R.layout.class_routine_students,null,true);

        //TextView classtime_h = v.findViewById(R.id.textView_classtime_h);
        //TextView classtime_m = v.findViewById(R.id.textView_classtime_m);

        TextView classtime = v.findViewById(R.id.textView_classtime);

        TextView classteacter = v.findViewById(R.id.textView_teacherName);
        TextView classcourse_name = v.findViewById(R.id.textView_coursename);
        TextView classcourse_code= v.findViewById(R.id.textView_course_code);

        String[] split = classModels.get(i).getCtime().split(":");

        //int hour = Integer.parseInt(split[0]);
        //int min = Integer.parseInt(split[1]);

        //classtime_h.setText(split[0]);
        //classtime_m.setText(split[1]);

        classtime.setText(getHours(classModels.get(i).getCtime()));

        classteacter.setText(classModels.get(i).getCtname());
        classcourse_code.setText(classModels.get(i).getCcode());
        classcourse_name.setText(classModels.get(i).getCname());

        return v;
    }



    String getHours(String h)
    {
        String hour = "";

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            final Date dateObj = sdf.parse(h);
            //System.out.println(dateObj);
            hour = new SimpleDateFormat("hh:mm a").format(dateObj);
            return hour;

        } catch (final ParseException e) {
            e.printStackTrace();

            return h;
        }
    }


}
