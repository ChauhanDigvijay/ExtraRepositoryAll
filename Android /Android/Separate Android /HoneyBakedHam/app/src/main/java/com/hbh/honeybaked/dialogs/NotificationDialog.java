package com.hbh.honeybaked.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.balysv.materialripple.MaterialRippleLayout;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.base.BaseDialogFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.helper.PreferenceHelper;
import com.hbh.honeybaked.listener.DialogListener;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.supportingfiles.Utility;

public class NotificationDialog extends BaseDialogFragment {
    CardView card_view;
    DialogListener dialogListener;
    MaterialRippleLayout notification__ripple_show;
    Button notification_cancel;
    TextView notification_content;
    MaterialRippleLayout notification_ripple_cancel;
    Button notification_show;
    TextView notification_title;
    String sDialogContent;
    String sDialogType;
    String sUrl;

    public static NotificationDialog newInstance(String sDialogType, String sDialogContent, String sUrl, DialogListener dialogListener) {
        NotificationDialog notificationDialog = new NotificationDialog();
        notificationDialog.dialogListener = dialogListener;
        Bundle b = new Bundle();
        b.putSerializable("sDialogType", sDialogType);
        b.putSerializable("sDialogContent", sDialogContent);
        b.putSerializable("sUrl", sUrl);
        notificationDialog.setArguments(b);
        return notificationDialog;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.sDialogType = getArguments().getString("sDialogType");
            this.sDialogContent = getArguments().getString("sDialogContent");
            this.sUrl = getArguments().getString("sUrl");
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
        View view = getActivity().getLayoutInflater().inflate(R.layout.notification_dialog, container, false);
        Window window = getDialog().getWindow();
        LayoutParams wlp = window.getAttributes();
        int[] screenSizes = Utility.getWindowHeightWidth(getActivity());
        wlp.gravity = 17;
        wlp.width = (int) (((double) screenSizes[0]) * 0.9d);
        window.setAttributes(wlp);
        initDialogView(view);
        setListeners();
        setNotificationValues();
        return view;
    }

    private void setListeners() {
        this.notification_cancel.setOnClickListener(this);
        this.notification_show.setOnClickListener(this);
    }

    private void setNotificationValues() {
        this.notification_content.setText(this.sDialogContent);
        if (!Utility.isEmptyString(this.sUrl) || this.sDialogType.equalsIgnoreCase("pb")) {
            this.notification__ripple_show.setVisibility(View.GONE);
            this.notification_show.setText("SHOW");
            return;
        }
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.notification_ripple_cancel.getLayoutParams();
        params.rightMargin = (int) getActivity().getResources().getDimension(R.dimen.notification_top_margin);
        params.leftMargin = (int) getActivity().getResources().getDimension(R.dimen.notification_top_margin);
        this.notification_ripple_cancel.setLayoutParams(params);
        this.notification__ripple_show.setVisibility(View.GONE);
        this.notification_cancel.setText("OK");
    }

    private void initDialogView(View view) {
        this.notification_ripple_cancel = (MaterialRippleLayout) view.findViewById(R.id.notification_ripple_cancel);
        this.notification__ripple_show = (MaterialRippleLayout) view.findViewById(R.id.notification__ripple_show);
        this.notification_cancel = (Button) view.findViewById(R.id.notification_cancel);
        this.notification_show = (Button) view.findViewById(R.id.notification_show);
        this.notification_title = (TextView) view.findViewById(R.id.notification_title);
        this.notification_content = (TextView) view.findViewById(R.id.notification_content);
       // this.card_view = (CardView) view.findViewById(R.id.card_view);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.card_view.getLayoutParams();
        params.width = (int) (((double) Utility.getWindowHeightWidth(getActivity())[0]) * 0.8d);
        this.card_view.setLayoutParams(params);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.notification_cancel:
                dismiss();
                return;
            case R.id.notification_show:
                dismiss();
                if (this.sDialogType.equalsIgnoreCase("pb")) {
                    this.dialogListener.performDialogAction(AppConstants.LOYALTY_PAGE, new MenuModel("My Loyalty", "", ""));
                    return;
                } else if (this.sDialogType.equalsIgnoreCase("re") && !Utility.isEmptyString(this.sUrl)) {
                    this.dialogListener.performDialogAction(AppConstants.SHOPONLINE, new MenuModel("Offers & Rewards", "", this.sUrl));
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }
}
