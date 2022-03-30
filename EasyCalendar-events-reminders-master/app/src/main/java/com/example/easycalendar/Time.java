package com.example.easycalendar;

import io.realm.RealmObject;

public class Time extends RealmObject {
    private Integer hour;
    private Integer minute;
    private String sep = ":";

    public Time(){}

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }


    public Time(int hour, int minute, String  sep) {
        this.hour = hour;
        this.minute = minute;
        this.sep = sep;
    }

    public Time(String hourAndMinute, String sep){
        String[] tokens = hourAndMinute.split(String.valueOf(sep));
       // if(tokens.length != 2)
            //throw Exception("Argument not proper for Time class");
        this.hour = Integer.valueOf(tokens[0]);
        this.minute = Integer.valueOf(tokens[1]);
        this.sep = sep;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getSep() {
        return sep;
    }

    public void setSep(String sep) {
        this.sep = sep;
    }

    public boolean isAfter(Time aTime){
        if(hour > aTime.getHour())
            return true;
        if(hour < aTime.getHour())
            return false;
        if(minute > aTime.getMinute())
            return true;
        else
            return false;
    }

    public boolean isEqual(Time aTime){
        if(hour == aTime.getHour() && minute == aTime.getMinute())
            return true;
        return false;
    }
    public boolean isBefore(Time aTime){
        if(isEqual(aTime))
            return false;
        return ! this.isAfter(aTime);
    }

    @Override
    public String toString(){
        String hour_text,minute_text;
        if(hour < 10)
            hour_text = "0"+ hour.toString();
        else
            hour_text =  hour.toString();
        if(minute <10)
            minute_text = "0"+ minute.toString();
        else
            minute_text = minute.toString();
        return hour_text + sep + minute_text;
    }
}
