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

import java.util.List;

/**
 * Created by d3stR0y3r on 11/18/2018.
 */
public class TeacherRoutineAdapter extends BaseAdapter {

    Activity activity;
    private List<ClassModel> classModels;
    private List<String> sessions;


    public TeacherRoutineAdapter(Activity activity, List<ClassModel> classModels, List<String> sessions) {
        this.activity = activity;
        this.classModels = classModels;
        this.sessions = sessions;
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

        final View v = layoutInflater.inflate(R.layout.classroutin_teacher,null,true);


        TextView classtime_h = v.findViewById(R.id.textView_tclasstime_h);
        TextView classtime_m = v.findViewById(R.id.textView_tclasstime_m);
        TextView session = v.findViewById(R.id.textView_tsession);
        TextView classcourse = v.findViewById(R.id.textView_tsubject);
        TextView classcourse_name = v.findViewById(R.id.textView_tsubject_name);

        String[] split = classModels.get(i).getCtime().split(":");

        //classtime.setText(classModels.get(i).getCtime());
        classtime_h.setText(split[0]);
        classtime_m.setText(split[1]);

        classcourse.setText(classModels.get(i).getCcode());
        session.setText(sessions.get(i));
        classcourse_name.setText(classModels.get(i).getCname());

        return v;
    }
}
