package com.itvillageltd.emphoyee.tracker;

import android.Manifest.permission;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itvillage.dev.basicutil.ToastUtil;
import com.itvillageltd.emphoyee.tracker.Adapter.TasksList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class EmployeeHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private CardView profile;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLocationDatabaseReference;
    private FirebaseUser firebaseUser;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseAuth firebaseAuth;
    private ListView taskList;
    private Runnable runnable;
    private double lat, longi;
    private ProgressDialog dialog;
    private Button checkIn, checkOut;
    private ArrayList<String> mainName = new ArrayList<>();
    private ArrayList<String> timedate = new ArrayList<>();
    private ArrayList<String> subtitle = new ArrayList<>();
    private ArrayList<String> taskIdList = new ArrayList<>();


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);

        FirebaseApp.initializeApp(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profile = findViewById(R.id.profile);
        taskList = (ListView) findViewById(R.id.LogsList);
        checkIn = findViewById(R.id.checkin);
        checkOut = findViewById(R.id.checkout);

        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                mLocationDatabaseReference.child(firebaseUser.getUid()).child("activetime").child("login").push().child("time").setValue(dtf.format(now));
                mLocationDatabaseReference.child(firebaseUser.getUid()).child("activeStatus").setValue(true);
                ToastUtil.show(EmployeeHomeActivity.this, "Thank You for your attendee");
            }
        });
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();

                mLocationDatabaseReference.child(firebaseUser.getUid()).child("activetime").child("logout").push().child("time").setValue(dtf.format(now));
                mLocationDatabaseReference.child(firebaseUser.getUid()).child("activeStatus").setValue(false);
                ToastUtil.show(EmployeeHomeActivity.this, "Check Out Successful");
            }
        });
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationDatabaseReference = mFirebaseDatabase.getReference().child("employee");
        firebaseAuth = FirebaseAuth.getInstance();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeHomeActivity.this);
                final View customLayout = getLayoutInflater().inflate(R.layout.profilecustomdialog, null);
                builder.setView(customLayout);
                Button task = customLayout.findViewById(R.id.task);
                Button performance = customLayout.findViewById(R.id.performance);
                Button attendence = customLayout.findViewById(R.id.attendence);
                task.setVisibility(View.INVISIBLE);
                performance.setVisibility(View.INVISIBLE);
                attendence.setVisibility(View.INVISIBLE);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        requestPermission();
        getLocation();
        TaskList();


    }

    /*
    *
    * Real Location Tracking
    *
    * */
    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(EmployeeHomeActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    lat = location.getLatitude();
                    longi = location.getLongitude();
                    final Handler handler = new Handler();
                    runnable = new Runnable() {
                        public void run() {
                            mLocationDatabaseReference.child(firebaseUser.getUid()).child("location").child("lat").setValue(lat);
                            mLocationDatabaseReference.child(firebaseUser.getUid()).child("location").child("lng").setValue(longi);

                            handler.postDelayed(runnable, 1000);
                        }
                    };
                    handler.post(runnable);


                }
            }
        });
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    /*
    *
    * Toolbar Code
    *
    * */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.employee_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {
        } else if (id == R.id.nav_task) {
        } else if (id == R.id.signout) {
            try {
                firebaseAuth.signOut();
                Intent intent = new Intent(EmployeeHomeActivity.this, LoginEmployeeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                startActivity(intent);

            } catch (Exception e) {
                Log.e("Error", "onClick: Exception " + e.getMessage(), e);
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            Intent intent = new Intent(EmployeeHomeActivity.this,
                                    LoginEmployeeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            firebaseAuth.signOut();
                        } catch (Exception e) {
                            Log.e("Error", "onClick: Exception " + e.getMessage(), e);
                        }
                        finish();
                    }

                }).create().show();
    }

    public void TaskList() {

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading in please wait...");
        dialog.setIndeterminate(true);
        dialog.show();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("employee").child(firebaseUser.getUid()).child("task")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //depending upon what datatype youre using caste to it.
                        String title = (String) snapshot.child("employeesList").getValue();
                        String description = (String) snapshot.child("taskList").getValue();
                        String time = (String) snapshot.child("time").getValue();
                        String taskId = snapshot.getKey();

                        mainName.add(title);
                        subtitle.add(description);
                        taskIdList.add(taskId);
                        timedate.add(time);

                        TasksList adapter = new TasksList(EmployeeHomeActivity.this,
                                mainName, subtitle, taskIdList, timedate, firebaseUser.getUid());

                        taskList.setAdapter(adapter);
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
