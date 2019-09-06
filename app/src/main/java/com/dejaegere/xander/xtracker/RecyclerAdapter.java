package com.dejaegere.xander.xtracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";
    private Cursor cursor;
    private Context mContext;

    public RecyclerAdapter(Cursor cursor, Context mContext) {
        this.cursor = cursor;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        if (!cursor.moveToPosition(i)) {
            return;
        }
        String name = cursor.getString(cursor.getColumnIndex(DataHandler.COLUMN_NAME));
        String time = cursor.getString(cursor.getColumnIndex(DataHandler.COLUMN_TIME));
        String parent = cursor.getString(cursor.getColumnIndex(DataHandler.COLUMN_PARENT));

        final ListActivity activity = new ListActivity(name, time,parent);
        viewHolder.name.setText(name);
        viewHolder.time.setText(time);
        viewHolder.listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SeeActivity.class);
                intent.putExtra("activityTag", activity);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }

        cursor = newCursor;

        if (cursor != null) {
            notifyDataSetChanged();
        }

    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView time;
        RelativeLayout listLayout;

        protected ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.activity_name);
            time = itemView.findViewById(R.id.activity_time);
            listLayout = itemView.findViewById(R.id.list_item_layout);


        }
    }

}
