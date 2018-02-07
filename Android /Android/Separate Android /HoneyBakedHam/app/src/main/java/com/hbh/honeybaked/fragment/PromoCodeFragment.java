package com.hbh.honeybaked.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hbh.honeybaked.R;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.supportingfiles.Utility;

public class PromoCodeFragment extends BaseFragment {
    ImageView QR_promoCode;
    String describtion = "";
    TextView describtion_tv;
    String promoCode = "";
    TextView promocode_tv;
    String title = "";
    TextView title_tv;

    public static PromoCodeFragment newInstances(MenuModel menuModel) {
        PromoCodeFragment promoCodeFragment = new PromoCodeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MenuModel", menuModel);
        promoCodeFragment.setArguments(bundle);
        return promoCodeFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_promocode, container, false);
        if (getArguments() != null) {
            MenuModel menuModel = (MenuModel) getArguments().getSerializable("MenuModel");
            this.title = menuModel.getMenu_title();
            this.promoCode = menuModel.getMenu_link();
            this.describtion = menuModel.getMenu_img();
        }
        initView(v);
        return v;
    }

    private void initView(View v) {
        this.title_tv = (TextView) v.findViewById(R.id.title_tv);
        this.describtion_tv = (TextView) v.findViewById(R.id.describtion_tv);
        this.promocode_tv = (TextView) v.findViewById(R.id.promocode_tv);
        this.QR_promoCode = (ImageView) v.findViewById(R.id.QR_promoCode);
        setValues();
    }

    private void setValues() {
        if (Utility.isEmptyString(this.title)) {
            this.title_tv.setVisibility(View.VISIBLE);
        } else {
            this.title_tv.setText(this.title);
        }
        if (Utility.isEmptyString(this.describtion)) {
            this.describtion_tv.setVisibility(View.VISIBLE);
        } else {
            this.describtion_tv.setText(this.describtion);
        }
        if (Utility.isEmptyString(this.promoCode)) {
            this.promocode_tv.setVisibility(View.VISIBLE);
            return;
        }
        this.promocode_tv.setText("Promo Code :" + this.promoCode);
        setQrImage();
    }

    private void setQrImage() {
        if (Utility.isEmptyString(this.promoCode)) {
            this.QR_promoCode.setVisibility(View.VISIBLE);
            return;
        }
//        try {
//            LayoutParams layoutParams = this.QR_promoCode.getLayoutParams();
//            layoutParams.width = (int) (((double) this.screenWidth) * 0.35d);
//            layoutParams.height = (int) (((double) this.screenWidth) * 0.35d);
//            this.QR_promoCode.setLayoutParams(layoutParams);
//            this.QR_promoCode.setImageBitmap(new Builder().content(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_LOYALTY_NO)).qrSize(350).margin(1).color(Color.rgb(0, 0, 0)).bgColor(Color.rgb(255, 255, 255)).ecc(ErrorCorrectionLevel.M).overlay(null).overlaySize(0).overlayAlpha(255).overlayXfermode(null).footNote("").encode());
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
    }

    public void onClick(View v) {
    }
}
