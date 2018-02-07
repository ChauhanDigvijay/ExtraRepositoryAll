package com.fishbowl.LoyaltyTabletApp.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer.PassSendActivity;
import com.fishbowl.LoyaltyTabletApp.Activites.NonGeneric.Offer.PushDetail_Activity;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferItem;
import com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models.OfferSummary;
import com.fishbowl.LoyaltyTabletApp.R;
import com.fishbowl.LoyaltyTabletApp.Utils.FBUtils;
import com.fishbowl.LoyaltyTabletApp.Utils.StringUtilities;

import java.util.ArrayList;
import java.util.Date;

public class OfferBaseAdapter extends BaseAdapter {
    public static ArrayList<OfferItem> offerList;
    Button buttonpromo, sendSMS;
    Activity ctx;
    View signedView;
    WebView webView;
    LayoutInflater inflater;
    ImageView im1;
    String mimeType = "text/html";
    String encoding = "utf-8";
    int diifer;
    private Toolbar toolbar;
    private ImageLoader mImageLoader;
    private NetworkImageView background;
    private ViewPager viewPager;

    public OfferBaseAdapter(Activity context, ArrayList<OfferItem> offerList) {
        this.ctx = context;
        inflater = LayoutInflater.from(ctx);
        this.offerList = offerList;
    }

    @Override
    public int getCount() {
        if (OfferSummary.offerList != null) {
            if (OfferSummary.offerList.size() != 0) {
                return OfferSummary.offerList.size();
            }
        }

        return 0;
    }

    @Override
    public OfferItem getItem(int position) {
        return offerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewGroup currentView = null;
        OfferItem item = null;
        item = OfferSummary.offerList.get(position);
        currentView = (ViewGroup) inflater.inflate(R.layout.activity_view_basic, parent, false);
        currentView = setViewwithchannelid(currentView, item, position);
        return currentView;
    }

