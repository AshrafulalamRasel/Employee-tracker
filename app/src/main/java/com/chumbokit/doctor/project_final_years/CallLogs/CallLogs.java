package com.chumbokit.doctor.project_final_years.CallLogs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.chumbokit.doctor.project_final_years.Adapter.CallLists;
import com.chumbokit.doctor.project_final_years.R;

public class CallLogs extends AppCompatActivity {
    ListView callList;
    String[] maintitle = {
            "Faver", "Diabetes",
            "Crohn's & Colitis", "Lupus",
            "Celiac Disease",
    };
    String[] CallDate = {
            "Faver", "Diabetes",
            "Crohn's & Colitis", "Lupus",
            "Celiac Disease",
    };

    String[] subtitle = {
            "sunday,2019-12-8", "sunday,2019-12-8",
            "sunday,2019-12-8", "sunday,2019-12-8",
            "sunday,2019-12-8",
    };

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
        CallLists adapter = new CallLists(this, maintitle, subtitle, CallDate, imgid);
        callList = (ListView) findViewById(R.id.callList);
        callList.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
