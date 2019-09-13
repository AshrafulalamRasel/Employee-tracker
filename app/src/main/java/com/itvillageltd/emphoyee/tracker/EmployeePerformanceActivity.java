package com.itvillageltd.emphoyee.tracker;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeePerformanceActivity extends AppCompatActivity {
    String uid;
    ArrayList<String> taskStatusList = new ArrayList<>();
    TextView complate, peding, runing, performancetype;
    int runningCount = 0, pendingCount = 0, finishCount = 0;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_performance);
        uid = getIntent().getStringExtra("uid");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        complate = findViewById(R.id.complate);
        peding = findViewById(R.id.peding);
        runing = findViewById(R.id.runing);
        performancetype = findViewById(R.id.performancetype);
        TaskList();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void TaskList() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("employee").child(uid).child("task").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //depending upon what datatype youre using caste to it.
                        String taskStatus = (String) snapshot.child("status").getValue();

                        if (taskStatus.equals("running")) {
                            runningCount++;
                        }
                        if (taskStatus.equals("finish")) {
                            finishCount++;
                        }
                        if (taskStatus.equals("pending")) {
                            pendingCount++;
                        }

                        taskStatusList.add(taskStatus);

                        dialog.dismiss();
                    }
                    complate.setText(String.valueOf(finishCount));
                    peding.setText(String.valueOf(pendingCount));
                    runing.setText(String.valueOf(runningCount));
                    int totalTask = pendingCount + runningCount;
                    if (finishCount >= totalTask / 2) {
                        performancetype.setText("Good");
                    } else {
                        performancetype.setText("Medium");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
