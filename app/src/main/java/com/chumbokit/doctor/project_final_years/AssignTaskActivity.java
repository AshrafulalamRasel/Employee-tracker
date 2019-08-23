package com.chumbokit.doctor.project_final_years;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itvillage.dev.basicutil.ToastUtil;

import java.util.ArrayList;

public class AssignTaskActivity extends AppCompatActivity {
    EditText title, description;
    Button send;
    ArrayList<String> mainName = new ArrayList<>();
    String uid;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLocationDatabaseReference;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_task);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        send = findViewById(R.id.send);
        uid = getIntent().getStringExtra("uid");

        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mLocationDatabaseReference = mFirebaseDatabase.getReference().child("employee").child(uid).child("task").push();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading in please wait...");
        dialog.setIndeterminate(true);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                mLocationDatabaseReference.child("title").setValue(title.getText().toString());
                mLocationDatabaseReference.child("description").setValue(description.getText().toString());
                title.setText("");
                description.setText("");
                ToastUtil.show(AssignTaskActivity.this, "Send Successfully");
                dialog.dismiss();
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
