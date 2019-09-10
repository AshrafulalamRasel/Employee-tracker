package com.chumbokit.doctor.project_final_years;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private CardView profile, client;
    private ArrayList<Boolean> activeStatusList = new ArrayList<>();
    private ArrayList<String> loginTimeList = new ArrayList<>();
    private ArrayList<String> logoutTimeList = new ArrayList<>();
    private ArrayList<String> nameTimeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        profile = findViewById(R.id.profile);
        client = findViewById(R.id.client);


        setSupportActionBar(toolbar);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.profilecustomdialog, null);
                builder.setView(customLayout);
                Button task = customLayout.findViewById(R.id.task);
                Button performance = customLayout.findViewById(R.id.performance);
                task.setVisibility(View.INVISIBLE);
                performance.setVisibility(View.INVISIBLE);
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, employees.class));
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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
                        String loginTime = (String) snapshot.child("activetime").child("login").getValue();
                        String logoutTime = (String) snapshot.child("activetime").child("logout").getValue();
                        activeStatusList.add(activeStatus);
                        loginTimeList.add(loginTime);
                        logoutTimeList.add(logoutTime);
                        nameTimeList.add(name);
                    }

                   // Toast.makeText(getApplicationContext(),""+nameTimeList,Toast.LENGTH_LONG).show();
                    //Toast.makeText(getApplicationContext(),""+loginTimeList,Toast.LENGTH_LONG).show();
                  //  Toast.makeText(getApplicationContext(),""+logoutTimeList,Toast.LENGTH_LONG).show();
                   sendNotification(activeStatusList, nameTimeList, loginTimeList, logoutTimeList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void sendNotification(ArrayList<Boolean> activeStatusList, ArrayList<String> nameTimeList, ArrayList<String> loginTimeList, ArrayList<String> logoutTimeList) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0/* Request code*/, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = "Notification";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        for (int i = 0 ; i < activeStatusList.size(); i++) {
            if (!activeStatusList.get(i)) {
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channelId)
                                .setSmallIcon(R.drawable.notification)
                                .setContentTitle(nameTimeList.get(i))
                                .setContentText("is Logout at " + logoutTimeList.get(i))
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(i/* ID of notification*/ , notificationBuilder.build());
            } else if(activeStatusList.get(i)){
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, channelId)
                                .setSmallIcon(R.drawable.notification)
                                .setContentTitle(nameTimeList.get(i))
                                .setContentText("is Login at " + loginTimeList.get(i))
                                .setAutoCancel(true)
                                .setSound(defaultSoundUri)
                                .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                // Since android Oreo notification channel is needed.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId,
                            "Channel human readable title",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(i /*ID of notification*/ , notificationBuilder.build());
            }
            Toast.makeText(getApplicationContext(),""+activeStatusList.get(i)+nameTimeList.get(i)+loginTimeList.get(i)+logoutTimeList.get(i),Toast.LENGTH_LONG).show();
        }

    }
}
