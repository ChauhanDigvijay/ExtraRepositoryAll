package com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.CreateGFCard;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.Activites.Generic.GiftCardBaseActivity;
import com.olo.jambajuice.Fragments.GiftCardFragments.BottomsheetFragment;
import com.olo.jambajuice.R;

/*
    Created by Jeeva
 */

public class NewCardActivityGiftCard extends GiftCardBaseActivity implements View.OnClickListener {

    public static NewCardActivityGiftCard newCardActivity;
    BottomSheetDialogFragment bottomsheetFragment;
    private RelativeLayout addNewCardLayout, JCHeader;
    private RelativeLayout JCrootLayout;
    private int totalHeight, headerHeight, textViewContentArea;
    private ImageButton addcardButton;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);

        context = this;
        newCardActivity = this;
        isShowBasketIcon = false;

        setUpToolBar(true,true);
        setBackButton(false,false);

        JCrootLayout = (RelativeLayout) findViewById(R.id.JCrootLayout);
        addNewCardLayout = (RelativeLayout) findViewById(R.id.addNewCardLayout);
        JCHeader = (RelativeLayout) findViewById(R.id.JCHeader);


        addcardButton = (ImageButton) findViewById(R.id.addCardButton);
        addcardButton.setOnClickListener(this);
        addNewCardLayout.setOnClickListener(this);

        bottomsheetFragment = BottomsheetFragment.newInstance("Modal Bottom Sheet");


        initToolbar();
        resizeView();

    }

    private void initToolbar() {
        setTitle("Jamba Cards");
        toolbar.setBackgroundColor(ContextCompat.getColor(context, R.color.background_white));
        TextView title = (TextView) toolbar.findViewById(R.id.title);
        title.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray));
    }

    private void resizeView() {

        ViewTreeObserver observer = JCrootLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    JCrootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    JCrootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                totalHeight = JCrootLayout.getMeasuredHeight();

                headerHeight = JCHeader.getHeight();


                int contentArea = totalHeight - (headerHeight);
                textViewContentArea = (int) (contentArea * 0.5);

                RelativeLayout.LayoutParams textAreaParams = (RelativeLayout.LayoutParams) addNewCardLayout.getLayoutParams();
                textAreaParams.height = textViewContentArea;
                addNewCardLayout.setLayoutParams(textAreaParams);

                int paddingCommon = getResources().getDimensionPixelSize(R.dimen.new_card_padding);

                addNewCardLayout.setPadding(paddingCommon, paddingCommon, paddingCommon, paddingCommon);

            }

        });
    }

    private void createDialog() {
        bottomsheetFragment.show(getSupportFragmentManager(), bottomsheetFragment.getTag());
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addNewCardLayout:
                createDialog();
                break;
            case R.id.addCardButton:
                createDialog();
                break;
        }
    }
}
