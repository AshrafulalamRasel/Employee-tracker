package com.chumbokit.doctor.project_final_years;

import android.Manifest.permission;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ListView;

import com.chumbokit.doctor.project_final_years.CallLogs.LogObject;
import com.chumbokit.doctor.project_final_years.CallLogs.LogsAdapter;
import com.chumbokit.doctor.project_final_years.CallLogs.LogsManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class EmployeeHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private CardView profile;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mLocationDatabaseReference;
    private FirebaseUser firebaseUser;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private FirebaseAuth firebaseAuth;
    private ListView logList;
    private Runnable runnable;
    private double lat, longi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profile = findViewById(R.id.profile);
        logList = (ListView) findViewById(R.id.LogsList);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        FirebaseApp.initializeApp(this);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationDatabaseReference = mFirebaseDatabase.getReference().child("employee");
        firebaseAuth = FirebaseAuth.getInstance();
        requestPermission();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeHome.this);
                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.profilecustomdialog, null);
                builder.setView(customLayout);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        getLocation();
        loadLogs();
        mLocationDatabaseReference.child(firebaseUser.getUid()).child("activeStatus").setValue(true);
    }

    //    ------------------------ Real Location------------------------------
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(EmployeeHome.this, new OnSuccessListener<Location>() {
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
/*----------------------------Call Log------------------------------------*/

    private void loadLogs() {
        LogsManager logsManager = new LogsManager(this);
        List<LogObject> callLogs = logsManager.getLogs(LogsManager.ALL_CALLS);
        LogsAdapter logsAdapter = new LogsAdapter(this, R.layout.log_layout, callLogs);
        logList.setAdapter(logsAdapter);

    }

    /*----------------------------------------------Tollbar Code---------------------------------------------------------------*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.employee_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_task) {
            Intent intent = new Intent(EmployeeHome.this, EmployeeTasksShow.class);
            startActivity(intent);
        } else if (id == R.id.signout) {
            try {
                firebaseAuth.signOut();
                Intent intent = new Intent(EmployeeHome.this, LoginEmployee.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                mLocationDatabaseReference.child(firebaseUser.getUid()).child("activeStatus").setValue(false);
                startActivity(intent);

            } catch (Exception e) {
                Log.e("Error", "onClick: Exception " + e.getMessage(), e);
            }

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

                    public void onClick(DialogInterface arg0, int arg1) {
                        try {
                            firebaseAuth.signOut();
                            Intent intent = new Intent(EmployeeHome.this, LoginEmployee.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);//makesure user cant go back
                            mLocationDatabaseReference.child(firebaseUser.getUid()).child("activeStatus").setValue(false);
                            startActivity(intent);

                        } catch (Exception e) {
                            Log.e("Error", "onClick: Exception " + e.getMessage(), e);
                        }
                        finish();
                    }

                }).create().show();
    }
}
