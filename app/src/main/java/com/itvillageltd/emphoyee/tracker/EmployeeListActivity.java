package com.itvillageltd.emphoyee.tracker;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itvillageltd.emphoyee.tracker.Adapter.EmployeesList;

import java.util.ArrayList;


public class EmployeeListActivity extends AppCompatActivity {

    ListView list;
    ArrayList<String> mainName = new ArrayList<>();
    ArrayList<String> subtitle = new ArrayList<>();
    ArrayList<Double> latList = new ArrayList<>();
    ArrayList<Double> longList = new ArrayList<>();
    ArrayList<String> uidList = new ArrayList<>();
    ArrayList<Boolean> activeStatusList = new ArrayList<>();
    ArrayList<Integer> imgid = new ArrayList<>();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dialog = new ProgressDialog(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        dialog.setMessage("Loading in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("employee").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //depending upon what datatype youre using caste to it.
                        String name = (String) snapshot.child("name").getValue();
                        Double lat = (Double) snapshot.child("location").child("lat").getValue();
                        Double lng = (Double) snapshot.child("location").child("lng").getValue();
                        String uid = (String) snapshot.getKey();
                        Boolean activeStatus = (Boolean) snapshot.child("activeStatus").getValue();
                        latList.add(lat);
                        longList.add(lng);
                        uidList.add(uid);
                        activeStatusList.add(activeStatus);
                        mainName.add(name);
                        subtitle.add(name);
                        imgid.add(R.drawable.prescription);
                        EmployeesList adapter = new EmployeesList(EmployeeListActivity.this,
                                mainName, subtitle, imgid, latList, longList, uidList, activeStatusList);
                        list = (ListView) findViewById(R.id.List);
                        list.setAdapter(adapter);
                        dialog.dismiss();
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
