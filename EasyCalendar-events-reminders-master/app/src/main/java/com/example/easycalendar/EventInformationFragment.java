package com.example.easycalendar;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnRangeSelectedListener;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventInformationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventInformationFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int eventColor= -16537100; //default color
    private Spinner spinner_notification;
    private Spinner spinner_recurrence;
    private Spinner spinner_category;
    private Spinner spinner_emailNotification;
    private Spinner spinner_ringtones;
    private ImageButton btn_setNotificationToNone;
    private ImageButton btn_setRecurrenceToNone;
    private ImageButton btn_setLocationToNone;
    private ImageButton  btn_showPalette;


    private TextView notiftv;
    private EditText edtTxt_email;
    private EditText edtTxt_notes;
    private EditText edtTxt_eventName;
    private CheckBox chckbox_email;
    private CheckBox checkbox_rememberEmail;
    private TextView tv_startTime;
    private TextView tv_startDate;
    private TextView tv_endTime;
    private TextView tv_endDate;
    private TextView tv_location;


    TimePickerDialog timePickerDialog;
    private AlertDialog.Builder builder_palette;
    private  View paletteView;
    private  View datePickerView;

    // TODO: Rename and change types of parameters
    private String chosenDate;

    public EventInformationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment EventInformationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EventInformationFragment newInstance(String param1) {
        EventInformationFragment fragment = new EventInformationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            chosenDate = getArguments().getString(ARG_PARAM1);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event_information, container, false);
    }

    private void setListeners() {
        btn_setNotificationToNone.setOnClickListener(this);
        btn_setRecurrenceToNone.setOnClickListener(this);
        btn_setLocationToNone.setOnClickListener(this);
        btn_showPalette.setOnClickListener(this);
        spinner_notification.setOnItemSelectedListener(this);
        spinner_emailNotification.setOnItemSelectedListener(this);
        spinner_recurrence.setOnItemSelectedListener(this);
        spinner_category.setOnItemSelectedListener(this);
        spinner_ringtones.setOnItemSelectedListener(this);
        tv_startTime.setOnClickListener(this);
        tv_startDate.setOnClickListener(this);
        tv_endTime.setOnClickListener(this);
        tv_endDate.setOnClickListener(this);
        tv_location.setOnClickListener(this);

        chckbox_email.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    edtTxt_email.setVisibility(View.VISIBLE);
                    edtTxt_email.setClickable(true);
                    checkbox_rememberEmail.setVisibility(View.VISIBLE);
                    if(spinner_emailNotification.getSelectedItemPosition() == 0)
                        spinner_emailNotification.setSelection(spinner_notification.getSelectedItemPosition());
                } else {
                    edtTxt_email.setClickable(false);
                    edtTxt_email.setVisibility(View.INVISIBLE);
                    checkbox_rememberEmail.setVisibility(View.INVISIBLE);
                    spinner_emailNotification.setSelection(0);

                }
            }
        });
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        initViews();
        setListeners();
    }


    private void showPaletteLayout() {
        LayoutInflater inflater = EventInformationFragment.this.getLayoutInflater();
        paletteView = inflater.inflate(R.layout.palette_layout,null);
        builder_palette = new AlertDialog.Builder(getActivity());
        builder_palette.setTitle("Renk Paleti");
        builder_palette.setMessage("Etkinlik Türüne Özel Renk Seçimi");
        builder_palette.setView(paletteView);
        builder_palette.setPositiveButton("Tamam", null);
        builder_palette.show();
        SpectrumPalette spectrumPalette = paletteView.findViewById(R.id.palette);
        spectrumPalette.setOnColorSelectedListener(new SpectrumPalette.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                //Toast.makeText(getActivity(), "CoLOR : " + color, Toast.LENGTH_SHORT).show();
                btn_showPalette.setBackgroundColor(color);
                eventColor = color;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_setNotificationToNone:
                spinner_notification.setSelection(0);
                break;
            case R.id.btn_setRecurranceToNone:
                spinner_recurrence.setSelection(0);
                break;
            case R.id.btn_setLocationToNone:
                tv_location.setText("Konum Ekle");
                break;
            case R.id.btn_showPalette:
                showPaletteLayout();
                break;
            case R.id.startTime:
                timePick("start");
                break;
            case R.id.endTime:
                timePick("end");
                break;
            case R.id.starDate:
                mydatepick();
                break;
            case R.id.tv_location:
                getLocation();
                break;
            default:
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result= data.getStringExtra("result");
                tv_location.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult

    private void getLocation() {
        Intent intent = new Intent(getActivity(),MapsActivityCurrentPlace.class);
        intent.putExtra("location",tv_location.getText().toString());
        startActivityForResult(intent,1);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner_notification:
                if(position != 0 ) {
                    btn_setNotificationToNone.setVisibility(View.VISIBLE);
                    chckbox_email.setClickable(true);
                    spinner_emailNotification.setVisibility(View.VISIBLE);
                    //chckbox_email.setTextColor(R.color.md_black_1000);
                }
                else {
                    btn_setNotificationToNone.setVisibility(View.INVISIBLE);
                    chckbox_email.setClickable(false);
                    spinner_emailNotification.setVisibility(View.INVISIBLE);
                    // chckbox_email.setTextColor(R.color.md_grey_500);
                }
                break;
            case R.id.spinner_recurrance:
                if(position != 0 )
                    btn_setRecurrenceToNone.setVisibility(View.VISIBLE);
                else
                    btn_setRecurrenceToNone.setVisibility(View.INVISIBLE);
                break;
            case R.id.spinner_emailNotification:
                if(position ==0 )
                    chckbox_email.setChecked(false);
                else
                    chckbox_email.setChecked(true);
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initViews(){



        spinner_notification = getActivity().findViewById(R.id.spinner_notification);
        spinner_recurrence = getActivity().findViewById(R.id.spinner_recurrance);
        spinner_category = getActivity().findViewById(R.id.spinner_category);
        spinner_ringtones = getActivity().findViewById(R.id.spinner_ringtones);
        spinner_emailNotification= getActivity().findViewById(R.id.spinner_emailNotification);
        btn_setNotificationToNone = getActivity().findViewById(R.id.btn_setNotificationToNone);
        btn_setRecurrenceToNone = getActivity().findViewById(R.id.btn_setRecurranceToNone);
        btn_setLocationToNone= getActivity().findViewById(R.id.btn_setLocationToNone);
        btn_showPalette =getActivity(). findViewById(R.id.btn_showPalette);
        chckbox_email =getActivity().findViewById(R.id.checkbo_email);
        checkbox_rememberEmail =getActivity().findViewById(R.id.checkbox_rememberEmail);
        edtTxt_email = getActivity().findViewById(R.id.edtTxt_email);
        edtTxt_notes = getActivity().findViewById(R.id.edtTxt_notes);
        edtTxt_eventName = getActivity().findViewById(R.id.edtTxt_eventName);
        tv_startTime = getActivity().findViewById(R.id.startTime);
        tv_startDate = getActivity().findViewById(R.id.starDate);
        tv_endTime = getActivity().findViewById(R.id.endTime);
        tv_endDate = getActivity().findViewById(R.id.endDate);
        tv_location = getActivity().findViewById(R.id.tv_location);


        ArrayAdapter<CharSequence> adptr_notification = ArrayAdapter.createFromResource(getActivity(),
                R.array.notificationOptions_array, android.R.layout.simple_spinner_item);
        adptr_notification.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_notification.setAdapter(adptr_notification);

        spinner_emailNotification.setAdapter(adptr_notification);

        ArrayAdapter<CharSequence> adptr_recurrance = ArrayAdapter.createFromResource(getActivity(),
                R.array.recurranceOptions_array, android.R.layout.simple_spinner_item);
        adptr_recurrance.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_recurrence.setAdapter(adptr_recurrance);

        ArrayAdapter<CharSequence> adptr_category = ArrayAdapter.createFromResource(getActivity(),
                R.array.categoryOptions_array, android.R.layout.simple_spinner_item);
        adptr_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_category.setAdapter(adptr_category);

        ArrayAdapter<CharSequence> adptr_ringtones = ArrayAdapter.createFromResource(getActivity(),
                R.array.ringtones_array, android.R.layout.simple_spinner_item);
        adptr_ringtones.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_ringtones.setAdapter(adptr_ringtones);

        // Starting positions of spinners
        getSharedPreferences();

        if (getArguments() != null) {
            tv_startDate.setText(chosenDate);
            tv_endDate.setText(chosenDate);
        }

    }

    public void timePick(String time_type){
        final Calendar takvim = Calendar.getInstance();
        int saat = takvim.get(Calendar.HOUR_OF_DAY);
        int dakika = takvim.get(Calendar.MINUTE);

        TimePickerDialog tpd = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // hourOfDay ve minute değerleri seçilen saat değerleridir.
                        Time selectedTime = new Time(hourOfDay,minute);
                        if(time_type.equals("start")) {  //StartTimePicker selected
                            tv_startTime.setText(selectedTime.toString());
                            tv_endTime.setText(selectedTime.toString());
                        }else { //EndTimePicker selected
                            Time startTime = new Time(tv_startTime.getText().toString(),":");
                            Boolean oneDayEvent = tv_startDate.getText().toString().equals(tv_endDate.getText().toString());
                            if( oneDayEvent && selectedTime.isBefore(startTime) )
                                Toast.makeText(getActivity(), "Hatalı bitiş zamanı", Toast.LENGTH_SHORT).show();
                            else
                                tv_endTime.setText(selectedTime.toString());
                        }

                    }
                }, saat, dakika, true);

        tpd.setButton(TimePickerDialog.BUTTON_POSITIVE, "Tamam", tpd);
        tpd.show();
    }

    public void mydatepick(){
        AlertDialog.Builder builder_date;
        LayoutInflater inflater = EventInformationFragment.this.getLayoutInflater();
        datePickerView = inflater.inflate(R.layout.datepick_layout,null);

        builder_date = new AlertDialog.Builder(getActivity());
        builder_date.setTitle(" Etkinlik Tarihi");
        builder_date.setMessage("Başlangıç ve bitiş tarihi");
        builder_date.setView(datePickerView);
        builder_date.setPositiveButton("Tamam", null);
        builder_date.show();
        MaterialCalendarView calendarView = datePickerView.findViewById(R.id.calendarView);

        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                tv_startDate.setText(date.getDate().toString());
                tv_endDate .setText( date.getDate().toString());
            }
        });

        calendarView.setOnRangeSelectedListener(new OnRangeSelectedListener() {
            @Override
            public void onRangeSelected(@NonNull MaterialCalendarView widget, @NonNull List<CalendarDay> dates) {
                CalendarDay startDay = dates.get(0);
                tv_startDate.setText( startDay.getDate().toString());
                startDay = dates.get(dates.size() - 1);
                tv_endDate.setText( startDay.getDate().toString());
            }
        });
    }





    public MyEvent getInputs(){

        String eventName = edtTxt_eventName.getText().toString();
        int eventCategory  = spinner_category.getSelectedItemPosition();
        //eventColor already setted

        // LocalDate startDate = MyEvent.StringToDate( tv_startDate.getText().toString(), '' );
        //LocalDate endDate = MyEvent.StringToDate( tv_endDate.getText().toString(), '/' );
        String startDate =tv_startDate.getText().toString();
        String endDate = tv_endDate.getText().toString();
        Time startTime = new Time(tv_startTime.getText().toString(),":");
        Time endTime = new Time(tv_endTime.getText().toString(),":");
        String eventLocation = tv_location.getText().toString();

        int notification = spinner_notification.getSelectedItemPosition();
        String notes = edtTxt_notes.getText().toString();
        int reccurance = spinner_recurrence.getSelectedItemPosition();
        int ringtone = spinner_ringtones.getSelectedItemPosition();

        MyEvent myEvent = new MyEvent(eventName, eventCategory,eventColor,startTime,endTime,
                startDate,endDate,notification,notes,reccurance,ringtone,eventLocation);
        return myEvent;

    }

    public void setInputs(MyEvent myEvent){


        spinner_notification.setSelection(myEvent.getIndex_notification());
        spinner_recurrence.setSelection(myEvent.getIndex_recurrence());
        spinner_category.setSelection(myEvent.getIndex_category());
        spinner_emailNotification.setSelection(myEvent.getEmail_notification());
        spinner_ringtones.setSelection(myEvent.getIndex_ringtone());

        btn_showPalette.setBackgroundColor(myEvent.getColor());
        edtTxt_email.setText(myEvent.getEmail());
        edtTxt_notes.setText(myEvent.getNotes());
        edtTxt_eventName.setText(myEvent.getEventName());
        tv_startTime.setText(myEvent.getStartTime().toString());
        tv_startDate.setText(myEvent.getStartDate());;
        tv_endTime.setText(myEvent.getEndTime().toString());;
        tv_endDate.setText(myEvent.getEndDate());
        tv_location.setText(myEvent.getEventLocation());
        eventColor = myEvent.getColor();
    }

    private void getSharedPreferences(){

        SharedPreferences pref = getActivity().
                getSharedPreferences("default", getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //Getting values from shared preferences
        int indexRecurrence = pref.getInt("recurrence",0);
        int indexTime = pref.getInt("time",0);
        int indexSound = pref.getInt("sound",0);


        /*Setting values*/
        spinner_notification.setSelection(indexTime);
        spinner_recurrence.setSelection(indexRecurrence);
        spinner_ringtones.setSelection(indexSound);
        spinner_category.setSelection(0);

    }

}
