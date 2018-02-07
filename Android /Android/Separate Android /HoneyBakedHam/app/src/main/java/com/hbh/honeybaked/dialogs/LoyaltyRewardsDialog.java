package com.hbh.honeybaked.dialogs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.adapter.LoyaltyAdapter;
import com.hbh.honeybaked.base.BaseDialogFragment;
import com.hbh.honeybaked.constants.PreferenceConstants;
import com.hbh.honeybaked.helper.PreferenceHelper;
import com.hbh.honeybaked.listener.DialogListener;
import com.hbh.honeybaked.module.OfferModule;
import com.hbh.honeybaked.supportingfiles.Utility;

import java.util.ArrayList;

public class LoyaltyRewardsDialog extends BaseDialogFragment {
    Bitmap bitmapimg;
    DialogListener dialogListener;
    ArrayList<OfferModule> offerModuleArrayList;
    private ImageView qr_imageView;

    class C17221 implements OnClickListener {
        C17221() {
        }

        public void onClick(View v) {
            LoyaltyRewardsDialog.this.dismiss();
        }
    }

    class C17232 implements ImageListener {
        C17232() {
        }

        public void onResponse(ImageContainer imageContainer, boolean b) {
            Bitmap rBitamp = imageContainer.getBitmap();
            if (rBitamp != null) {
                LoyaltyRewardsDialog.this.qr_imageView.setImageBitmap(rBitamp);
                LoyaltyRewardsDialog.this.qr_imageView.setVisibility(View.GONE);
                LoyaltyRewardsDialog.this.qr_imageView.invalidate();
                if (rBitamp.isRecycled()) {
                    rBitamp.recycle();
                }
            }
        }

        public void onErrorResponse(VolleyError volleyError) {
            LoyaltyRewardsDialog.this.qr_imageView.setVisibility(View.GONE);
        }
    }

    public static LoyaltyRewardsDialog newInstance(ArrayList<OfferModule> offerModuleArrayList, DialogListener dialogListener, Bitmap bitmap) {
        LoyaltyRewardsDialog progressDialogFragment = new LoyaltyRewardsDialog();
        progressDialogFragment.dialogListener = dialogListener;
        Bundle b = new Bundle();
        b.putSerializable("offerModuleArrayList", offerModuleArrayList);
        b.putParcelable("bitmapimg", bitmap);
        progressDialogFragment.setArguments(b);
        return progressDialogFragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.offerModuleArrayList = (ArrayList) getArguments().getSerializable("offerModuleArrayList");
            this.bitmapimg = (Bitmap) getArguments().getParcelable("bitmapimg");
        }
        this.hbha_pref_helper = new PreferenceHelper(getActivity());
        setRetainInstance(true);

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(1);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(false);
        getDialog().getWindow().setSoftInputMode(3);
        View view = getActivity().getLayoutInflater().inflate(R.layout.loyalty_rewards_popup, container, false);
        Window window = getDialog().getWindow();
        LayoutParams wlp = window.getAttributes();
        int[] screenSizes = Utility.getWindowHeightWidth(getActivity());
        wlp.gravity = 48;
        LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.y = (int) (((double) screenSizes[1]) * 0.2d);
        getDialog().getWindow().setAttributes(layoutParams);
        wlp.flags &= -3;
        window.setAttributes(wlp);
        ListView listView = (ListView) view.findViewById(R.id.rewards_list);
        LinearLayout layout = (LinearLayout) view.findViewById(R.id.reward_list_lay);
        ImageView closeImageview = (ImageView) view.findViewById(R.id.alert_close);
        TextView Avail_point = (TextView) view.findViewById(R.id.available_point);
        TextView user_ph_no = (TextView) view.findViewById(R.id.user_ph_no);
        this.qr_imageView = (ImageView) view.findViewById(R.id.qr_imageView);
        if (this.offerModuleArrayList.size() == 0) {
            ((LinearLayout.LayoutParams) layout.getLayoutParams()).height = 10;
        }
        setQrImage();
        user_ph_no.setText(Utility.convertToUsFormat(this.hbha_pref_helper.getStringValue("current_reg_ph_no")));
        Avail_point.setText(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_EARN_POINTS) + " POINTS AVAILABLE");
        listView.setAdapter(new LoyaltyAdapter(getActivity(), this.offerModuleArrayList, this.dialogListener));
        closeImageview.setOnClickListener(new C17221());
        return view;
    }

    private void setQrImage() {
        if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_LOYALTY_NO))) {
            this.qr_imageView.setVisibility(View.GONE);
            return;
        }
        try {
            ViewGroup.LayoutParams layoutParams = this.qr_imageView.getLayoutParams();
            layoutParams.width = (int) (((double) this.width) * 0.35d);
            layoutParams.height = (int) (((double) this.width) * 0.35d);
            this.qr_imageView.setLayoutParams(layoutParams);
           // Utility.getImageLoader(getActivity()).get(String.format(ApiConstants.QRCODE_URL, new Object[]{this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_LOYALTY_NO), Integer.valueOf(400)}), new C17232());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
