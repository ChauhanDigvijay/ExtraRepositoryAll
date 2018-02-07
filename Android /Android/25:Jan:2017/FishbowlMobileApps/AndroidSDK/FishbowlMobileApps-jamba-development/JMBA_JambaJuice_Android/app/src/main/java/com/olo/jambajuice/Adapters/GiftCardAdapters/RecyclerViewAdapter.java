package com.olo.jambajuice.Adapters.GiftCardAdapters;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.TemplateSelectionInterfaces;
import com.olo.jambajuice.BusinessLogic.Models.GiftCardModels.GiftCardTemplate;
import com.olo.jambajuice.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> {

    private List<GiftCardTemplate> itemList;
    private Context context;
    private int cellWidth;
    private TemplateSelectionInterfaces templateSelectionInterfaces;

    public RecyclerViewAdapter(Context context, List<GiftCardTemplate> itemList, TemplateSelectionInterfaces templateSelectionInterfaces, int cellWidth) {
        this.itemList = itemList;
        this.context = context;
        this.templateSelectionInterfaces = templateSelectionInterfaces;
        this.cellWidth=cellWidth;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        @SuppressLint("InflateParams") View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_list, null);
        return new RecyclerViewHolders(layoutView, templateSelectionInterfaces,cellWidth);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        Picasso.with(context)
                .load(itemList.get(position).getInCommBrandCardImage().getImageUrl())
                .placeholder(R.drawable.product_placeholder)
                .error(R.drawable.product_placeholder)
                .into(holder.cardImg);

        if (itemList.get(position).getSelected()) {
            holder.tickImgLayout.setVisibility(View.VISIBLE);
            holder.cardImg.setImageAlpha(128);
        } else {
            holder.tickImgLayout.setVisibility(View.GONE);
            holder.cardImg.setImageAlpha(255);
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
