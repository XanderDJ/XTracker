package com.dejaegere.xander.xtracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class AddActivity extends AppCompatActivity {

    DataHandler handler;
    String parent = "None";
    String time = "0H 0M 0S";
    final ArrayList<String> names = new ArrayList<>();
    final ArrayList<String> times = new ArrayList<>();
    Spinner spinner;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);
        handler = new DataHandler(this);
        spinner = findViewById(R.id.add_activity_spinner);
        Cursor results = handler.getAllActivitiesNamesAndTimes();
        names.add(parent);
        times.add(time);
        int columnIndex=results.getColumnIndex(DataHandler.COLUMN_NAME);
        int timeIndex = results.getColumnIndex(DataHandler.COLUMN_TIME);
        while(results.moveToNext()) {
            names.add(results.getString(columnIndex));
            times.add(results.getString(timeIndex));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,names);
        spinner.setAdapter(adapter);
    }

    public  void  changeTime(View view){
        String activityParent =spinner.getSelectedItem().toString();
        parent = activityParent;
        int position = names.indexOf(parent);
        String newTime = times.get(position);
        time = newTime;
        EditText editText = findViewById(R.id.add_activity_start_et);
        editText.setText(newTime);
    }

    public void addActivity(View view){
        EditText activityNameET = findViewById(R.id.addActivityEdit);
        String activityName = activityNameET.getText().toString();
        String activityParent =spinner.getSelectedItem().toString();
        handler.insertRow(activityName,time,activityParent);
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
}
