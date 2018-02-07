package com.BasicApp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.GetAllRewardOfferPointBank;
import com.BasicApp.BusinessLogic.Models.RewardPointSummary;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Activites.NonGeneric.PointBank.PointBankRedeemedActivity;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.Member;
import com.fishbowl.basicmodule.Services.FBUserOfferService;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by schaudhary_ic on 09-Feb-17.
 */

public class PointBankAdapter extends BaseAdapter {
    List<GetAllRewardOfferPointBank> myList;
    LayoutInflater inflater;
    Context context;
    int mycustomPosition;
    public Member member = null;

    public PointBankAdapter(Context context, List<GetAllRewardOfferPointBank> myList, int mycustomPosition) {
        super();
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.mycustomPosition = mycustomPosition;
    }

    @Override
    public int getCount() {
        return this.myList.size();
    }

    @Override
    public Object getItem(int position) {

        return this.myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_row_pointbank, parent, false);
        }
        TextView title, button_point;
        Button button;
        View view;
        int newPosition;
        member= FBUserService.sharedInstance().member;
        final GetAllRewardOfferPointBank loyaltyitem = myList.get(position);
        view =(View) convertView.findViewById(R.id.view);
        title = (TextView) convertView.findViewById(R.id.title);
        button = (Button) convertView.findViewById(R.id.button);
        button_point = (TextView) convertView.findViewById(R.id.button_point);
        LinearLayout thumb = (LinearLayout) convertView.findViewById(R.id.thumb);
        int earnedPoints = RewardPointSummary.earnedPoints;

        thumb.setVisibility(View.GONE);
        if (StringUtilities.isValidString(loyaltyitem.getPublicname())) {
            title.setText(loyaltyitem.getPublicname());
        }
        button_point.setText(String.valueOf(loyaltyitem.getLoyaltyPoints()));
        if (position==mycustomPosition){
            thumb.setVisibility(View.VISIBLE);
        }
        else{
            thumb.setVisibility(View.GONE);
        }
        if (earnedPoints >= loyaltyitem.getLoyaltyPoints()) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
            // newPosition = loyaltyitem.getLoyaltyPoints();
            //  thumb.setVisibility(View.VISIBLE);
            button.setEnabled(true);
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.red_color));
        }
        else{
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
            //thumb.setVisibility(View.GONE);
            button.setEnabled(false);
            thumb.setVisibility(View.GONE);
            button.setBackgroundColor(ContextCompat.getColor(context, R.color.gray));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject object=new JSONObject();
                try {
                    object.put("memberId",member.customerID);
                    object.put("offerId",loyaltyitem.getOfferId());
                    object.put("tenantId",loyaltyitem.getTenantId());
                    object.put("claimPoints",loyaltyitem.getLoyaltyPoints());
                }catch (Exception e){

                }
                FBUserOfferService.sharedInstance().useOffer(object, new FBUserOfferService.FBUseOfferCallback() {
                    @Override
                    public void onUseOfferCallback(JSONObject response, Exception error) {
                        try{if (response != null && error == null) {
                            if (response.has("message")) {
                                String messageURL = response.getString("message");
                                Intent i = new Intent(context, PointBankRedeemedActivity.class);
                                i.putExtra("htmlbody",messageURL);
                                context.startActivity(i);
                            }


                        } else {
                            //  FBUtils.tryHandleTokenExpiry(context,error);
                            FBUtils.showAlert(context, String.valueOf(error));
                        }
                    } catch(JSONException e){}
                    }
                });
            }
        });
        return convertView;
    }

}
