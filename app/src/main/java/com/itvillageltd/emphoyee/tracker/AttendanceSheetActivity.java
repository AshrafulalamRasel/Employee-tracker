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

import java.util.ArrayList;

public class AttendanceSheetActivity extends AppCompatActivity {

    private String employeeId;
    private ListView checkInTime, checkOutTime;
    private ArrayList<String> loginTimeList = new ArrayList<>();
    private ArrayList<String> logoutTimeList = new ArrayList<>();

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
        employeeId = getIntent().getExtras().getString("employeeId");
        Log.e("employeeId", employeeId);
        setTableRow();
    }

    private void setTableRow() {

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("employee").child(employeeId).child("activetime");
        ref.child("login").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                        String loginTime = (String) snapshot.child("time").getValue();
                        loginTimeList.add(loginTime);
                    }
                    Log.e("values", String.valueOf(loginTimeList));
                    ArrayAdapter<String> loginAdapter = new ArrayAdapter<String>(AttendanceSheetActivity.this,
                            android.R.layout.simple_list_item_1, loginTimeList);
                    checkInTime.setAdapter(loginAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
                    ArrayAdapter<String> logoutAdapter = new ArrayAdapter<String>(AttendanceSheetActivity.this, android.R.layout.simple_list_item_1, logoutTimeList);
                    checkOutTime.setAdapter(logoutAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialog.dismiss();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
