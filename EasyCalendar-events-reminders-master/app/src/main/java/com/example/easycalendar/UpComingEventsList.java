package com.example.easycalendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.time.LocalDate;

public class UpComingEventsList extends AppCompatActivity implements View.OnClickListener{
private RecyclerView recyclerView_UpcomingEvents;
    private LinearLayoutManager layoutManager;

   RealmResults<MyEvent> eventsDb;
    private MyRecyclerViewAdapter myRecyclerViewAdapter;

    private ImageButton btn_mainMenu;
    private Spinner showingInterval;
    private Realm realm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_coming_events_list);

        recyclerView_UpcomingEvents = findViewById(R.id.recyclerView_UpcomingEvents);
        btn_mainMenu = findViewById(R.id.btn_MainMenu);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        showingInterval =findViewById(R.id.spinner_showingInterval);
        recyclerView_UpcomingEvents.setLayoutManager(layoutManager);
        realm = Realm.getDefaultInstance();

        eventsDb = realm.where(MyEvent.class).findAll();

        eventsDb = eventsDb.sort("startDate", Sort.DESCENDING,
        "startTime.hour",Sort.ASCENDING );

        setRecyclerView_UpcomingEvents();
        setSpinnerShowingIntervals();


        setListeners();



    }

    private void setListeners() {
        setRecyclerView_UpcomingEventsListener();
        btn_mainMenu.setOnClickListener(this);
        showingInterval.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                LocalDate date = LocalDate.now();
                String year = String.valueOf(date.getYear());
                int monthVal = date.getMonth().getValue();
                String month;
                if(monthVal < 10)
                    month ="0" + String.valueOf(monthVal);
                else
                    month =String.valueOf(monthVal);

                if(position == 2) {
                    eventsDb = realm.where(MyEvent.class).contains("startDate", year + "-"+month).findAll();
                }
                if(position == 1){
                    eventsDb = realm.where(MyEvent.class).contains("startDate", year).findAll();
                }
                if(position == 0)
                    eventsDb = realm.where(MyEvent.class).findAll();
                setRecyclerView_UpcomingEvents();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setSpinnerShowingIntervals() {
        ArrayAdapter<CharSequence> adptr_intervals = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.intervals_array, android.R.layout.simple_spinner_item);
        adptr_intervals.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        showingInterval.setAdapter(adptr_intervals);

        showingInterval.setSelection(0);
    }


    private void setRecyclerView_UpcomingEventsListener() {
        recyclerView_UpcomingEvents.addOnItemTouchListener(
                new RecyclerViewItemClickListener(UpComingEventsList.this, recyclerView_UpcomingEvents ,new RecyclerViewItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String primaryKey = eventsDb.get(position).getpKey();
                        //Toast.makeText(UpComingEventsList.this, position + " a basıldı", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpComingEventsList.this, EditEventActivity.class);
                        intent.putExtra("primaryKey",primaryKey);
                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        //Toast.makeText(UpComingEventsList.this, position + " a uzun basıldı", Toast.LENGTH_SHORT).show();
                        showAlertDialog(position);
                    }
                })
        );
    }
    private void showAlertDialog(int position){
        AlertDialog.Builder options  = new AlertDialog.Builder(UpComingEventsList.this);
        options.setMessage("Etkinlik ayarları");
        options.setNegativeButton("Sil", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteEvent(position);
            }
        });
        options.setPositiveButton("Tamam", null);
        options.setNeutralButton("Paylaş", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String eventDetails = getEventDetails(position);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, eventDetails);
                Intent sendIntent = Intent.createChooser(intent, null);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(sendIntent);
                }
            }
        });
        options.show();
    }

    private String getEventDetails(int position) {
        String key = eventsDb.get(position).getpKey();
        MyEvent myEvent = realm.where(MyEvent.class).equalTo("pKey",key).findFirst();
        String details =myEvent.getStartDate() +" Tarihinde" + " Saat : "+myEvent.getStartTime()
                +myEvent.getEventName() + " var " ;
        return details;
    }

    private void setRecyclerView_UpcomingEvents(){
        myRecyclerViewAdapter = new MyRecyclerViewAdapter(getApplicationContext(),eventsDb);
        recyclerView_UpcomingEvents.setAdapter(myRecyclerViewAdapter);
    }

    private void deleteEvent(int position) {

        //find parent primary key for recurrence events
        String [] pkey = eventsDb.get(position).getpKey().split("_");
        String parentPkey = pkey[0];

        RealmResults<MyEvent> results = realm.where(MyEvent.class)
                .contains("pKey", parentPkey)
                .findAll();

        realm.executeTransaction(realm -> {
            // Delete all matches
            results.deleteAllFromRealm();
        });
        Toast.makeText(this, "Etkinlik Silindi", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_MainMenu:
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
