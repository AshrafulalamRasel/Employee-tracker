package com.chumbokit.doctor.project_final_years.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.chumbokit.doctor.project_final_years.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itvillage.dev.basicutil.ToastUtil;

import java.util.ArrayList;


/**
 * Created by monirozzamanroni on 6/10/2019.
 */

public class TasksList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> maintitle;
    private final ArrayList<String> subtitle;
    private final ArrayList<String> taskIdList;
    private final String uid;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLocationDatabaseReference;

    public TasksList(Activity context, ArrayList<String> maintitle, ArrayList<String> subtitle, ArrayList<String> taskIdList,String uid) {
        super(context, R.layout.custom_employee_list, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
        this.taskIdList = taskIdList;
        this.uid = uid;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mLocationDatabaseReference = mFirebaseDatabase.getReference().child("employee").child(uid).child("task");
    }

    public View getView(final int position, View view, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_task_list, null, true);
        TextView title = rowView.findViewById(R.id.task1);
        CardView details = rowView.findViewById(R.id.details);
        final Button finsih=rowView.findViewById(R.id.finsih);
        final Button start=rowView.findViewById(R.id.start);
        title.setText(maintitle.get(position));
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Task Description")
                        .setMessage(subtitle.get(position))
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        finsih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLocationDatabaseReference.child(taskIdList.get(position)).child("status").setValue("finish");
                ToastUtil.show(context,"Task Finish Successfully");
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mLocationDatabaseReference.child(taskIdList.get(position)).child("status").setValue("running");
                ToastUtil.show(context,"Task start Successfully");
            }
        });

        return rowView;

    }

    ;
}