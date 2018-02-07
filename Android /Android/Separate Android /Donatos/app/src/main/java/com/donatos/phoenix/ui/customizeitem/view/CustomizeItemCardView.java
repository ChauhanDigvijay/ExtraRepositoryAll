package com.donatos.phoenix.ui.customizeitem.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.donatos.phoenix.R;
import com.donatos.phoenix.network.common.MenuItem;

import java.util.HashMap;

public class CustomizeItemCardView extends LinearLayout {
    private ImageView f8814a;
    private TextView f8815b;
    private TextView f8816c;
    private TextView f8817d;

    public CustomizeItemCardView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        // setOrientation(1);
        View inflate = inflate(getContext(), R.layout.view_model_itemcard, this);
        this.f8814a = (ImageView) inflate.findViewById(R.id.itemcard_image);
        this.f8815b = (TextView) inflate.findViewById(R.id.itemcard_title);
        this.f8816c = (TextView) inflate.findViewById(R.id.itemcard_subtitle);
        this.f8817d = (TextView) inflate.findViewById(R.id.itemcard_description);
    }

    public final void m8347a(MenuItem menuItem, HashMap<String, String> hashMap) {
        // C2170e.m6597b(this.f8814a.getContext()).m6725a(menuItem.getImage() != null ? "https://files.donatos.com/" + menuItem.getImage().getFilepath() : C2510n.m7365a(this.f8815b.getContext(), R.drawable.placeholder_horiz).toString()).m6484a((int) R.drawable.placeholder_horiz).m6485a(C2285b.ALL).m6495b().mo1356a(this.f8814a);
        this.f8815b.setText(menuItem.getName());
        this.f8816c.setText(menuItem.getDescription());
        CharSequence charSequence = null;
        CharSequence calorieKeyString = menuItem.getCalorieKeyString();
//        if (!(hashMap == null || C2510n.m7367a(calorieKeyString) || !hashMap.containsKey(calorieKeyString))) {
//            charSequence = (String) hashMap.get(calorieKeyString);
//        }
        this.f8817d.setText(charSequence);
    }
}
