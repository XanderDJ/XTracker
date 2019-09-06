package com.dejaegere.xander.xtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;


public class DataHandler extends SQLiteOpenHelper {

    private static int VERSION = 2;
    private static String DATABASENAME = "XTracker.db";
    private static String TABLENAME = "activity";
    public static String COLUMN_NAME = "name";
    public static String COLUMN_TIME = "time";
    public static String COLUMN_PARENT = "parent";
    public static String FROM_1_TO_2 = "ALTER TABLE "
            + TABLENAME + " ADD COLUMN " + COLUMN_PARENT + " varchar(200)";
    public static String CHANGING_PARENTS_FROM_NULL_TO_NONE = "UPDATE " + TABLENAME + " SET parent=\"None\" " + "WHERE parent IS NULL";


    public DataHandler(@Nullable Context context) {
        super(context, DATABASENAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLENAME + " (name varchar(200), time varchar(150), parent varchar(200));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            sqLiteDatabase.execSQL(FROM_1_TO_2);
            sqLiteDatabase.execSQL(CHANGING_PARENTS_FROM_NULL_TO_NONE);
        }
    }

    public Cursor getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"name", "time", "parent"};
        return db.query(TABLENAME, columns, null, null, null, null, null);
    }

    public Cursor getAllActivitiesNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"name"};
        return db.query(TABLENAME, columns, null, null, null, null, null);
    }

    public Cursor getAllActivitiesNamesAndTimes(){
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"name","time"};
        return db.query(TABLENAME, columns, null, null, null, null, null);
    }

    public Cursor getActivity(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"name", "time", "parent"};
        String[] selectionArgs = {name};
        return db.query(TABLENAME, columns, "name = ?", selectionArgs, null, null, null);
    }

    public void insertRow(String name, String time, String parent) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("time", time);
        values.put("parent", parent);
        database.insert(TABLENAME, null, values);
    }

    public void updateRow(String name, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put("time", time);
        String[] whereArgs = {name};
        db.update(TABLENAME, value, "name = ?", whereArgs);
    }

    public void dropRow(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] args = {name};
        db.delete(TABLENAME, "name = ?", args);
    }
}
