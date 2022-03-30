package com.example.easycalendar;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class PreferencesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ImageButton BtnBack;
    private Spinner SpinnerNotificationTime;
    private Spinner SpinnerNotificationRecurrence;
    private Spinner SpinnerNotificationSound;
    private Switch NightMode;
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        initViews();
        getSharedPreferences();
        setListeners();


    }

    private void setListeners() {
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        NightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("mode", true);
                    editor.commit();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("mode", false);
                    editor.commit();
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int index;
        switch (parent.getId()) {
            case R.id.Preferences_spinner_notificationTime:
                index = SpinnerNotificationTime.getSelectedItemPosition();
                editor.putInt("time", index);
                editor.commit();
                break;
            case R.id.Preferences_spinner_notificationRecurrence:
                index = SpinnerNotificationRecurrence.getSelectedItemPosition();
                editor.putInt("recurrence", index);
                editor.commit();
                break;
            case R.id.Preferences_spinner_notificationSound:
                index = SpinnerNotificationSound.getSelectedItemPosition();
                editor.putInt("sound", index);
                editor.commit();
                break;
            default:
                break;
        }
    }

    private void initViews() {
        context = getApplicationContext();
        BtnBack = (ImageButton)findViewById( R.id.Preferences_btn_back );
        SpinnerNotificationTime = (Spinner)findViewById( R.id.Preferences_spinner_notificationTime );
        SpinnerNotificationRecurrence = (Spinner)findViewById( R.id.Preferences_spinner_notificationRecurrence );
        SpinnerNotificationSound = (Spinner)findViewById( R.id.Preferences_spinner_notificationSound );
        NightMode = (Switch)findViewById( R.id.Preferences_nightMode );

        ArrayAdapter<CharSequence> adptr_notification = ArrayAdapter.createFromResource(context,
                R.array.notificationOptions_array, android.R.layout.simple_spinner_item);
        adptr_notification.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerNotificationTime.setAdapter(adptr_notification);


        ArrayAdapter<CharSequence> adptr_recurrance = ArrayAdapter.createFromResource(context,
                R.array.recurranceOptions_array, android.R.layout.simple_spinner_item);
        adptr_recurrance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerNotificationRecurrence.setAdapter(adptr_recurrance);


        ArrayAdapter<CharSequence> adptr_ringtones = ArrayAdapter.createFromResource(context,
                R.array.ringtones_array, android.R.layout.simple_spinner_item);
        adptr_ringtones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerNotificationSound.setAdapter(adptr_ringtones);

        SpinnerNotificationSound.setOnItemSelectedListener(this);
        SpinnerNotificationTime.setOnItemSelectedListener(this);
        SpinnerNotificationRecurrence.setOnItemSelectedListener(this);


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getSharedPreferences(){

        pref = context.getSharedPreferences("default", context.MODE_PRIVATE);
        editor = pref.edit();

        //Getting values from shared preferences
        int indexRecurrence = pref.getInt("recurrence",0);
        int indexTime = pref.getInt("time",0);
        int indexSound = pref.getInt("sound",0);
        Boolean nightMode = pref.getBoolean("mode",false);

        /*Setting values*/
        SpinnerNotificationTime.setSelection(indexTime);
        SpinnerNotificationRecurrence.setSelection(indexRecurrence);
        SpinnerNotificationSound.setSelection(indexSound);
        NightMode.setChecked(nightMode);



    }
}


