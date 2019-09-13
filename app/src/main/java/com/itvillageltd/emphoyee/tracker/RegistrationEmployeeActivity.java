package com.itvillageltd.emphoyee.tracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationEmployeeActivity extends AppCompatActivity {

    private String Name, email, password;
    private RelativeLayout rlayout;
    private Animation animation;
    private EditText Username, Email, Password;
    private Button mRegisterbtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mdatabase;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_employee);

        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Username = findViewById(R.id.etUserName);
        Email = findViewById(R.id.etUserEmail);
        Password = findViewById(R.id.etUserPassword);
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("employee");
        mRegisterbtn = findViewById(R.id.SignUp);

        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserRegister();

            }

        });


    }


    private void UserRegister() {

        String empolyeePassword = "employee";

        Name = Username.getText().toString().trim();
        email = Email.getText().toString().trim();
        password = (Password.getText() + empolyeePassword).toString().trim();

        Log.e("Password", password);

        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(RegistrationEmployeeActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(RegistrationEmployeeActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegistrationEmployeeActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        } else if (Password.length() < 6) {
            Toast.makeText(RegistrationEmployeeActivity.this, "Passwor must be greater then 6 digit", Toast.LENGTH_SHORT).show();
            return;
        }

        mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendEmailVerification();
                    mDialog.dismiss();
                    OnAuth();
                    mAuth.signOut();
                } else {
                    Toast.makeText(RegistrationEmployeeActivity.this, "error on creating user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void sendEmailVerification() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegistrationEmployeeActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationEmployeeActivity.this, LoginEmployeeActivity.class);
                        startActivity(intent);
                        mdatabase.child(user.getUid()).child("name").setValue(Name);
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }

    private void OnAuth() {
        Toast.makeText(RegistrationEmployeeActivity.this, "Verify Your Email Address", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
