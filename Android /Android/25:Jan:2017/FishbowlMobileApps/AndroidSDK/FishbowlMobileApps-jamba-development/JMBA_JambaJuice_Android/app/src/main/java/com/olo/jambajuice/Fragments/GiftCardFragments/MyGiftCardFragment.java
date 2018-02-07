package com.olo.jambajuice.Fragments.GiftCardFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.koushikdutta.ion.Ion;
import com.olo.jambajuice.Activites.NonGeneric.GiftCardNonGeneric.ManageGiftCard.CardDetailsActivityGiftCard;
import com.olo.jambajuice.R;
import com.olo.jambajuice.Utils.TransitionManager;
import com.wearehathway.apps.incomm.Models.InCommCard;


public class MyGiftCardFragment extends Fragment {
    int userId;
    public static final MyGiftCardFragment newInstance(InCommCard card, int position){
        MyGiftCardFragment f = new MyGiftCardFragment();
        Bundle b = new Bundle();
        b.putString("url",card.getImageUrl());
        b.putInt("cardId", card.getCardId());
        b.putInt("dataIndex", position);
        f.setArguments(b);
        return f;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_gift_card_preview,container,false);
        ImageView imageViewGiftCardPreview =(ImageView)v.findViewById(R.id.imageViewGiftCardPreview);
        Button btnMyCardInfo=(Button)v.findViewById(R.id.btnMyCardInfo);

        btnMyCardInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cardId=getArguments().getInt("cardId");
                int dataIndex=getArguments().getInt("dataIndex");
                Bundle bundle = new Bundle();
                bundle.putInt("cardId",cardId);
                bundle.putInt("dataIndex",dataIndex);
                TransitionManager.transitFrom(getActivity(),CardDetailsActivityGiftCard.class,bundle);
            }
        });

        String imageURl=getArguments().getString("url");
        if(!TextUtils.isEmpty(imageURl)) {
            Ion.with(this)
                    .load(imageURl)
                    .withBitmap()
                    .placeholder(R.drawable.product_placeholder)
                    .error(R.drawable.product_placeholder)
                    .intoImageView(imageViewGiftCardPreview);
        }
        return v;
    }
}
