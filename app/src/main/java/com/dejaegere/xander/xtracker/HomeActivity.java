package com.dejaegere.xander.xtracker;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private DataHandler handler;
    private RecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new DataHandler(this);
        Cursor allActivities = handler.getAll();
        initRecyclerView(allActivities);
    }



    protected void initRecyclerView(Cursor cursor){
        Log.d(TAG, "initRecyclerView: initiating recycler view");
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter(cursor, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.swapCursor(handler.getAll());
    }

    @Override
    protected void onResume(){
        super.onResume();
        adapter.swapCursor(handler.getAll());
    }

    public void addActivity(View v){
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

}
