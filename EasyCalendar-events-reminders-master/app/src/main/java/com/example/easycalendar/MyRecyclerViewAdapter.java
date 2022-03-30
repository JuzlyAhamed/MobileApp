package com.example.easycalendar;

import android.content.Context;
import android.content.res.TypedArray;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class MyRecyclerViewAdapter extends RealmRecyclerViewAdapter<MyEvent, MyRecyclerViewAdapter.MyViewHolder> {

    Context context;



    MyRecyclerViewAdapter(Context context, OrderedRealmCollection<MyEvent> data) {
        super(data, true);
        this.context = context;
        // Only set this if the model class has a primary key that is also a integer or long.
        // In that case, {@code getItemId(int)} must also be overridden to return the key.
        // See https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#hasStableIds()
        // See https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html#getItemId(int)
        setHasStableIds(false);
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_card, parent, false);
        MyViewHolder holder = new MyViewHolder(itemView);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.eventName.setText(getItem(position).getEventName());

        TypedArray categories = context.getResources().obtainTypedArray(R.array.categoryOptions_array);
        String category  = categories.getString(getItem(position).getIndex_category());
        holder.eventCategory.setText(category);

        String date = getItem(position).getStartDate().toString();
        holder.startDate.setText(date);

        holder.startTime.setText(getItem(position).getStartTime().toString());
        holder.eventColor.setBackgroundColor(getItem(position).getColor());
        LocalDate date1 = MyEvent.StringToDate(getItem(position).getStartDate(),'-');

    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView eventCategory;
        TextView eventName;
        TextView startDate;
        TextView weekDay;
        TextView startTime;
        ImageView eventColor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event_card_eventName);
            eventCategory = itemView.findViewById( R.id.event_card_eventCategory );
            startDate = itemView.findViewById( R.id.event_card_startDate );
            startTime = itemView.findViewById( R.id.event_card_startTime );
            eventColor = itemView.findViewById(R.id.event_card_eventColor);

        }
    }




}
