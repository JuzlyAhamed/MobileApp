package com.example.easycalendar;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.os.Build;
import android.util.Log;

import org.threeten.bp.LocalDate;

import java.util.Calendar;

public class MyReminder {
    private static final String CHANNEL_ID = "1234";
    private Context context;
    MyEvent event;
    public MyReminder(Context context, MyEvent event) {
        this.context = context;
        this.event = event;
    }


    public void createReminder() {

        //Creating Intent
        Intent intent = new Intent(context, ReminderBroadcast.class);
        intent.setAction("EVENT_REMINDER");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("id", event.getpKey());
        intent.putExtra("event_name", event.getEventName());
        String eventType = context.getResources().
                getStringArray(R.array.categoryOptions_array)[event.getIndex_category()];
        intent.putExtra("event_type", eventType);
        intent.putExtra("event_time", event.getStartTime().toString());
        intent.putExtra("event_date", event.getStartDate());
        intent.putExtra("event_sound", getRingtoneSource(event.getIndex_ringtone()));


        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        //getting event time and setting alarm
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        LocalDate eventDate = MyEvent.StringToDate(event.getStartDate(),'-');
        Time eventTime = event.getStartTime();

        calendar.set(eventDate.getYear(), eventDate.getMonthValue()-1, eventDate.getDayOfMonth(),
                eventTime.getHour(), eventTime.getMinute(), 0);
        switch (event.getIndex_notification()){
            case 1:
                calendar.add(Calendar.MINUTE,-10);
                break;
            case 2:
                calendar.add(Calendar.MINUTE,-30);
                break;
            case 3:
                calendar.add(Calendar.HOUR,-1);
                break;
            case 4:
                calendar.add(Calendar.HOUR,-24);
                break;
            default:
                break;
        }
        Log.i("Alarm", calendar.getTime().toString());
        if(event.getIndex_recurrence() > 0 ){
            // Create Repeating Reminder
            if(event.getIndex_recurrence() == 1){
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);}
            else if(event.getIndex_recurrence() == 2){
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY *7, pendingIntent);
            }else if(event.getIndex_recurrence() == 3){
                //TODO reset and create again when next month notification occurred
                long interval = getDuration();
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), getDuration(), pendingIntent);
            }
            else if(event.getIndex_recurrence() == 4){
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY *365, pendingIntent);
            }
        }else{
            //Create One Shot Reminder
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

    }

    private int getRingtoneSource(int index) {
        switch (index){
            case 0:
                return R.raw.tone1;
            case 1:
                return R.raw.tone2;
            case 2:
                return R.raw.tone3;
            case 3:
                return R.raw.tone4;
            case 4:
                return R.raw.tone5;
            default:
                return R.raw.tone1;
        }
    }

    public void createNotificationChannel() {
        final String packageName = context.getPackageName();

        //  Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //Uri sound = Uri.parse(source_sound);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build();
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "events", importance);
            channel.setDescription("Calendar events");
           // channel.setSound(sound, audioAttributes);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
    private long getDuration(){
        // get todays date
        Calendar cal = Calendar.getInstance();
        // get current month
        int currentMonth = cal.get(Calendar.MONTH);
        currentMonth += 1;
        // check if has not exceeded threshold of december
        if(currentMonth > Calendar.DECEMBER){
            currentMonth = Calendar.JANUARY;
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
        }

        cal.set(Calendar.MONTH, currentMonth);
        // get the maximum possible days in this month
        int maximumDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, maximumDay);
        long thenTime = cal.getTimeInMillis(); // this is time one month ahead
        return thenTime; // this is what you set as trigger point time i.e one month after
    }
}
