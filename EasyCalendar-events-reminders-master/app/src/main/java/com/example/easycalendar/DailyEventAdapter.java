package com.example.easycalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DailyEventAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<MyEvent> events;

    public DailyEventAdapter(Context context, ArrayList<MyEvent> events) {
        this.context = context;
        this.events = events;
    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_event_card, parent,false);

        TextView time =  view.findViewById(R.id.daily_event_card_strtTime);
        ImageView color = view.findViewById(R.id.daily_event_card_color);
        TextView category = view.findViewById(R.id.daily_event_card_category);
        TextView eventName = view.findViewById(R.id.daily_event_card_eventName);

        time.setText(events.get(position).getStartTime().toString());
        color.setBackgroundColor(events.get(position).getColor());
        int index = events.get(position).getIndex_category();
        String categoryName = context.getResources().getStringArray(R.array.categoryOptions_array)[index];
        category.setText(categoryName);
        eventName.setText(events.get(position).getEventName());

        return view;
    }
}
