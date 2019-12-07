package com.itvillageltd.emphoyee.tracker;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itvillageltd.emphoyee.tracker.Adapter.ManagerTasksView;

import java.util.ArrayList;

public class EmployeeTaskListActivity extends AppCompatActivity {
    String uid;
    ImageButton task;
    ListView taskList;
    ArrayList<String> mainName = new ArrayList<>();
    ArrayList<String> subtitle = new ArrayList<>();
    ArrayList<String> taskStatusList = new ArrayList<>();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_employee_task);

        uid = getIntent().getStringExtra("uid");
        task = findViewById(R.id.task);
        taskList = findViewById(R.id.taskList);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
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
                        String title = (String) snapshot.child("employeesList").getValue();
                        String description = (String) snapshot.child("taskList").getValue();
                        String taskStatus = (String) snapshot.child("status").getValue();

                        mainName.add(title);
                        subtitle.add(description);
                        taskStatusList.add(taskStatus);

                        ManagerTasksView adapter = new ManagerTasksView(EmployeeTaskListActivity.this, mainName, subtitle,taskStatusList);

                        taskList.setAdapter(adapter);
                        dialog.dismiss();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
