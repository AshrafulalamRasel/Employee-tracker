package com.chumbokit.doctor.project_final_years.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chumbokit.doctor.project_final_years.R;

import java.util.ArrayList;

/**
 * Created by monirozzamanroni on 6/19/2019.
 */

public class CallLists extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> contectNameList;
    private final ArrayList<String> duration;
    private final Integer[] imgid;
    private final ArrayList<String> date;

    public CallLists(Activity context, ArrayList<String> contectNameList, ArrayList<String> duration, Integer[] imgid, ArrayList<String> date) {
        super(context, R.layout.custom_employee_list, contectNameList);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.contectNameList = contectNameList;
        this.duration = duration;
        this.imgid = imgid;
        this.date = date;

    }

   /* public CallLists(Activity context, String[] phoneNum, Integer[] imgid) {
        super(context, R.layout.custom_employee_list, phoneNum);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = phoneNum;


    }*/

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.call_lists, null, true);

        TextView phoneNum = (TextView) rowView.findViewById(R.id.phoneNum);
        ImageView callImage = (ImageView) rowView.findViewById(R.id.callImage);
        TextView callDuration = (TextView) rowView.findViewById(R.id.callDuration);
        TextView callDate = (TextView) rowView.findViewById(R.id.callDate);

        phoneNum.setText(contectNameList.get(position));
        //callImage.setImageResource(imgid[position]);
        callDuration.setText(duration.get(position));
        callDate.setText(date.get(position));

        return rowView;

    }

    ;
}