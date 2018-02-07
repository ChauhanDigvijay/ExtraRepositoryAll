package com.donatos.phoenix.ui.customizeitem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.donatos.phoenix.R;
import com.donatos.phoenix.network.common.MenuItem;
import com.facebook.stetho.websocket.CloseCodes;
import java.text.NumberFormat;

public class CustomizeItemFooterView extends FrameLayout {
    public TextView f8818a;
    public Button f8819b;
    public int f8820c;
    public int f8821d;
    public Double f8822e;
    public NumberFormat f8823f = NumberFormat.getCurrencyInstance();
    private StepperView f8824g;
    private ProgressBar f8825h;

    public CustomizeItemFooterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        View inflate = inflate(getContext(), R.layout.view_model_customizeitemfooter, this);
        this.f8824g = (StepperView) inflate.findViewById(R.id.customizeitemfooter_quantity_stepper);
        this.f8818a = (TextView) inflate.findViewById(R.id.customizeitemfooter_total);
        this.f8819b = (Button) inflate.findViewById(R.id.customizeitemfooter_login_button);
        this.f8825h = (ProgressBar) inflate.findViewById(R.id.customizeitemfooter_total_spinner);
    }

    public int getQuantity() {
        return this.f8824g.getValue();
    }

    public void setAddToOrderListener(OnClickListener onClickListener) {
        this.f8819b.setOnClickListener(onClickListener);
    }

    public void setData(MenuItem menuItem) {
        this.f8824g.setMin(1);
        this.f8824g.setMax(CloseCodes.NORMAL_CLOSURE);
        this.f8824g.setValue(menuItem != null ? menuItem.getUserQuantity().intValue() : 0);
    }

    public void setLoading(boolean z) {
        if (z) {
            this.f8818a.setVisibility(4);
            this.f8819b.setText(getResources().getString(this.f8821d));
            this.f8825h.setVisibility(0);
            return;
        }
        this.f8818a.setVisibility(0);
        if (this.f8822e != null) {
            this.f8819b.setText(getResources().getString(this.f8820c, new Object[]{this.f8823f.format(this.f8822e.doubleValue() * ((double) getQuantity()))}));
        }
        this.f8825h.setVisibility(8);
    }

    public void setValueChangeListener(OnClickListener onClickListener) {
        this.f8824g.setOnValueChangedListener(onClickListener);
    }
}
