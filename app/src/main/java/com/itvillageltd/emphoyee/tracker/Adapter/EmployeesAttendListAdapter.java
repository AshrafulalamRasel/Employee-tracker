package com.itvillageltd.emphoyee.tracker.Adapter;

import android.app.Activity;
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

public class EmployeesAttendListAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> timeList;


    public EmployeesAttendListAdapter(Activity context, ArrayList<String> time) {
        super(context, R.layout.custom_employee_list, time);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.timeList = time;

    }

    public View getView(final int position, View view, ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.employee_attendee_view, null, true);

        TextView titleText = rowView.findViewById(R.id.timeShow);
        titleText.setText(timeList.get(position));

        return rowView;

    }

    ;
}