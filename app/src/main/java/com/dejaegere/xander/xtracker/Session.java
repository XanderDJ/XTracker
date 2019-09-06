package com.dejaegere.xander.xtracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Chronometer;

public class Session extends AppCompatActivity {
    private static final String TAG = "Session";
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;
    private ListActivity listActivity;
    private DataHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        chronometer = findViewById(R.id.chronometer);
        Intent intent = getIntent();
        listActivity = intent.getParcelableExtra("activityTag");
        handler = new DataHandler(this);
    }

    public void startSession(View view){
        if (!running){
            //set the base to now - time it has already ran
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
        }

    }

    public void pauseSession(View view){
        if (running){
            chronometer.stop();
            running = false;
            //set the pause offset to the time the system has been running
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
        }

    }

    /*
        Stop session and saves the time in the correct place
     */
    public void stopSession(View view){
        long elapsedTime = getElapsedTime();
        listActivity.changeTime(elapsedTime,handler);
        Intent intent = new Intent(this, SeeActivity.class);
        intent.putExtra("activityTag",listActivity);
        startActivity(intent);
    }

    private long getElapsedTime (){
        long elapsedTime = running ? SystemClock.elapsedRealtime() - chronometer.getBase() : pauseOffset;
        return elapsedTime;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("pauseOffset",pauseOffset);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        pauseOffset = savedInstanceState.getLong("pauseOffset");
    }
}
