package com.donatos.phoenix.ui.checkout.view;

import android.content.Context;
import android.support.v4.p015c.C0224a;
import android.support.v7.widget.AppCompatRadioButton;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import com.donatos.phoenix.R;
import com.donatos.phoenix.p134b.C2505i;
import com.donatos.phoenix.p134b.C2508l;
import com.donatos.phoenix.p134b.p135a.C2493b;
import com.donatos.phoenix.ui.checkout.p140b.C2626e;
import com.donatos.phoenix.ui.checkout.p140b.C2640s;
import com.donatos.phoenix.ui.checkout.p140b.C2641t;
import com.donatos.phoenix.ui.checkout.p140b.C2642u;
import com.donatos.phoenix.ui.common.C2728a;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import p027b.p041c.p046b.C1172b;

public class DriverTipView extends FrameLayout {
    C2493b f8468a;
    public AppCompatRadioButton f8469b;
    public AppCompatRadioButton f8470c;
    public AppCompatRadioButton f8471d;
    public AppCompatRadioButton f8472e;
    public Double f8473f = null;
    public Double f8474g;
    private TextView f8475h;
    private RadioGroup f8476i;
    private NumberFormat f8477j = NumberFormat.getCurrencyInstance();
    private int f8478k = -1;
    private List<C1172b> f8479l = new ArrayList();
    private Double f8480m;
    private OnCheckedChangeListener f8481n = C2710b.m8099a(this);
    private OnClickListener f8482o = C2711c.m8100a(this);

