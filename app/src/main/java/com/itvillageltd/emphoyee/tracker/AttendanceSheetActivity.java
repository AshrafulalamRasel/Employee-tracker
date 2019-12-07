package com.itvillageltd.emphoyee.tracker;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AttendanceSheetActivity extends AppCompatActivity {

    private String employeeId;
    private ListView checkInTime, checkOutTime, workingHour;
    private ArrayList<String> loginTimeList = new ArrayList<>();
    private ArrayList<String> logoutTimeList = new ArrayList<>();
    private ArrayList<String> workingHourList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_sheet);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        checkInTime = findViewById(R.id.checkinTime);
        checkOutTime = findViewById(R.id.checkOutTime);
        workingHour = findViewById(R.id.workingHourList);
        employeeId = getIntent().getExtras().getString("employeeId");

        /*
        * Attendee sheet show using by table
        * */
        setTableRow();
    }

    private void setTableRow() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("employee")
                .child(employeeId).child("activetime");
        ref.child("login").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String loginTime = (String) snapshot.child("time").getValue();
                        loginTimeList.add(loginTime);
                    }
                    ArrayAdapter<String> loginAdapter = new ArrayAdapter<String>(
                            AttendanceSheetActivity.this,
                            android.R.layout.simple_list_item_1, loginTimeList);
                    checkInTime.setAdapter(loginAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });

        ref.child("logout").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String logoutTime = (String) snapshot.child("time").getValue();
                        logoutTimeList.add(logoutTime);
                    }
                    Log.e("values", String.valueOf(logoutTimeList));
                    ArrayAdapter<String> logoutAdapter = new ArrayAdapter<String>(
                            AttendanceSheetActivity.this,
                            android.R.layout.simple_list_item_1, logoutTimeList);

                    checkOutTime.setAdapter(logoutAdapter);
                    setWorkingHour();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DatabaseError", databaseError.getMessage());
            }
        });

        dialog.dismiss();
    }

    private void setWorkingHour() {

        for (int i = 0; i < loginTimeList.size(); i++) {
            String login = loginTimeList.get(i);
            String logout = logoutTimeList.get(i);

            if (login == null || logout == null) {
                workingHourList.add("running");
            } else {
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                try {
                    Date date1 = format.parse(login);
                    Date date2 = format.parse(logout);
                    long difference = date2.getTime() - date1.getTime();
                    int hours = (int) difference / (1000 * 60 * 60);
                    long diffMins = difference - hours * (1000 * 60 * 60);
                    int mins = (int) diffMins / (1000 * 60);
                    workingHourList.add(hours + " Hrs " + mins + " mins");
                    Log.e("time difference", hours + " Hrs " + mins + " mins");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.e("values", String.valueOf(logoutTimeList));
        ArrayAdapter<String> logoutAdapter = new ArrayAdapter<String>(
                AttendanceSheetActivity.this,
                android.R.layout.simple_list_item_1, workingHourList);
        workingHour.setAdapter(logoutAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
