package com.example.easycalendar;


import org.threeten.bp.LocalDate;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;


public class MyEvent extends RealmObject {

    @Required
    @PrimaryKey
    private String pKey = UUID.randomUUID().toString();
    private String eventName;
    private int index_category;
    private int color =-6543440;
    private int index_ringtone;
    private Time startTime;
    private Time endTime;
    private String startDate;
    private String endDate;
    private int index_notification;
    private int email_notification;
    private String email;
    private boolean rememberEmail;
    private String notes;
    private int index_recurrence;
    private String eventLocation;

    public MyEvent(){
    }

    public MyEvent(String eventName, int index_category, int color, Time startTime, Time endTime,
                   String startDate, String endDate, int index_notification, String notes,
                   int index_recurrence,int index_ringtone,String eventLocation) {
        this.eventName = eventName;
        this.index_category = index_category;
        this.color = color;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.index_notification = index_notification;
        this.notes = notes;
        this.index_recurrence = index_recurrence;
        this.index_ringtone = index_ringtone;
        this.eventLocation= eventLocation;
    }
    public static LocalDate StringToDate(String dateString,char sep){
        String [] tokens = dateString.split(String.valueOf(sep));
        //if(tokens.length != 3)

        LocalDate date = LocalDate.of( Integer.valueOf(tokens[0]), Integer.valueOf(tokens[1]),Integer.valueOf(tokens[2]) );
        return date;
    }

    public MyEvent(Time time,int index_category,String eventName, String startDate) {
        this.index_category = index_category;
        this.eventName = eventName;
        this.startDate = startDate;
        this.startTime = time;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getIndex_category() {
        return index_category;
    }

    public void setIndex_category(int index_category) {
        this.index_category = index_category;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getIndex_notification() {
        return index_notification;
    }

    public void setIndex_notification(int index_notification) {
        this.index_notification = index_notification;
    }

    public int getEmail_notification() {
        return email_notification;
    }

    public void setEmail_notification(int email_notification) {
        this.email_notification = email_notification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isRememberEmail() {
        return rememberEmail;
    }

    public void setRememberEmail(boolean rememberEmail) {
        this.rememberEmail = rememberEmail;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getIndex_recurrence() {
        return index_recurrence;
    }

    public void setIndex_recurrence(int index_recurrence) {
        this.index_recurrence = index_recurrence;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public int getIndex_ringtone() {
        return index_ringtone;
    }

    public void setIndex_ringtone(int index_ringtone) {
        this.index_ringtone = index_ringtone;
    }

    public String getpKey() {
        return pKey;
    }
    public void setpKey(String pKey){
        this.pKey = pKey;
    }
}
