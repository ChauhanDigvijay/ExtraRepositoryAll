package com.olo.jambajuice.Adapters.GiftCardAdapters;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.TemplateSelectionInterfaces;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.PayGiftCard;
import com.olo.jambajuice.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PayeGiftCardListAdapter extends RecyclerView.Adapter<PayeGiftCardListAdapter.ViewHolder> {
    private List<PayGiftCard> eGiftCardList;
    private Context context;
    private int CardId;
    public TemplateSelectionInterfaces templateSelectionInterfaces;
    private boolean isNothingSelected = true;

    @Override
    public PayeGiftCardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_egift_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v, templateSelectionInterfaces);
        context = parent.getContext();
        return vh;
    }

    public PayeGiftCardListAdapter(Context context, List<PayGiftCard> eGiftCardList, TemplateSelectionInterfaces templateSelectionInterfaces) {
        this.eGiftCardList = eGiftCardList;
        this.context = context;
        this.templateSelectionInterfaces = templateSelectionInterfaces;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final PayeGiftCardListAdapter.ViewHolder holder, int position) {
        String balance = String.format("%.2f", eGiftCardList.get(position).geteGiftCardList().getBalance());
        Picasso.with(context)
                .load(eGiftCardList.get(position).geteGiftCardList().getImageUrl())
                .placeholder(R.drawable.product_placeholder)
                .error(R.drawable.product_placeholder)
                .into(holder.imgGiftCardView);
        holder.tvCardName.setText(eGiftCardList.get(position).geteGiftCardList().getCardName());
        holder.tvCardAvlBal.setText("$" + balance);
        holder.tvCardNumber.setText(eGiftCardList.get(position).geteGiftCardList().getCardNumber());
        if (eGiftCardList.get(position).getSelected()) {
            holder.selectGiftImg.setVisibility(View.VISIBLE);
            holder.eGiftCardViewRoot.setBackgroundColor(Color.WHITE);
            holder.eGiftCardViewRoot.setAlpha(1f);
            holder.selectGiftImg.setImageAlpha(255);
            holder.imgGiftCardView.setImageAlpha(180);
        } else {
            isNothingSelected = true;
            holder.selectGiftImg.setVisibility(View.GONE);
            holder.eGiftCardViewRoot.setBackgroundColor(ContextCompat.getColor(context,R.color.semi_transparent_white));
            holder.eGiftCardViewRoot.setAlpha(0.5f);
            holder.imgGiftCardView.setImageAlpha(128);
        }

        for(int i = 0;i< eGiftCardList.size();i++){
            if(eGiftCardList.get(i).getSelected()){
                isNothingSelected = false;
            }
        }

        if(isNothingSelected){
            holder.selectGiftImg.setVisibility(View.GONE);
            holder.eGiftCardViewRoot.setBackgroundColor(Color.WHITE);
            holder.eGiftCardViewRoot.setAlpha(1f);
            holder.imgGiftCardView.setImageAlpha(255);
        }

    }


    @Override
    public int getItemCount() {
        return eGiftCardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView tvCardName, tvCardNumber, tvCardAvlBal;
        public ImageView imgGiftCardView, selectGiftImg;
        public RelativeLayout tickImgGiftLayout;
        public LinearLayout eGiftCardViewRoot;
        private TemplateSelectionInterfaces templateSelectionInterfaces;

        public ViewHolder(View v, TemplateSelectionInterfaces templateSelectionInterfaces) {
            super(v);
            v.setOnClickListener(this);
            tvCardName = (TextView) v.findViewById(R.id.tvCardName);
            tvCardNumber = (TextView) v.findViewById(R.id.tvCardNumber);
            tvCardAvlBal = (TextView) v.findViewById(R.id.tvCardAvlBal);
            imgGiftCardView = (ImageView) v.findViewById(R.id.imgGiftCardView);
            tickImgGiftLayout = (RelativeLayout) v.findViewById(R.id.tickImgGiftLayout);
            selectGiftImg = (ImageView) v.findViewById(R.id.selectGiftImg);
            eGiftCardViewRoot = (LinearLayout) v.findViewById(R.id.eGiftCardViewRoot);
            this.templateSelectionInterfaces = templateSelectionInterfaces;

        }

        public void onClick(View view) {
            templateSelectionInterfaces.onSelection(getAdapterPosition());
        }
    }
}
