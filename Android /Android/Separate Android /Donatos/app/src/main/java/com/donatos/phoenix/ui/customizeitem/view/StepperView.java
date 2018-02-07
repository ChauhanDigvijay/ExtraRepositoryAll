package com.donatos.phoenix.ui.customizeitem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.donatos.phoenix.R;

public class StepperView extends FrameLayout {
    private ImageButton f8839a;
    private TextView f8840b;
    private ImageButton f8841c;
    private int f8842d;
    private int f8843e;
    private int f8844f;
    private boolean f8845g;
    private OnClickListener f8846h;
    private OnClickListener f8847i = C2829f.m8361a(this);
    private OnClickListener f8848j = C2830g.m8362a(this);

    public StepperView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        View inflate = inflate(getContext(), R.layout.view_stepper, this);
        this.f8839a = (ImageButton) inflate.findViewById(R.id.stepper_minus);
        this.f8840b = (TextView) inflate.findViewById(R.id.stepper_value);
        this.f8841c = (ImageButton) inflate.findViewById(R.id.stepper_plus);
        this.f8839a.setOnClickListener(this.f8848j);
        this.f8841c.setOnClickListener(this.f8847i);
        m8354a();
    }

    private void m8354a() {
        boolean z = true;
        this.f8839a.setEnabled(this.f8844f > this.f8842d);
        ImageButton imageButton = this.f8841c;
        if (this.f8844f >= this.f8843e) {
            z = false;
        }
        imageButton.setEnabled(z);
    }

    static /* synthetic */ void m8355a(StepperView stepperView, View view) {
        stepperView.setValue(stepperView.f8844f - 1);
        if (stepperView.f8846h != null) {
            stepperView.f8846h.onClick(view);
        }
    }

    static /* synthetic */ void m8356b(StepperView stepperView, View view) {
        stepperView.setValue(stepperView.f8844f + 1);
        if (stepperView.f8846h != null) {
            stepperView.f8846h.onClick(view);
        }
    }

    public int getValue() {
        return this.f8844f;
    }

    public void setMax(int i) {
        this.f8843e = i;
        m8354a();
    }

    public void setMin(int i) {
        this.f8842d = i;
        m8354a();
    }

    public void setOnValueChangedListener(OnClickListener onClickListener) {
        this.f8846h = onClickListener;
    }

    public void setTimes(boolean z) {
        this.f8845g = z;
    }

    public void setValue(int i) {
        CharSequence string;
        this.f8844f = i;
        TextView textView = this.f8840b;
        if (this.f8845g) {
            string = getContext().getString(R.string.customize.stepper.times, new Object[]{Integer.valueOf(i)});
        } else {
            string = String.format(Integer.toString(i), new Object[0]);
        }
        textView.setText(string);
        m8354a();
    }
}
