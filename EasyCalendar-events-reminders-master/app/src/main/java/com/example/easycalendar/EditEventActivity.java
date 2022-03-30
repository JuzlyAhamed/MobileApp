package com.example.easycalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.realm.Realm;
import io.realm.RealmResults;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class EditEventActivity extends AppCompatActivity implements View.OnClickListener{
    private Realm realm;
    private Button btn_editEvent;
    private Button btn_back;
    EventInformationFragment fragment;
    private String primaryKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);


        Intent intent = getIntent();
         primaryKey = intent.getStringExtra("primaryKey");

        initViews();
        setFragment();




    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_editEvent:
                updateEventFromDb();
                break;
            case R.id.EditEventActivity_btn_back:
                finish();
                break;
            default:
                break;
        }
    }

    void updateEventFromDb(){

        MyEvent myEvent = fragment.getInputs();

        //find parent primary key for recurrence events
        String [] pkey = primaryKey.split("_");
        String parentPkey = pkey[0];

        // Updating a boolean field
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<MyEvent> results = realm.where(MyEvent.class)
                        .contains("pKey", parentPkey).findAll();
                results.setString("eventName", myEvent.getEventName());
                results.setInt("index_category", myEvent.getIndex_category());
                results.setString("startDate", myEvent.getStartDate());
                results.setString("endDate", myEvent.getEndDate());
                results.setInt("index_notification", myEvent.getIndex_notification());
                results.setInt("email_notification", myEvent.getEmail_notification());
                results.setString("notes", myEvent.getNotes());
                results.setInt("index_recurrence", myEvent.getIndex_recurrence());
                results.setString("eventLocation", myEvent.getEventLocation());
                results.setInt("color", myEvent.getColor());
                results.setInt("index_ringtone", myEvent.getIndex_ringtone());


                //TODO update notification
            }
        });


        Toast.makeText(this, "Etkinlik Güncellendi", Toast.LENGTH_SHORT).show();

        //close the activity and return the menu
        Intent intent = new Intent(EditEventActivity.this,MainActivity.class);
        finish();
        startActivity(intent);
    }

    private void setFragment() {

        //getting object
        MyEvent myEvent = realm.where(MyEvent.class).equalTo("pKey",primaryKey).findFirst();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new EventInformationFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        fragmentTransaction.add(R.id.fragment_container, fragment);

        fragmentTransaction.runOnCommit( new Runnable(){
            @Override
            public void run() {
                fragment.setInputs(myEvent);
            }
        });
        fragmentTransaction.commit();



    }

    private void initViews() {

        btn_editEvent = findViewById(R.id.btn_editEvent);
        btn_back = findViewById(R.id.EditEventActivity_btn_back);
        realm = Realm.getDefaultInstance();

        btn_editEvent.setOnClickListener(this);
        btn_back.setOnClickListener(this);

    }
}
