package com.itvillageltd.emphoyee.tracker.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itvillageltd.emphoyee.tracker.AttendanceSheetActivity;
import com.itvillageltd.emphoyee.tracker.EmployeePerformanceActivity;
import com.itvillageltd.emphoyee.tracker.EmployeeTaskListActivity;
import com.itvillageltd.emphoyee.tracker.MapsActivity;
import com.itvillageltd.emphoyee.tracker.R;

import java.util.ArrayList;


/**
 * Created by monirozzamanroni on 6/10/2019.
 */

public class EmployeesList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> maintitle;
    private final ArrayList<String> subtitle;
    private final ArrayList<Integer> imgid;

    private final ArrayList<Double> latList;
    private final ArrayList<Double> longList;
    private final ArrayList<String> uidList;
    private final ArrayList<Boolean> activeStatusList;

    public EmployeesList(Activity context, ArrayList<String> maintitle, ArrayList<String> subtitle,
                         ArrayList<Integer> imgid, ArrayList<Double> latList, ArrayList<Double> longList,
                         ArrayList<String> uidList, ArrayList<Boolean> activeStatusList) {
        super(context, R.layout.custom_employee_list, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
        this.imgid = imgid;

        this.latList = latList;
        this.longList = longList;
        this.uidList = uidList;
        this.activeStatusList = activeStatusList;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_employee_list, null, true);
        LinearLayout task = rowView.findViewById(R.id.task);
        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        Button map = rowView.findViewById(R.id.map);
        if (activeStatusList.get(position) != null) {
            if (activeStatusList.get(position)) {
                map.setBackgroundColor(Color.parseColor("#20c153"));
            } else {
                map.setBackgroundColor(Color.parseColor("#f2060a"));
            }

            map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), MapsActivity.class);
                    intent.putExtra("lat", String.valueOf(latList.get(position)));
                    intent.putExtra("lng", String.valueOf(longList.get(position)));
                    intent.putExtra("name", maintitle.get(position));
                    context.startActivity(intent);
                }
            });
        }
        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final View customLayout = context.getLayoutInflater().inflate(R.layout.profilecustomdialog, null);
                builder.setView(customLayout);
                Button task = customLayout.findViewById(R.id.task);
                Button performance = customLayout.findViewById(R.id.performance);
                TextView userName = customLayout.findViewById(R.id.userName);
                TextView iduser = customLayout.findViewById(R.id.iduser);

                task.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(customLayout.getContext(), EmployeeTaskListActivity.class);
                        intent.putExtra("uid", uidList.get(position));
                        context.startActivity(intent);
                    }
                });
                performance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(customLayout.getContext(), EmployeePerformanceActivity.class);
                        intent.putExtra("uid", uidList.get(position));
                        context.startActivity(intent);
                    }
                });
                Button attendenceSheetBut = customLayout.findViewById(R.id.attendence);
                attendenceSheetBut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AttendanceSheetActivity.class);
                        intent.putExtra("employeeId", uidList.get(position));
                        context.startActivity(intent);
                    }
                });

                userName.setText(maintitle.get(position));
                iduser.setText(uidList.get(position));
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });
        titleText.setText(maintitle.get(position) + "(E " + position + 1 + ")");
        imageView.setImageResource(imgid.get(position));
        subtitleText.setText(subtitle.get(position));

        return rowView;

    }

    ;
}