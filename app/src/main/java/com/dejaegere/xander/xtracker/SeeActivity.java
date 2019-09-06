package com.dejaegere.xander.xtracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SeeActivity extends AppCompatActivity {
    private ListActivity activity;
    DataHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new DataHandler(this);
        setContentView(R.layout.activity_activity);
        activity = getIntent().getParcelableExtra("activityTag");

        TextView nameView = findViewById(R.id.activity_name);
        TextView timeView = findViewById(R.id.activity_time);

        nameView.setText(activity.getName());
        timeView.setText(activity.getTime());


    }

    public void goHome(View view){
        Intent intent =  new Intent(this,HomeActivity.class);
        startActivity(intent);
    }

    public void goToSession(View view){
        Intent intent = new Intent(this, Session.class);
        intent.putExtra("activityTag", activity);
        startActivity(intent);
    }

    public void goToAddTime(View view){
        Intent intent = new Intent(this, ChangeTime.class);
        intent.putExtra("activityTag",activity);
        startActivity(intent);

    }

    public void deleteActivity(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete activity");
        builder.setMessage("You are about to delete the activity: \"" + activity.getName() + "\". Are you sure you want to delete this activity?");
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        handler.dropRow(activity.getName());
                        Intent intent = new Intent(SeeActivity.this,HomeActivity.class);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }


}
