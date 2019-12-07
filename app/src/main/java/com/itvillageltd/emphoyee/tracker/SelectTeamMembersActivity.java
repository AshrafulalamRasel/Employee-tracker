package com.itvillageltd.emphoyee.tracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itvillage.dev.basicutil.ToastUtil;

import java.util.ArrayList;

public class SelectTeamMembersActivity extends AppCompatActivity {

    ArrayList<String> employeeList = new ArrayList<>();
    ListView listView;
    private ProgressDialog dialog;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLocationDatabaseReference;
    private String taskUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team_members);

        listView = findViewById(R.id.listView);
        Button send = findViewById(R.id.send);
        final String employeeId = getIntent().getExtras().getString("employeeId");

        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        dialog = new ProgressDialog(this);
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
                        employeeList.add(name);
                        dialog.dismiss();

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            SelectTeamMembersActivity.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, employeeList);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                            dialog.show();
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                            ref.child("employee").child(employeeId).child("task")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() != null) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            taskUid = snapshot.getKey();

                                        }
                                        String selectedName = employeeList.get(position);
                                        Toast.makeText(getApplicationContext(),
                                                selectedName + " is added", Toast.LENGTH_SHORT).show();
                                        mLocationDatabaseReference = mFirebaseDatabase.getReference()
                                                .child("employee").child(employeeId).child("task")
                                                .child(taskUid);
                                        mLocationDatabaseReference.child("team")
                                                .push()
                                                .child("name")
                                                .setValue(selectedName);
                                        dialog.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SelectTeamMembersActivity.this,
                        HomeActivity.class));
                ToastUtil.show(SelectTeamMembersActivity.this, "Send Successfully");
            }
        });
    }
}
