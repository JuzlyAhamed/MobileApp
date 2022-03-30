package com.example.easycalendar;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.HashSet;



public class EventDecorator implements DayViewDecorator {

    private final int color;
    private final  HashSet<CalendarDay> dates;
    Context context;

    public EventDecorator(int color, HashSet<CalendarDay> dates, Context context) {
        this.color = color;
        this.dates = dates;
        this.context = context;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);

    }

    @Override
    public void decorate(DayViewFacade view) {
        //view.addSpan(new DotSpan(20, color));
        if(color == 1){
            ShapeDrawable circle = new ShapeDrawable( new RectShape());
            circle.setAlpha(30);
            view.setBackgroundDrawable(circle);

          //  view.setBackgroundDrawable(context.getDrawable(R.drawable.circle3));
            //view.setBackgroundDrawable(context.getDrawable(R.drawable.circle2));
        }else {

           // int [] colors = context.getResources().getIntArray(R.array.colors2);

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
           // shape.setColor(color);
            shape.setStroke(6,color,6,4);
            shape.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
            //shape.setColor(color);
            //shape.setStroke(5, color);/*
           // GradientDrawable [] drawables = new GradientDrawable[1];
            //drawables[0] = shape;
/*
            GradientDrawable shape2 = new GradientDrawable();
            shape2.setShape(GradientDrawable.RADIAL_GRADIENT);
            shape2.setGradientRadius(new Float(45));
            shape2.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
            //shape.setColor();
            shape2.setStroke(5, context.getColor(R.color.colorAccent));
            drawables[1] = shape2;*/

           // LayerDrawable a = new LayerDrawable(drawables);
            //a.addLayer(shape);
            //view.setBackgroundDrawable(context.getDrawable(R.drawable.circle));

            view.setBackgroundDrawable(shape);
        }
    }
}