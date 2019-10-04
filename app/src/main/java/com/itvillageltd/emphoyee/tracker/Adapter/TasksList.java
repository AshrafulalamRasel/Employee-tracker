package com.itvillageltd.emphoyee.tracker.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itvillage.dev.basicutil.ToastUtil;
import com.itvillageltd.emphoyee.tracker.R;

import java.util.ArrayList;


/**
 * Created by monirozzamanroni on 6/10/2019.
 */

public class TasksList extends ArrayAdapter<String> {
    private final Activity context;
    private final ArrayList<String> maintitle;
    private final ArrayList<String> timedate;
    private final ArrayList<String> subtitle;
    private final ArrayList<String> taskIdList;
    private final String uid;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLocationDatabaseReference;
    private ProgressDialog dialog;

    public TasksList(Activity context, ArrayList<String> maintitle, ArrayList<String> subtitle, ArrayList<String> taskIdList, ArrayList<String> timedate, String uid) {
        super(context, R.layout.custom_employee_list, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
        this.subtitle = subtitle;
        this.taskIdList = taskIdList;
        this.timedate = timedate;
        this.uid = uid;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mLocationDatabaseReference = mFirebaseDatabase.getReference().child("employee").child(uid).child("task");
    }

    public View getView(final int position, View view, ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_task_list, null, true);
        final TextView title = rowView.findViewById(R.id.task1);
        final TextView time = rowView.findViewById(R.id.timedate);
        CardView details = rowView.findViewById(R.id.details);
        final Button finsih=rowView.findViewById(R.id.finsih);
        final Button start=rowView.findViewById(R.id.start);
        title.setText(subtitle.get(position));
        time.setText("Deadline line of this task:" + " " + timedate.get(position));

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
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayList<String> teamMemberNameList = new ArrayList<>();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final View customLayout = context.getLayoutInflater().inflate(R.layout.show_team_member, null);
                builder.setView(customLayout);
                final ListView teamMateList = customLayout.findViewById(R.id.teamMateList);
                dialog = new ProgressDialog(context);
                dialog.setMessage("Loading in please wait...");
                dialog.setIndeterminate(true);
                dialog.show();

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("employee").child(uid).child("task").child(taskIdList.get(position)).child("team").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                String name = (String) snapshot.child("name").getValue();

                                teamMemberNameList.add(name);

                            }
                            ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, teamMemberNameList);

                            teamMateList.setAdapter(itemsAdapter);
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return rowView;

    }

    ;
}