package com.itvillageltd.emphoyee.tracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itvillage.dev.basicutil.ToastUtil;

import java.util.ArrayList;

public class AssignTaskActivity extends AppCompatActivity {

    Button send;
    String employeeId, taskTitle, employeeName;
    Spinner employeesList, taskList;
    ArrayList<String> employeeNameList = new ArrayList<>();
    ArrayList<String> employeeIdList = new ArrayList<>();
    ArrayList<String> taskArrayList = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLocationDatabaseReference;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);

        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        employeesList = findViewById(R.id.employeeList);
        taskList = findViewById(R.id.taskList);
        send = findViewById(R.id.send);

        employeeNameList.add("--- Select Employee ----");
        employeeIdList.add("--- Employee id ----");


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("employee");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //depending upon what datatype youre using caste to it.
                        String name = (String) snapshot.child("name").getValue();
                        String uid = snapshot.getKey();
                        employeeNameList.add(name);
                        employeeIdList.add(uid);
                        dialog.dismiss();
                    }
                    Log.e("Employee List", String.valueOf(employeeNameList));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ArrayAdapter employeesListAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, employeeNameList);
        employeesListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        employeesList.setAdapter(employeesListAdapter);
        employeesList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employeeName = parent.getItemAtPosition(position).toString();
                employeeId = employeeIdList.get(position);
                Toast.makeText(parent.getContext(), "Selected Employee", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        taskArrayList.add("--- Select Task ----");
        taskArrayList.add("Develop Ee-commerce site");
        taskArrayList.add("Develop a android app");
        taskArrayList.add("Sell Product");
        taskArrayList.add("Maintain Project 1");
        taskArrayList.add("Maintain Project 2");
        taskArrayList.add("Maintain Project 3");

        ArrayAdapter taskListAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, taskArrayList);
        taskListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        taskList.setAdapter(taskListAdapter);
        taskList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                taskTitle = parent.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Selected Task", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                mLocationDatabaseReference = mFirebaseDatabase.getReference().child("employee").child(employeeId).child("task").push();
                mLocationDatabaseReference.child("employeesList").setValue(employeeName);
                mLocationDatabaseReference.child("taskList").setValue(taskTitle);
                mLocationDatabaseReference.child("status").setValue("pending");
                ToastUtil.show(AssignTaskActivity.this, "Send Successfully");
                dialog.dismiss();
                Intent intent = new Intent(AssignTaskActivity.this, SelectTeamMembersActivity.class);
                intent.putExtra("employeeId", employeeId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
