package com.donatos.phoenix.ui.customizeitem.view;

import android.content.Context;
import android.support.v4.p015c.C0224a;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.donatos.phoenix.R;

public class CarouselItemView extends CardView {
    public TextView f8812e;
    public ImageView f8813f;

    public CarouselItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setCardElevation(10.0f);
        setRadius(0.0f);
        setUseCompatPadding(true);
        View inflate = inflate(getContext(), R.layout.view_model_carouselitem, this);
        this.f8812e = (TextView) inflate.findViewById(R.id.carouselitem_title);
        this.f8813f = (ImageView) inflate.findViewById(R.id.carouselitem_image);
    }

    public void setViewSelected(boolean z) {
        if (z) {
            setCardBackgroundColor(C0224a.m798c(getContext(), R.color.donatosRed));
            this.f8812e.setTextColor(C0224a.m798c(getContext(), R.color.white));
            this.f8813f.setColorFilter(C0224a.m798c(getContext(), R.color.white));
            setCardElevation(1.0f);
            return;
        }
        setCardBackgroundColor(C0224a.m798c(getContext(), R.color.white));
        this.f8812e.setTextColor(C0224a.m798c(getContext(), R.color.donatosRed));
        this.f8813f.setColorFilter(C0224a.m798c(getContext(), R.color.donatosRed));
        setCardElevation(10.0f);
    }
}
