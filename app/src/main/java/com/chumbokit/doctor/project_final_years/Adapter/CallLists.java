package com.chumbokit.doctor.project_final_years.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chumbokit.doctor.project_final_years.R;

/**
 * Created by monirozzamanroni on 6/19/2019.
 */

public class CallLists extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] maintitle;
    private final String[] CallDate;
    private final String[] subtitle;
    private final Integer[] imgid;

    public CallLists(Activity context, String[] phoneNum, String[] callDuration, String[] callDate, Integer[] callImage) {
        super(context, R.layout.custom_employee_list, phoneNum);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = phoneNum;
        this.subtitle = callDuration;
        this.imgid = callImage;
        this.CallDate = callDate;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.call_lists, null, true);

        TextView phoneNum = (TextView) rowView.findViewById(R.id.phoneNum);
        ImageView callImage = (ImageView) rowView.findViewById(R.id.callImage);
        TextView callDuration = (TextView) rowView.findViewById(R.id.callDuration);
        TextView callDate = (TextView) rowView.findViewById(R.id.callDate);

        phoneNum.setText(maintitle[position]);
        callImage.setImageResource(imgid[position]);
        callDuration.setText(subtitle[position]);
        callDate.setText(CallDate[position]);

        return rowView;

    }

    ;
}