    public DriverTipView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ((C2728a) context).mo1600e().mo1498a(this);
        View inflate = inflate(getContext(), R.layout.view_model_drivertip, this);
        this.f8475h = (TextView) inflate.findViewById(R.id.drivertip_title);
        this.f8476i = (RadioGroup) inflate.findViewById(R.id.drivertip_group);
        this.f8469b = (AppCompatRadioButton) inflate.findViewById(R.id.drivertip_ten);
        this.f8470c = (AppCompatRadioButton) inflate.findViewById(R.id.drivertip_fifteen);
        this.f8471d = (AppCompatRadioButton) inflate.findViewById(R.id.drivertip_twenty);
        this.f8472e = (AppCompatRadioButton) inflate.findViewById(R.id.drivertip_other);
        this.f8476i.setOnCheckedChangeListener(this.f8481n);
        this.f8469b.setOnClickListener(this.f8482o);
        this.f8470c.setOnClickListener(this.f8482o);
        this.f8471d.setOnClickListener(this.f8482o);
        this.f8478k = this.f8469b.getId();
        this.f8472e.setOnClickListener(C2712d.m8101a(this));
    }

    static /* synthetic */ void m8086a(DriverTipView driverTipView) {
        driverTipView.f8468a.m7325a("Checkout - Tip");
        driverTipView.m8091a();
    }

    static /* synthetic */ void m8087a(DriverTipView driverTipView, View view) {
        driverTipView.f8478k = -1;
        C2508l.m7347a().m7349a(new C2642u((Double) view.getTag()));
    }

    static /* synthetic */ void m8088a(DriverTipView driverTipView, Object obj) throws Exception {
        if (obj instanceof C2626e) {
            driverTipView.f8480m = ((C2626e) obj).f8170a;
            driverTipView.m8091a();
            m8089a(driverTipView.f8480m);
        }
    }

    private static void m8089a(Double d) {
        C2508l.m7347a().m7349a(new C2640s(d));
    }

    static /* synthetic */ void m8090b(DriverTipView driverTipView, View view) {
        driverTipView.f8473f = null;
        if (view.getId() == driverTipView.f8478k) {
            driverTipView.f8476i.clearCheck();
            driverTipView.f8478k = -1;
            m8089a(Double.valueOf(0.0d));
            return;
        }
        driverTipView.f8478k = view.getId();
        switch (view.getId()) {
            case R.id.drivertip_ten:
                C2508l.m7347a().m7349a(new C2641t(Integer.valueOf(10)));
                return;
            case R.id.drivertip_fifteen:
                C2508l.m7347a().m7349a(new C2641t(Integer.valueOf(15)));
                return;
            case R.id.drivertip_twenty:
                C2508l.m7347a().m7349a(new C2641t(Integer.valueOf(20)));
                return;
            default:
                return;
        }
    }

    public final void m8091a() {
        if (this.f8474g != null) {
            if (this.f8473f != null) {
                if (C2505i.m7344a(this.f8474g.doubleValue() * 0.1d) == this.f8473f.doubleValue()) {
                    this.f8469b.setChecked(true);
                    this.f8478k = this.f8469b.getId();
                    this.f8473f = null;
                } else if (C2505i.m7344a(this.f8474g.doubleValue() * 0.15d) == this.f8473f.doubleValue()) {
                    this.f8470c.setChecked(true);
                    this.f8478k = this.f8470c.getId();
                    this.f8473f = null;
                } else if (C2505i.m7344a(this.f8474g.doubleValue() * 0.2d) == this.f8473f.doubleValue()) {
                    this.f8471d.setChecked(true);
                    this.f8478k = this.f8471d.getId();
                    this.f8473f = null;
                } else if (this.f8473f == null || this.f8473f.doubleValue() <= 0.0d) {
                    this.f8478k = -1;
                } else {
                    this.f8480m = new Double(this.f8473f.doubleValue());
                    this.f8472e.setChecked(true);
                    this.f8478k = this.f8472e.getId();
                    this.f8473f = null;
                }
            }
            int c = C0224a.m798c(getContext(), R.color.black);
            int c2 = C0224a.m798c(getContext(), R.color.gunsmoke);
            int c3 = C0224a.m798c(getContext(), R.color.white);
            int c4 = C0224a.m798c(getContext(), R.color.white);
            CharSequence spannableString = new SpannableString(getResources().getString(R.string.drivertip.ten, new Object[]{this.f8477j.format(this.f8474g.doubleValue() * 0.1d)}));
            CharSequence spannableString2 = new SpannableString(getResources().getString(R.string.drivertip.fifteen, new Object[]{this.f8477j.format(this.f8474g.doubleValue() * 0.15d)}));
            CharSequence spannableString3 = new SpannableString(getResources().getString(R.string.drivertip.twenty, new Object[]{this.f8477j.format(this.f8474g.doubleValue() * 0.2d)}));
            spannableString.setSpan(new ForegroundColorSpan(this.f8469b.isChecked() ? c3 : c), 0, 4, 0);
            spannableString2.setSpan(new ForegroundColorSpan(this.f8470c.isChecked() ? c3 : c), 0, 4, 0);
            spannableString3.setSpan(new ForegroundColorSpan(this.f8471d.isChecked() ? c3 : c), 0, 4, 0);
            spannableString.setSpan(new ForegroundColorSpan(this.f8469b.isChecked() ? c4 : c2), 4, spannableString.length(), 0);
            spannableString.setSpan(new RelativeSizeSpan(0.7273f), 4, spannableString.length(), 0);
            spannableString2.setSpan(new ForegroundColorSpan(this.f8470c.isChecked() ? c4 : c2), 4, spannableString2.length(), 0);
            spannableString2.setSpan(new RelativeSizeSpan(0.7273f), 4, spannableString.length(), 0);
            spannableString3.setSpan(new ForegroundColorSpan(this.f8471d.isChecked() ? c4 : c2), 4, spannableString3.length(), 0);
            spannableString3.setSpan(new RelativeSizeSpan(0.7273f), 4, spannableString.length(), 0);
            this.f8469b.setText(spannableString);
            this.f8470c.setText(spannableString2);
            this.f8471d.setText(spannableString3);
            this.f8469b.setTag(Double.valueOf(this.f8474g.doubleValue() * 0.1d));
            this.f8470c.setTag(Double.valueOf(this.f8474g.doubleValue() * 0.15d));
            this.f8471d.setTag(Double.valueOf(this.f8474g.doubleValue() * 0.2d));
            if (this.f8480m == null || this.f8480m.doubleValue() <= 0.0d) {
                this.f8472e.setText(getResources().getText(R.string.drivertip.other));
                AppCompatRadioButton appCompatRadioButton = this.f8472e;
                if (!this.f8472e.isChecked()) {
                    c3 = c;
                }
                appCompatRadioButton.setTextColor(c3);
                this.f8472e.setTag(null);
                return;
            }
            CharSequence spannableString4 = new SpannableString(getResources().getString(R.string.drivertip.otheramount, new Object[]{this.f8477j.format(this.f8480m)}));
            if (!this.f8472e.isChecked()) {
                c3 = c;
            }
            spannableString4.setSpan(new ForegroundColorSpan(c3), 0, 6, 0);
            if (!this.f8472e.isChecked()) {
                c4 = c2;
            }
            spannableString4.setSpan(new ForegroundColorSpan(c4), 6, spannableString4.length(), 0);
            spannableString4.setSpan(new RelativeSizeSpan(0.7273f), 6, spannableString4.length(), 0);
            this.f8472e.setText(spannableString4);
            this.f8472e.setTag(this.f8480m);
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f8479l.add(C2508l.m7347a().f7674a.subscribe(new C2713e(this)));
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (C1172b c1172b : this.f8479l) {
            C2508l.m7347a();
            C2508l.m7348a(c1172b);
        }
    }

    public void setTitle(int i) {
        this.f8475h.setText(i);
    }
}
