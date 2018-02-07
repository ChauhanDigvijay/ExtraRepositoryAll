package com.olo.jambajuice.Adapters.GiftCardAdapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.PaymentSelectionInterfaceCallBack;
import com.olo.jambajuice.BusinessLogic.Managers.GiftCardDataManager.GiftCardDataManager;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Views.Generic.SemiBoldTextView;
import com.squareup.picasso.Picasso;
import com.wearehathway.apps.incomm.Models.InCommBrandCreditCardType;
import com.wearehathway.apps.incomm.Models.InCommUserPaymentAccount;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ananad on 26-Aug-16.
 */
public class CardListAdapater extends RecyclerView.Adapter<CardListAdapater.ViewHolder>  {
    ArrayList<InCommUserPaymentAccount> userPaymentAccount;
    Context context;
    PaymentSelectionInterfaceCallBack callBack;
    int pos;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_credit_card_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        context=parent.getContext();
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(userPaymentAccount != null) {
            final InCommUserPaymentAccount name = userPaymentAccount.get(position);
            String cardType = userPaymentAccount.get(position).getCreditCardTypeCode();
            List<InCommBrandCreditCardType> allCards = GiftCardDataManager.getInstance().getBrands().get(0).getCreditCardTypes();
            for (int i = 0; i < allCards.size(); i++) {
                String cardsName = allCards.get(i).getCreditCardType();
                String imgUrl = allCards.get(i).getThumbnailImageUrl();
                if (cardType.equalsIgnoreCase(cardsName)) {
                    Picasso.with(context)
                            .load(imgUrl)
                            .placeholder(R.drawable.product_placeholder)
                            .error(R.drawable.product_placeholder)
                            .into(holder.imgCardView);
                    break;
                }

            }

            if (name.getCreditCardNumber() != null) {
                holder.tvCreditCardNo.setText("xxxx xxxx xxxx " + name.getCreditCardNumber());
            }

            if (pos==name.getId()){
                GiftCardDataManager.getInstance().setPaymentAccountID(name.getId());
                holder.checkStatus.setVisibility(View.VISIBLE);
                holder.notExpand.setVisibility(View.GONE);
            }
            else {
                holder.checkStatus.setVisibility(View.GONE);
                holder.notExpand.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public int getItemCount() {

        return userPaymentAccount.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public SemiBoldTextView tvCreditCardNo;
        private ImageView imgCardView;
        private ImageButton notExpand,checkStatus;
        private RelativeLayout creditCardLayout;

        public ViewHolder(View v) {
            super(v);
            tvCreditCardNo = (SemiBoldTextView) v.findViewById(R.id.tvCreditCardNo);
            imgCardView=(ImageView) v.findViewById(R.id.imgCardView);
            notExpand=(ImageButton) v.findViewById(R.id.notExpand);
            checkStatus=(ImageButton) v.findViewById(R.id.checkStatus);
            creditCardLayout = (RelativeLayout)v.findViewById(R.id.creditCardLayout);
            creditCardLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.onSelection(getAdapterPosition());
                }
            });
        }
    }

    public CardListAdapater(ArrayList<InCommUserPaymentAccount> userPaymentAcc, PaymentSelectionInterfaceCallBack interfaceCallBack, int pos) {
        userPaymentAccount = userPaymentAcc;
        callBack = interfaceCallBack;
        this.pos=pos;
    }
}
