package com.example.easycalendar;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.media.MediaPlayer;
import android.net.Uri;

import java.util.UUID;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {

    private static final String CHANNEL_ID = "1234";
    private NotificationCompat.Builder builder;
    private Context context;
    private Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("EVENT_REMINDER")) {
            this.context = context;
            this.intent = intent;
            //createNotificationChannel();
            createNotification();

            final String packageName = context.getPackageName();
            String source_sound = "android.resource://" + packageName + "R.raw.promise";
            Uri sound = Uri.parse(source_sound);

        }
    }

    private void createNotification() {

        String uuid = intent.getStringExtra("id");
        String event_name = intent.getStringExtra("event_name");
        String event_type = intent.getStringExtra("event_type");
        String event_time = intent.getStringExtra("event_time");
        String event_date = intent.getStringExtra("event_date");
        int ringtone = intent.getIntExtra("event_sound",R.raw.tone1);

        MediaPlayer mediaPlayer = MediaPlayer.create(context,ringtone);
        mediaPlayer.start();

        Intent fullScreenIntent = new Intent(context, MainActivity.class);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(event_type)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(event_name + " \n" + event_date + " " + event_time))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setFullScreenIntent(fullScreenPendingIntent, true)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(generateID(uuid), builder.build());

    }


    private int generateID(String uuid){
        UUID myuuid = UUID.fromString(uuid);
        Long highbits = myuuid.getMostSignificantBits();
        int id = (int)(highbits >> 32);
        return id;

    }
}
