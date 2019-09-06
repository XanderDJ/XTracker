package com.dejaegere.xander.xtracker;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ListActivity implements Parcelable {
    private String name;
    private String time;
    private String parentName;

    public ListActivity(String name, String time, String parentName) {
        this.name = name;
        this.time = time;
        this.parentName = parentName;
    }

    public ListActivity(String name, String time) {
        this(name, time, "None");
    }

    public ListActivity(Parcel in) {
        String[] data = new String[3];
        in.readStringArray(data);
        name = data[0];
        time = data[1];
        parentName = data[2];

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public ListActivity getParentActivity(DataHandler handler) {
        Cursor result = handler.getActivity(getParentName());
        result.moveToNext();
        int name = result.getColumnIndex(DataHandler.COLUMN_NAME);
        int time = result.getColumnIndex(DataHandler.COLUMN_TIME);
        int parent = result.getColumnIndex(DataHandler.COLUMN_PARENT);
        return new ListActivity(result.getString(name), result.getString(time), result.getString(parent));
    }

    public void changeTime(long milliseconds, DataHandler handler) {
        int seconds = (int) milliseconds / 1000;
        int secondsMod = seconds % 60;
        int minutes = seconds / 60;
        int minutesMod = minutes % 60;
        int hours = minutes / 60;
        changeTime(hours, minutesMod, secondsMod, handler);

    }

    public void changeTime(int hours, int minutes, int seconds, DataHandler handler) {
        Pattern pattern = Pattern.compile("^(\\d+)H (\\d+)M (\\d+)S");
        Matcher matcher = pattern.matcher(getTime());
        if (!getParentName().equals("None")) {
            ListActivity parentActivity = getParentActivity(handler);
            parentActivity.changeTime(hours, minutes, seconds, handler);
        }
        if (matcher.find()) {
            // getting the old times from time string
            int oldHours = Integer.valueOf(matcher.group(1));
            int oldMinutes = Integer.valueOf(matcher.group(2));
            int oldSeconds = Integer.valueOf(matcher.group(3));

            // calculating new time variables
            int newSeconds,newMinutes,newHours;

            // check if time needs to be removed or added
            if (hours < 0 || minutes < 0 || seconds <0){
                // % doesn't seem to put - numbers back to + so i'm doing the wrap around manually
                newSeconds = oldSeconds + seconds;
                newSeconds = newSeconds< 0 ? 60 + newSeconds : newSeconds;
                // if the new seconds wrapped around then the new minutes need to be subtracted by one
                newMinutes = oldMinutes + minutes;
                newMinutes = oldSeconds + seconds < 0 ? newMinutes -1 : newMinutes;
                newMinutes = newMinutes < 0 ? 60 + newMinutes : newMinutes;

                // if the new minutes wrapped around then the new hours need to be subtracted by one
                newHours = oldHours + hours;
                newHours = oldMinutes + minutes < 0 ? newHours -1 : newHours;
                if ( newHours < 0){
                    newHours = newMinutes = newSeconds = 0;
                }
            }
            // adding time
            else {

                newSeconds = (oldSeconds + seconds) % 60;
                newMinutes = (oldMinutes + minutes + (oldSeconds + seconds) / 60) % 60;
                newHours = oldHours + hours + (oldMinutes + minutes + (oldSeconds + seconds) / 60) / 60;
            }

            String time = String.format(Locale.ENGLISH, "%dH %dM %dS", newHours, newMinutes, newSeconds);
            setTime(time);
            handler.updateRow(getName(), getTime());
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{name, time, parentName});
    }

    public static final Parcelable.Creator<ListActivity> CREATOR = new Parcelable.Creator<ListActivity>() {

        @Override
        public ListActivity createFromParcel(Parcel parcel) {
            return new ListActivity(parcel);
        }

        @Override
        public ListActivity[] newArray(int i) {
            return new ListActivity[i];
        }
    };

}
