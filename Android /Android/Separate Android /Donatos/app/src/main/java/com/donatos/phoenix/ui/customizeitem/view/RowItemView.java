package com.donatos.phoenix.ui.customizeitem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;
import com.donatos.phoenix.network.common.MenuItemBase;
import com.donatos.phoenix.network.common.MenuItemType;
import com.donatos.phoenix.network.common.MenuTopping;
import com.donatos.phoenix.network.locations.MenuInstruction;
import com.donatos.phoenix.p134b.C2508l;
import com.donatos.phoenix.p134b.C2510n;
import com.donatos.phoenix.ui.common.C2728a;
import com.donatos.phoenix.ui.common.C2750s;
import com.donatos.phoenix.ui.common.ab;
import com.donatos.phoenix.ui.customizeitem.C2819r;
import com.donatos.phoenix.ui.customizeitem.C2820s;
import com.donatos.phoenix.ui.customizeitem.C2821t;
import com.donatos.phoenix.ui.customizeitem.C2822u;
import java.util.ArrayList;
import java.util.List;
import p027b.p041c.p046b.C1172b;

public class RowItemView extends FrameLayout {
    private static final Integer f8826l = Integer.valueOf(2);
    C2750s f8827a;
    private TextView f8828b;
    private TextView f8829c;
    private TextView f8830d;
    private CheckBox f8831e;
    private StepperView f8832f;
    private TextView f8833g;
    private LinearLayout f8834h;
    private MenuItemBase f8835i;
    private boolean f8836j;
    private List<C1172b> f8837k = new ArrayList();

