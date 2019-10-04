package com.chumbokit.doctor.project_final_years;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itvillage.dev.basicutil.ToastUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AssignTaskActivity extends AppCompatActivity {
    EditText title, description;
    Button send;
    ArrayList<String> mainName = new ArrayList<>();
    String uid;
    private DatePickerDialog picker;
    private EditText selectedDate;
    private String date, dateOfAppointment;
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
        selectedDate = findViewById(R.id.editText1);
        selectedDate.setInputType(InputType.TYPE_NULL);
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
                mLocationDatabaseReference.child("pickuptime").setValue(selectedDate.getText().toString());


                title.setText("");
                description.setText("");
                selectedDate.setText("");
                ToastUtil.show(AssignTaskActivity.this, "Send Successfully");
                dialog.dismiss();
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
                                    dateOfAppointment = strDate;
                                    selectedDate.setText(strDate);
                                    Log.d("selected date", strDate);

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
