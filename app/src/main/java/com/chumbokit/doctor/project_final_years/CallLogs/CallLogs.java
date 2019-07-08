package com.chumbokit.doctor.project_final_years.CallLogs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.chumbokit.doctor.project_final_years.Adapter.CallLists;
import com.chumbokit.doctor.project_final_years.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CallLogs extends AppCompatActivity {
    String uid;
    ListView callList;
    DatabaseReference reference;
    ArrayList<String> contectNameList;
    ArrayList<String> duration;
    ArrayList<String> date;
    ArrayList<String> callTypes;


    Integer[] imgid = {
            R.drawable.prescription, R.drawable.prescription,
            R.drawable.prescription, R.drawable.prescription,
            R.drawable.prescription,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_logs);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        uid = getIntent().getStringExtra("uid");
        reference = FirebaseDatabase.getInstance().getReference().child("employee").child(uid).child("callLogs");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    contectNameList = new ArrayList<>();
                    duration = new ArrayList<>();
                    date = new ArrayList<>();
                    callTypes = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String values = (String) snapshot.child("phone").getValue();
                        String[] valuesAll = values.split("\n");
                        contectNameList.add(valuesAll[0]);
                        duration.add(valuesAll[1]);
                        date.add(valuesAll[2]);
                        callTypes.add(valuesAll[3]);


                        CallLists adapter = new CallLists(CallLogs.this, contectNameList, duration, imgid, date, callTypes);
                        callList = (ListView) findViewById(R.id.callList);
                        callList.setAdapter(adapter);
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