    public RowItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ((C2728a) context).mo1600e().mo1504a(this);
        View inflate = inflate(getContext(), R.layout.view_model_rowitem, this);
        this.f8828b = (TextView) inflate.findViewById(R.id.rowitem_title);
        this.f8829c = (TextView) inflate.findViewById(R.id.rowitem_subtitle);
        this.f8830d = (TextView) inflate.findViewById(R.id.rowitem_description);
        this.f8831e = (CheckBox) inflate.findViewById(R.id.rowitem_checkbox);
        this.f8832f = (StepperView) inflate.findViewById(R.id.rowitem_stepper);
        this.f8833g = (TextView) inflate.findViewById(R.id.rowitem_edit);
        this.f8834h = (LinearLayout) inflate.findViewById(R.id.rowitem_textlayout);
    }

    static /* synthetic */ void m8349a(RowItemView rowItemView, Object obj) throws Exception {
        if (obj instanceof C2821t) {
            if (((C2821t) obj).f8800e == rowItemView.f8835i.getId().intValue() && !(rowItemView.f8835i instanceof MenuInstruction) && rowItemView.f8835i.getSelectedToppingCount().intValue() > rowItemView.f8835i.getMax().intValue()) {
                rowItemView.f8835i.setUserSelected(Boolean.valueOf(false));
            }
            rowItemView.m8353a(rowItemView.f8835i, rowItemView.f8836j);
        }
    }

    private boolean m8350a() {
        return (this.f8835i == null || this.f8835i.getType() == null || this.f8835i.getType().equals(MenuItemType.PIZZA) || this.f8835i.isTopping() == null || !this.f8835i.isTopping().booleanValue()) ? false : true;
    }

    static /* synthetic */ void m8351b(RowItemView rowItemView, MenuItemBase menuItemBase) {
        menuItemBase.setUserQuantity(Integer.valueOf(rowItemView.f8832f.getValue()));
        C2508l.m7347a().m7349a(new C2820s(menuItemBase.getId().intValue(), menuItemBase instanceof MenuInstruction));
    }

    static /* synthetic */ void m8352b(RowItemView rowItemView, Object obj) throws Exception {
        if ((obj instanceof C2819r) && ((C2819r) obj).f8791a.equals(rowItemView.f8835i.getId())) {
            rowItemView.f8835i.setToppingAmount(((C2819r) obj).f8792b);
            rowItemView.f8835i.setToppingCoverage(((C2819r) obj).f8793c);
            C2508l.m7347a().m7349a(new C2820s(rowItemView.f8835i.getId().intValue(), rowItemView.f8835i instanceof MenuInstruction));
            if ((rowItemView.f8835i instanceof MenuItemBase) && rowItemView.f8835i.getSelectedToppingCount().intValue() > rowItemView.f8835i.getMax().intValue()) {
                rowItemView.f8835i.setToppingAmount(C2822u.REGULAR);
            }
            rowItemView.m8353a(rowItemView.f8835i, rowItemView.f8836j);
        }
    }

    private void setViewSelected(boolean z) {
        if (z) {
            this.f8831e.setChecked(true);
            if ((this.f8835i instanceof MenuTopping) && !this.f8835i.getType().equals(MenuItemType.PIZZA)) {
                this.f8833g.setVisibility(8);
                this.f8832f.setVisibility(0);
                this.f8830d.setVisibility(8);
            } else if ((this.f8835i instanceof MenuTopping) && this.f8835i.getType().equals(MenuItemType.PIZZA)) {
                this.f8833g.setVisibility(0);
                this.f8832f.setVisibility(8);
                this.f8830d.setVisibility(0);
            } else {
                this.f8833g.setVisibility(8);
                this.f8832f.setVisibility(8);
                this.f8830d.setVisibility(8);
            }
        } else {
            this.f8831e.setChecked(false);
            this.f8833g.setVisibility(8);
            this.f8832f.setVisibility(8);
            this.f8829c.setVisibility(8);
            this.f8830d.setVisibility(8);
            this.f8835i.setUserQuantity(Integer.valueOf(0));
        }
        if (this.f8835i.getType() == null) {
            this.f8829c.setVisibility(8);
        } else if ((!(this.f8835i instanceof MenuTopping) || !this.f8835i.getType().equals(MenuItemType.PIZZA) || !this.f8836j) && !this.f8835i.getType().equals(MenuItemType.SALAD)) {
            this.f8829c.setVisibility(8);
        } else if (C2510n.m7367a(this.f8829c.getText())) {
            this.f8829c.setVisibility(8);
        } else {
            this.f8829c.setVisibility(0);
        }
    }

    public final void m8353a(MenuItemBase menuItemBase, boolean z) {
        this.f8835i = menuItemBase;
        this.f8836j = z;
        this.f8828b.setText(ab.m8118a(menuItemBase.getName()));
        this.f8829c.setText(this.f8835i.getCalories());
        this.f8830d.setText(getResources().getString(R.string.customize.row.subtitle, new Object[]{menuItemBase.getToppingAmountString(), menuItemBase.getToppingCoverageString()}));
        setViewSelected(menuItemBase.isUserSelected().booleanValue());
        this.f8832f.setTimes(m8350a());
        this.f8832f.setValue(menuItemBase.getUserQuantity().intValue());
        this.f8832f.setMin(1);
        int intValue = this.f8835i.getSelectedToppingCount() != null ? this.f8835i.getMax().intValue() - (this.f8835i.getSelectedToppingCount().intValue() - this.f8832f.getValue()) : this.f8835i.getMax().intValue() - this.f8832f.getValue();
        StepperView stepperView = this.f8832f;
        if (m8350a()) {
            intValue = Math.min(intValue, f8826l.intValue());
        }
        stepperView.setMax(intValue);
        this.f8832f.setOnValueChangedListener(C2824a.m8357a(this, menuItemBase));
        this.f8833g.setOnClickListener(C2825b.m8358a(this, menuItemBase));
    }

    public MenuItemBase getItem() {
        return this.f8835i;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.f8837k.add(C2508l.m7347a().f7674a.subscribe(new C2826c(this)));
        this.f8837k.add(C2508l.m7347a().f7674a.subscribe(new C2827d(this)));
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        for (C1172b c1172b : this.f8837k) {
            C2508l.m7347a();
            C2508l.m7348a(c1172b);
        }
    }

    public void setCheckListener(OnClickListener onClickListener) {
        this.f8834h.setOnClickListener(onClickListener);
        this.f8831e.setOnClickListener(onClickListener);
    }
}
