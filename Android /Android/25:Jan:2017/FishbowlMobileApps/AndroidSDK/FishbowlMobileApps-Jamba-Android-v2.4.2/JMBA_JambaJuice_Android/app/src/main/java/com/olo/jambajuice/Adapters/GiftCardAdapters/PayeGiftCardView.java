package com.olo.jambajuice.Adapters.GiftCardAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.TemplateSelectionInterfaces;
import com.olo.jambajuice.R;

/**
 * Created by Ananad on 14-Sep-16.
 */
public class PayeGiftCardView extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tvCardName, tvCardNumber, tvCardAvlBal;
    public ImageView imgGiftCardView, selectGiftImg;
    public RelativeLayout tickImgGiftLayout;
    public LinearLayout eGiftCardViewRoot;
    private TemplateSelectionInterfaces templateSelectionInterfaces;

    public PayeGiftCardView(View v,TemplateSelectionInterfaces templateSelectionInterfaces) {
        super(v);
        tvCardName = (TextView) v.findViewById(R.id.tvCardName);
        tvCardNumber = (TextView) v.findViewById(R.id.tvCardNumber);
        tvCardAvlBal = (TextView) v.findViewById(R.id.tvCardAvlBal);
        imgGiftCardView = (ImageView) v.findViewById(R.id.imgGiftCardView);
        tickImgGiftLayout = (RelativeLayout) v.findViewById(R.id.tickImgGiftLayout);
        selectGiftImg = (ImageView) v.findViewById(R.id.selectGiftImg);
        eGiftCardViewRoot = (LinearLayout) v.findViewById(R.id.eGiftCardViewRoot);

    }

    @Override
    public void onClick(View view) {
        templateSelectionInterfaces.onSelection(getAdapterPosition());
    }
}
