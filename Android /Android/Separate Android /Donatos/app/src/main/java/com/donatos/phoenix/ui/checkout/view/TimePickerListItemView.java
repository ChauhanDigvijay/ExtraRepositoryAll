package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.support.v4.p015c.C0224a;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimePickerListItemView extends LinearLayout {
    private TextView f8508a = ((TextView) inflate(getContext(), R.layout.view_model_timepickerlistitem, this).findViewById(R.id.timepickerlistitem_date));
    private Date f8509b;
    private boolean f8510c = false;

    public TimePickerListItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void m8097a() {
        if (this.f8510c) {
            this.f8508a.setTextSize(2, 18.0f);
            this.f8508a.setTextColor(C0224a.m798c(getContext(), R.color.donatosRed));
            this.f8508a.setTypeface(null, 1);
            return;
        }
        this.f8508a.setTextSize(2, 14.0f);
        this.f8508a.setTextColor(C0224a.m798c(getContext(), R.color.black));
        this.f8508a.setTypeface(null, 0);
    }

    public Date getDate() {
        return this.f8509b;
    }

    public boolean getSelected() {
        return this.f8510c;
    }

    public void setData(Date date) {
        this.f8509b = date;
        if (this.f8509b == null) {
            this.f8508a.setText(getContext().getString(R.string.checkout.when.assoonaspossible));
        } else {
            this.f8508a.setText(new SimpleDateFormat("h:mm a", Locale.US).format(date).toLowerCase());
        }
        m8097a();
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        layoutParams.gravity = 1;
        this.f8508a.setLayoutParams(layoutParams);
        setLayoutParams(layoutParams);
    }

    public void setSelected(boolean z) {
        this.f8510c = z;
        m8097a();
    }
}
