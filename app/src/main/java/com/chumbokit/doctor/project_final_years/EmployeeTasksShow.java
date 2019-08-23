package com.chumbokit.doctor.project_final_years;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.chumbokit.doctor.project_final_years.Adapter.TasksList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeTasksShow extends AppCompatActivity {
    ListView tasklist;
    ArrayList<String> mainName = new ArrayList<>();
    ArrayList<String> subtitle = new ArrayList<>();
    private ProgressDialog dialog;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_tasks_show);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        tasklist = findViewById(R.id.tasklist);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("employee").child(firebaseUser.getUid()).child("task").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //depending upon what datatype youre using caste to it.
                        String title = (String) snapshot.child("title").getValue();
                        String description = (String) snapshot.child("description").getValue();

                        mainName.add(title);
                        subtitle.add(description);

                        TasksList adapter = new TasksList(EmployeeTasksShow.this, mainName, subtitle);

                        tasklist.setAdapter(adapter);
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
