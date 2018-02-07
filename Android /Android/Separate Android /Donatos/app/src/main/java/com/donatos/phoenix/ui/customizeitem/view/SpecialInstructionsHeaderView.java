package com.donatos.phoenix.ui.customizeitem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;
import com.donatos.phoenix.ui.common.ac;

public class SpecialInstructionsHeaderView extends LinearLayout {
    private TextView f8838a;

    public SpecialInstructionsHeaderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOrientation(0);
        View inflate = inflate(getContext(), R.layout.view_model_specialinstructionsheader, this);
        inflate.setPadding(0, ac.m8119a(15.0f, inflate.getContext()), 0, 0);
        this.f8838a = (TextView) inflate.findViewById(R.id.specialinstructionsheader_textview);
    }

    public void setExpanded(Boolean bool) {
        this.f8838a.setCompoundDrawablesWithIntrinsicBounds(0, 0, bool.booleanValue() ? R.drawable.ic_up : R.drawable.ic_down, 0);
    }

    public void setTitle(String str) {
        this.f8838a.setText(str);
    }
}
