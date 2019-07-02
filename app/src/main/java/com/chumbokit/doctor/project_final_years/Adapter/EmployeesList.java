package com.chumbokit.doctor.project_final_years.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chumbokit.doctor.project_final_years.CallLogs.CallLogs;
import com.chumbokit.doctor.project_final_years.MapsActivity;
import com.chumbokit.doctor.project_final_years.R;

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

    public EmployeesList(Activity context, ArrayList<String> maintitle, ArrayList<String> subtitle, ArrayList<Integer> imgid, ArrayList<Double> latList, ArrayList<Double> longList, ArrayList<String> uidList, ArrayList<Boolean> activeStatusList) {
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
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_employee_list, null, true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        Button map= rowView.findViewById(R.id.map);
        Button callDtails= rowView.findViewById(R.id.callHistory);
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

        callDtails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CallLogs.class);
                context.startActivity(intent);
            }
        });

        titleText.setText(maintitle.get(position));
        imageView.setImageResource(imgid.get(position));
        subtitleText.setText(subtitle.get(position));

        return rowView;

    }

    ;
}