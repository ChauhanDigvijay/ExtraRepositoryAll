package com.olo.jambajuice.Activites.NonGeneric.OrderHistory;

import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.olo.jambajuice.BusinessLogic.Models.BasketChoice;
import com.olo.jambajuice.BusinessLogic.Models.RecentOrderDetails;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.Utils;

/**
 * Created by Ihsanulhaq on 6/18/2015.
 */
public class OrderDetailViewHolder implements View.OnClickListener {

    private final TextView title;
    private final TextView detail;
    private final TextView specialInstrctions;
    private final TextView total;
    private final TextView items;
    private TextView seeMore;
    private TextView seeLess;
    private TextView orderStatus;
    private RelativeLayout content;
    private View line;
    private String size;

    public OrderDetailViewHolder(View convertView) {
        title = (TextView) convertView.findViewById(R.id.tv_title);
        detail = (TextView) convertView.findViewById(R.id.tv_detail);
        total = (TextView) convertView.findViewById(R.id.tv_amount);
        specialInstrctions = (TextView) convertView.findViewById(R.id.tv_specialInst);
        items = (TextView) convertView.findViewById(R.id.tv_items);
        seeMore = (TextView) convertView.findViewById(R.id.txtSeeMore);
        seeLess = (TextView) convertView.findViewById(R.id.txtSeeLess);
        content = (RelativeLayout) convertView.findViewById(R.id.hist_content);
       // line = (View) convertView.findViewById(R.id.orline);

      //  line.setVisibility(View.VISIBLE);
        seeMore.setOnClickListener(this);
        seeLess.setOnClickListener(this);

       // line.setPadding(10, 0, 10, 0);
        content.setPadding(10, 0, 10, 0);
        total.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    public void invalidate(RecentOrderDetails recentOrderDetails) {

        if (recentOrderDetails != null) {
            title.setText(Utils.toCamelCase(recentOrderDetails.getName()));
            total.setText(Utils.formatPrice(recentOrderDetails.getPrice()));
            if (recentOrderDetails.getSpecialInstructions().isEmpty()) {
                specialInstrctions.setText("No special instructions");
                specialInstrctions.setVisibility(View.GONE);
            } else {
                specialInstrctions.setText(recentOrderDetails.getSpecialInstructions());
                specialInstrctions.setVisibility(View.VISIBLE);
            }
            detail.setVisibility(View.VISIBLE);
            String selectedItems = "";
            String modifiers = "";
            if (recentOrderDetails.getChoices() != null) {
                for (BasketChoice basketChoice : recentOrderDetails.getChoices()) {
                    String choiceName = basketChoice.getName();
                    //1. Olo is sending extra choice with same name, skip it.
                    //2. For steel cut oat meal olo is sending nested product modifier parent in choice so skip it.
//                if (!detail.contains(choiceName) && !choiceName.toLowerCase().contains("click here"))
                    if (!choiceName.toLowerCase().contains("click here")) {
                        if (choiceName.contains("Small")) {
                            size = "Small";
                            continue;
                        }
                        if (choiceName.contains("Medium")) {
                            size = "Medium";
                            continue;
                        }
                        if (choiceName.contains("Large")) {
                            size = "Large";
                            continue;
                        }
//                        if (selectedItems.equals("")) {
//                            selectedItems = choiceName;
//                        } else {
//                            selectedItems += ", " + choiceName;
//                        }
                    }
                    if (!choiceName.toLowerCase().contains("click here")) {
                        if (!choiceName.contains("Small") && !choiceName.contains("Medium") && !choiceName.contains("Large")) {
                            if (modifiers.equals("")) {
                                modifiers = choiceName;
                            } else {
                                modifiers += ", " + choiceName;
                            }
                        }

                    }
                }
            }
            detail.setText(size);
            items.setVisibility(View.VISIBLE);
            if (modifiers.isEmpty()) {
                items.setText("No modifiers added");
                items.setVisibility(View.GONE);
            } else {
                items.setText(modifiers);
            }
            items.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (seeLess.getVisibility() == View.GONE) {
                        if (items.getLineCount() > 2) {
                            seeMore.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtSeeMore:
                showMoreText();
                break;
            case R.id.txtSeeLess:
                showLessText();
                break;
        }
    }

    public void showMoreText() {
        ObjectAnimator animation = ObjectAnimator.ofInt(items, "maxLines", items.getLineCount());
        animation.setDuration(100).start();
        seeMore.setVisibility(View.GONE);
        seeLess.setVisibility(View.VISIBLE);
    }

    public void showLessText() {
        ObjectAnimator animation = ObjectAnimator.ofInt(items, "maxLines", 2);
        animation.setDuration(100).start();
        seeLess.setVisibility(View.GONE);
        seeMore.setVisibility(View.VISIBLE);
    }
}
