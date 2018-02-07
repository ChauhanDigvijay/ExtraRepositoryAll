package com.donatos.phoenix.ui.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.donatos.phoenix.R;
import com.donatos.phoenix.ui.common.C2728a;
import com.donatos.phoenix.ui.common.C2750s;

public class NutritionLinkView extends LinearLayout {
    C2750s f8972a;

    public NutritionLinkView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        ((C2728a) context).mo1600e().mo1508a(this);
        setOrientation(1);
        ((TextView) inflate(getContext(), R.layout.view_model_menu_nutritionlink, this).findViewById(R.id.menu_nutrition_link_textview)).setOnClickListener(C2901v.m8501a(this));
    }
}
