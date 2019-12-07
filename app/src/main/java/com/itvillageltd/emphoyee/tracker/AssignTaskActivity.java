package com.itvillageltd.emphoyee.tracker;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itvillage.dev.basicutil.ToastUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AssignTaskActivity extends AppCompatActivity {

    private Button send;
    private String employeeId, taskTitle, employeeName, date;
    private Spinner employeesList, taskList;
    private ArrayList<String> employeeNameList = new ArrayList<>();
    private ArrayList<String> employeeIdList = new ArrayList<>();
    private ArrayList<String> taskArrayList = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLocationDatabaseReference;
    private ProgressDialog dialog;
    private DatePickerDialog picker;
    private EditText selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);

        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        selectedDate = findViewById(R.id.editText1);
        selectedDate.setInputType(InputType.TYPE_NULL);

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

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
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
                Log.e("onNothingSelected", parent.toString());
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
                Log.e("onNothingSelected", parent.toString());
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                mLocationDatabaseReference = mFirebaseDatabase.getReference().child("employee").child(employeeId).child("task").push();
                mLocationDatabaseReference.child("employeesList").setValue(employeeName);
                mLocationDatabaseReference.child("taskList").setValue(taskTitle);
                String time = String.valueOf(selectedDate.getText());
                mLocationDatabaseReference.child("time").setValue(time);
                Log.d("time pick", String.valueOf(selectedDate.getText()));

                mLocationDatabaseReference.child("status").setValue("pending");
                ToastUtil.show(AssignTaskActivity.this, "Send Successfully");
                dialog.dismiss();
                Intent intent = new Intent(AssignTaskActivity.this, SelectTeamMembersActivity.class);
                intent.putExtra("employeeId", employeeId);
                startActivity(intent);
            }
        });

        selectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);

                picker = new DatePickerDialog(AssignTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {

                                date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                                try {
                                    Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    String strDate = dateFormat.format(date1);
                                    selectedDate.setText(strDate);
                                    Log.d("time ", strDate);

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, year, month, day);
                picker.show();
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
