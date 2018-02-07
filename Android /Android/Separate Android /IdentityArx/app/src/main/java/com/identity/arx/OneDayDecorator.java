package com.identity.arx;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.style.ForegroundColorSpan;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import java.util.Collection;
import java.util.HashSet;

public class OneDayDecorator implements DayViewDecorator {
    Context context;
    private HashSet<CalendarDay> dates;
    private Drawable drawable;

    public OneDayDecorator(Context context, int resId, Collection<CalendarDay> dates) {
        this.drawable = ContextCompat.getDrawable(context, resId);
        this.dates = new HashSet(dates);
        this.context = context;
    }

    public boolean shouldDecorate(CalendarDay day) {
        return this.dates.contains(day);
    }

    public void decorate(DayViewFacade view) {
        view.addSpan(new ForegroundColorSpan(this.context.getResources().getColor(R.color.textview_display_absent)));
        view.setSelectionDrawable(this.drawable);
    }
}