    private View.OnClickListener onClickListener(final int position) {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(ctx, "Hello Javatpoint" + position, Toast.LENGTH_SHORT);
                toast.setMargin(50, 50);
                toast.show();
            }
        };
    }

    public ViewGroup setViewwithchannelid(ViewGroup currentView, final OfferItem item, final int position) {

        if (item.getChannelID() == 7) {
            currentView.removeAllViews();
            signedView = inflater.inflate(R.layout.activity_mobile_add, currentView, false);
            RelativeLayout rewardsView = (RelativeLayout) signedView.findViewById(R.id.rewardsLayer);
            webView = (WebView) rewardsView.findViewById(R.id.webview);
            String html = item.getHtmlBody();
            webView.loadData(html, mimeType, encoding);
            currentView.addView(signedView);
        } else if (item.getChannelID() == 1) {
            currentView.removeAllViews();
            signedView = inflater.inflate(R.layout.activity_email, currentView, false);
            RelativeLayout rewardsView = (RelativeLayout) signedView.findViewById(R.id.rewardsLayer);
            webView = (WebView) rewardsView.findViewById(R.id.webview);
            String html = item.getHtmlBody();
            webView.loadData(html, mimeType, encoding);
            currentView.addView(signedView);
        } else if (item.getChannelID() == 3) {
            String title = item.getOfferIItem();
            String desc = item.getOfferIName();
            if (StringUtilities.isValidString(item.getDatetime())) {
                Date intent = FBUtils.getDateFromString(item.getDatetime(), (String) null);
                if (intent == null) {
                    intent = FBUtils.getDateFromStringSlash(item.getDatetime(), null);
                }
                diifer = FBUtils.daysBetween(new Date(), intent);
            }
            currentView.removeAllViews();
            signedView = inflater.inflate(R.layout.activity_sms_reward, currentView, false);
            RelativeLayout rewardsView = (RelativeLayout) signedView.findViewById(R.id.rewardsLayer);
            TextView offertitle = (TextView) rewardsView.findViewById(R.id.offertitle);
            TextView offerdate = (TextView) rewardsView.findViewById(R.id.offerdate);
            TextView offer_location = (TextView) rewardsView.findViewById(R.id.offer_location_name);
            TextView offerdesc = (TextView) rewardsView.findViewById(R.id.offerdesc);

            if (StringUtilities.isValidString(title)) {
                offertitle.setText(title);
            }

            if (StringUtilities.isValidString(desc)) {
                offerdesc.setText(desc);
            } else {
                offerdesc.setVisibility(View.GONE);
            }
            if (diifer > 0) {
                offerdate.setText("Expires in" + " " + diifer + " " + "days");
            }
            currentView.addView(signedView);
        } else if (item.getChannelID() == 5) {
            currentView.removeAllViews();
            signedView = (ViewGroup) inflater.inflate(R.layout.activity__view_image, currentView, false);
            TextView title = (TextView) signedView.findViewById(R.id.nummber_one);
            TextView desc = (TextView) signedView.findViewById(R.id.nummber_four);
            TextView expiry = (TextView) signedView.findViewById(R.id.nummber_five);
            TextView offer = (TextView) signedView.findViewById(R.id.nummber_three);
            buttonpromo = (Button) signedView.findViewById(R.id.bt1);
            sendSMS = (Button) signedView.findViewById(R.id.bt2);
            NetworkImageView image = (NetworkImageView) signedView.findViewById(R.id.img_one);
            currentView.addView(signedView);
            title.setText(item.getOfferIItem());
            desc.setText(item.getOfferIName());

            if (StringUtilities.isValidString(item.getDatetime())) {
                Date intent = FBUtils.getDateFromString(item.getDatetime(), (String) null);
                if (intent == null) {
                    intent = FBUtils.getDateFromStringSlash(item.getDatetime(), null);
                }
                diifer = FBUtils.daysBetween(new Date(), intent);
            }
            expiry.setText("Expire Days" + " in " + diifer);
            offer.setText("$" + String.valueOf(item.getOfferIPrice()));
            final String url = "http://" + item.getPassCustomStripUrl();

            sendSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                    alertDialog.setTitle("Send via sms");
                    alertDialog.setMessage("Not available");
                    alertDialog.setIcon(R.drawable.logomain);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                }
            });

            buttonpromo.setOnClickListener(new View.OnClickListener() {
                public int diifer;

                @Override
                public void onClick(View v) {
                    {
                        OfferItem item = OfferSummary.offerList.get(position);
                        String offerId = item.getOfferId();
                        String url = item.getOfferIOther();
                        String title = item.getOfferIItem();
                        String desc = item.getOfferIName();
                        String html = item.getHtmlBody();
                        Boolean isPMOffer = Boolean.valueOf(item.isPMOffer());
                        int promotionId = item.getPmPromotionID();

                        if (StringUtilities.isValidString(item.getDatetime())) {
                            Date intent = FBUtils.getDateFromString(item.getDatetime(), (String) null);
                            if (intent == null) {
                                intent = FBUtils.getDateFromStringSlash(item.getDatetime(), null);
                            }
                            diifer = FBUtils.daysBetween(new Date(), intent);
                        }

                        Intent intent1 = new Intent(ctx, PushDetail_Activity.class);
                        Bundle extras = new Bundle();
                        extras.putString("Title", title);
                        extras.putInt("Expire", this.diifer);
                        extras.putInt("promotionId", promotionId);
                        extras.putString("offerId", offerId);
                        extras.putBoolean("isPMOffer", isPMOffer.booleanValue());
                        extras.putString("desc", desc);
                        intent1.putExtras(extras);
                        ctx.startActivity(intent1);
                    }
                }
            });


        } else if (item.getChannelID() == 6) {
            currentView.removeAllViews();
            signedView = (ViewGroup) inflater.inflate(R.layout.activity__view_image, currentView, false);
            TextView title = (TextView) signedView.findViewById(R.id.nummber_one);
            TextView desc = (TextView) signedView.findViewById(R.id.nummber_four);
            TextView expiry = (TextView) signedView.findViewById(R.id.nummber_five);
            TextView offer = (TextView) signedView.findViewById(R.id.nummber_three);
            buttonpromo = (Button) signedView.findViewById(R.id.bt1);
            sendSMS = (Button) signedView.findViewById(R.id.bt2);
            NetworkImageView image = (NetworkImageView) signedView.findViewById(R.id.img_one);
            currentView.addView(signedView);
            title.setText(item.getOfferIItem());
            desc.setText(item.getOfferIName());

            if (StringUtilities.isValidString(item.getDatetime())) {
                Date intent = FBUtils.getDateFromString(item.getDatetime(), (String) null);
                if (intent == null) {
                    intent = FBUtils.getDateFromStringSlash(item.getDatetime(), null);
                }
                diifer = FBUtils.daysBetween(new Date(), intent);
            }
            expiry.setText("Expire Days" + " in " + diifer);
            offer.setText("$" + String.valueOf(item.getOfferIPrice()));

            sendSMS.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(ctx).create();
                    alertDialog.setTitle("Send via sms");
                    alertDialog.setMessage("Not available");
                    alertDialog.setIcon(R.drawable.logomain);
                    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    alertDialog.show();
                }
            });

            buttonpromo.setOnClickListener(new View.OnClickListener() {
                public int diifer;
                String offerId = item.getOfferId();

                @Override
                public void onClick(View v) {
                    {
                        Intent intent1 = new Intent(ctx, PassSendActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("offerId", offerId);
                        intent1.putExtras(extras);
                        ctx.startActivity(intent1);
                    }
                }
            });
        }
        return currentView;
    }


}