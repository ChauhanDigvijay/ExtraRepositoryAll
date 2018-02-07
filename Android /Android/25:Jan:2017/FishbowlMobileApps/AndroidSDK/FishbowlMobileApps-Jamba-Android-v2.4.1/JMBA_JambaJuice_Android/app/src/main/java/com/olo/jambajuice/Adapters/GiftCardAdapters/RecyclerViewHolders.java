package com.olo.jambajuice.Adapters.GiftCardAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.olo.jambajuice.BusinessLogic.Interfaces.GiftCardInterFaces.TemplateSelectionInterfaces;
import com.olo.jambajuice.R;

public class RecyclerViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView cardImg;
    public RelativeLayout tickImgLayout;
    private TemplateSelectionInterfaces templateSelectionInterfaces;

    public RecyclerViewHolders(View itemView, TemplateSelectionInterfaces templateSelectionInterfaces,int screen_height) {
        super(itemView);
        itemView.setOnClickListener(this);
        cardImg = (ImageView)itemView.findViewById(R.id.cardImg);
        tickImgLayout = (RelativeLayout)itemView.findViewById(R.id.tickImgLayout);
        this.templateSelectionInterfaces = templateSelectionInterfaces;
        cardImg.getLayoutParams().height= (int) (screen_height*0.63);

    }

    @Override
    public void onClick(View view) {
        templateSelectionInterfaces.onSelection(getAdapterPosition());
    }
}
