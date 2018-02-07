package com.fishbowl.loyaltymodule.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fishbowl.loyaltymodule.R;


/**
 * Created by schaudhary_ic on 03-Oct-16.
 */

public class BottomToolbarActivity extends LinearLayout implements View.OnClickListener {
    private TextView storename, storeno, storelocationn,distance;

    LinearLayout car_layout;

    ImageButton call;
    Integer storeId;

    String strnum, strloc, strname;
    public static Boolean historyflag = false;
    String storelocation;

    Float storedistance;
    public BottomToolbarActivity(Context context) {
        super(context);
    }

    public BottomToolbarActivity(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomToolbarActivity(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initBottomToolbar() {
        inflateBottomToolbar();
    }

    private void inflateBottomToolbar() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_main, this);
        call.setOnClickListener(this);




    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        inflateBottomToolbar();
    }

    @Override
    public void onClick(View v) {

}}
