package com.example.easycalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import io.realm.Realm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.threeten.bp.LocalDate;

import java.util.ArrayList;

public class AddEventActivity extends AppCompatActivity implements View.OnClickListener{

    private final LocalDate maxDate = LocalDate.of(2050,12,30);
    private final  int MAX_RECURRENCE_COUNT = 100000;

    private Realm realm;
    private Button btn_addEvent;
    private Button btn_back;
    String chosenDate;
    EventInformationFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        Intent intent = getIntent();
        chosenDate = intent.getStringExtra("date");

        initViews();

        createFragment();


    }

    private void createFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragment = new EventInformationFragment().newInstance(chosenDate);
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void initViews() {
        btn_addEvent = findViewById(R.id.btn_completeAddingEvent);
        btn_back = findViewById(R.id.AddEventActivity_btn_back);
        realm = Realm.getDefaultInstance();

        btn_addEvent.setOnClickListener(this);
        btn_back.setOnClickListener(this);
    }

    private void addEvent(){

        MyEvent myEvent = fragment.getInputs();
        ArrayList<MyEvent> myEventsRecurrences = getRecurrenceEvents(myEvent);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for(MyEvent event: myEventsRecurrences){
                     realm.copyToRealm(event);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(AddEventActivity.this, "Etkinlik kaydedildi", Toast.LENGTH_SHORT).show();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Toast.makeText(AddEventActivity.this, "Bir hata meydana geldi", Toast.LENGTH_SHORT).show();
            }
        });

        if(myEvent.getIndex_notification() != 0) {
            // if notfication is desirable
            MyReminder remin = new MyReminder(getApplicationContext(), myEvent);
            remin.createReminder();
            remin.createNotificationChannel();
        }

        //close the activity and return the menu
        Intent intent = new Intent(AddEventActivity.this,MainActivity.class);
        finish();
        startActivity(intent);
    }

    private ArrayList<MyEvent> getRecurrenceEvents(MyEvent mainEvent) {

        ArrayList<MyEvent> recurrence = new ArrayList<>();
        LocalDate startDate = MyEvent.StringToDate(mainEvent.getStartDate(),'-');

        String parentPkey = mainEvent.getpKey();

        recurrence.add(mainEvent);
        int count = 0;
        if (mainEvent.getIndex_recurrence() > 0) {
            while (startDate.isBefore(maxDate) && count < MAX_RECURRENCE_COUNT) {
                MyEvent childEvent = fragment.getInputs();

                if (mainEvent.getIndex_recurrence() == 1)
                    startDate = startDate.plusDays(1);
                else if (mainEvent.getIndex_recurrence() == 2)
                    startDate = startDate.plusWeeks(1);
                else if (mainEvent.getIndex_recurrence() == 3)
                    startDate = startDate.plusMonths(1);
                else if (mainEvent.getIndex_recurrence() == 4)
                    startDate = startDate.plusYears(1);

                childEvent.setStartDate(startDate.toString());
                childEvent.setpKey(parentPkey + "_" + count);
                count += 1;
                recurrence.add(childEvent);
            }
        }
        return recurrence;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_completeAddingEvent:
                addEvent();

                break;
            case R.id.AddEventActivity_btn_back:
                finish();
                break;
            default:
                break;
        }
    }
}
