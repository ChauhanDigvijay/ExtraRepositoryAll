package com.hbh.honeybaked.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.fishbowl.basicmodule.Analytics.FBEventSettings;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.fbsupportingfiles.FBAnalyticsManager;
import com.hbh.honeybaked.helper.PreferenceHelper;
import com.hbh.honeybaked.listener.DialogListener;
import com.hbh.honeybaked.module.OfferModule;
import com.hbh.honeybaked.supportingfiles.Utility;
import java.util.ArrayList;

public class LoyaltyAdapter extends BaseAdapter {
    Context context;
    DialogListener dialogListener;
    protected PreferenceHelper hbha_pref_helper;
    private LayoutInflater inflater = null;
    ArrayList<OfferModule> offerModuleArrayList;

    public class Holder {
        ImageView imageView;
        TextView point_Txt;
        TextView reedem_Txt;
        LinearLayout reedem_layout;
        TextView text_Point;
    }

    public LoyaltyAdapter(Context mainActivity, ArrayList<OfferModule> offerModuleArrayList, DialogListener dialogListener) {
        this.offerModuleArrayList = offerModuleArrayList;
        this.context = mainActivity;
        this.inflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
        this.dialogListener = dialogListener;
        this.hbha_pref_helper = new PreferenceHelper(this.context);
    }

    public int getCount() {
        return this.offerModuleArrayList.size();
    }

    public Object getItem(int position) {
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        View rowView = convertView;
        if (rowView == null) {
            holder = new Holder();
            rowView = this.inflater.inflate(R.layout.loyal_adapter, null);
            holder.point_Txt = (TextView) rowView.findViewById(R.id.point_txt);
            holder.text_Point = (TextView) rowView.findViewById(R.id.text_point);
            holder.reedem_Txt = (TextView) rowView.findViewById(R.id.reedem_txt);
            holder.reedem_layout = (LinearLayout) rowView.findViewById(R.id.reedem_layout);
            holder.reedem_Txt.setPaintFlags(holder.reedem_Txt.getPaintFlags() | 8);
            rowView.setTag(holder);
        } else {
            holder = (Holder) rowView.getTag();
        }
        final OfferModule offerModule = (OfferModule) this.offerModuleArrayList.get(position);
        holder.point_Txt.setText(offerModule.getPoint() + "\nPTS");
        holder.text_Point.setText(offerModule.getCampaignTitle());
        holder.reedem_layout.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                LoyaltyAdapter.this.shoDialog(offerModule);
            }
        });
        return rowView;
    }

    private void shoDialog(final OfferModule offerModule) {
        final Dialog dialog = new Dialog(this.context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.loyalty_alert);
        dialog.getWindow().setLayout(-2, -2);
        dialog.getWindow().setGravity(17);
        Button yes_butn = (Button) dialog.findViewById(R.id.yes_butn);
        Button no_butn = (Button) dialog.findViewById(R.id.no_butn);
        ((TextView) dialog.findViewById(R.id.forgot_head_text)).setText("Are you sure you want to redeem " + offerModule.getPoint() + " points for this offer?");
        yes_butn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                FBAnalyticsManager.sharedInstance().track_ItemWith(String.valueOf(offerModule.getCampaignId()), offerModule.getCampaignTitle(), FBEventSettings.REWARDS_REDEEMED);
                LoyaltyAdapter.this.dialogListener.performDialogAction(AppConstants.SHOPONLINE, offerModule);
                Utility.removeDialogFragment((FragmentActivity) LoyaltyAdapter.this.context, "LoyaltyRewardsDialog");
                dialog.dismiss();
            }
        });
        no_butn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
