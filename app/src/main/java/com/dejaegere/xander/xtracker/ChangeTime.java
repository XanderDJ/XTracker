package com.dejaegere.xander.xtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

public class ChangeTime extends AppCompatActivity {

    private DataHandler handler;
    private ListActivity activity;
    private ToggleButton toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_time);
        handler = new DataHandler(this);
        activity = getIntent().getParcelableExtra("activityTag");
        toggle = findViewById(R.id.change_time_toggle);
    }

    private boolean operationIsAdd() {
        return !toggle.isChecked();
    }

    protected void onBtnClick(View view) {
        EditText hoursT = findViewById(R.id.addTimeHours);
        EditText minutesT = findViewById(R.id.addTimeMinutes);
        EditText secondsT = findViewById(R.id.addTimeSeconds);
        int hours = hoursT.getText().length() != 0 ? Integer.valueOf(hoursT.getText().toString()) : 0;
        int minutes = minutesT.getText().length() != 0 ? Integer.valueOf(minutesT.getText().toString()) : 0;
        int seconds = secondsT.getText().length() != 0 ? Integer.valueOf(secondsT.getText().toString()) : 0;
        if (!validNumber(minutes)) {
            minutesT.setError("Minutes must be between 0 and 60");
        }
        if (!validNumber(seconds)) {
            secondsT.setError("Seconds must be between 0 and 60");
        }

        if (!validNumber(minutes) || !validNumber(seconds)) {
            return;
        }

        if (operationIsAdd()) activity.changeTime(hours, minutes, seconds, handler);
        else activity.changeTime(-hours, -minutes, -seconds, handler);

        Intent intent = new Intent(this, SeeActivity.class);
        intent.putExtra("activityTag", activity);
        startActivity(intent);
    }

    protected boolean validNumber(int number) {
        if (Math.abs(number) >= 60 || number < 0) return false;
        return true;
    }

}
