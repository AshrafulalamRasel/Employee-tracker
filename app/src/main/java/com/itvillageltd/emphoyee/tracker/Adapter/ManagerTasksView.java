package com.itvillageltd.emphoyee.tracker.Adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.itvillageltd.emphoyee.tracker.R;

import java.util.ArrayList;


/**
 * Created by monirozzamanroni on 6/10/2019.
 */

public class ManagerTasksView extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> maintitle;
    private final ArrayList<String> subtitle;
    private final ArrayList<String> taskStatus;


    public ManagerTasksView(Activity context, ArrayList<String> maintitle, ArrayList<String> subtitle, ArrayList<String> taskStatus) {
        super(context, R.layout.custom_employee_list, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
        this.taskStatus = taskStatus;

    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_manager_task_list, null, true);
        TextView title = rowView.findViewById(R.id.task1);
        TextView status = rowView.findViewById(R.id.status);
        status.setText(taskStatus.get(position));
        title.setText(subtitle.get(position));
        if (taskStatus.get(position).equals("running")) {
            status.setTextColor(Color.parseColor("#20be0b"));
        } else if (taskStatus.get(position).equals("finish")) {
            status.setTextColor(Color.parseColor("#be0e0b"));
        }
        return rowView;

    }

    ;
}