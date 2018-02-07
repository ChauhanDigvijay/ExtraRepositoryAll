package com.hbh.honeybaked.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.hbh.honeybaked.R;

public class RedeemsAdapter extends Adapter<RedeemsAdapter.ViewHolder> {
    Integer earned_points;
    int[] loyaltyPointsArray;
    private Context mContext;

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ImageView dot_imageview;
        public TextView redeem_points_textview;
        public TextView redeem_value_textview;

        public ViewHolder(View v) {
            super(v);
            this.redeem_points_textview = (TextView) v.findViewById(R.id.redeem_points_textview);
            this.redeem_value_textview = (TextView) v.findViewById(R.id.redeem_value_textview);
            this.dot_imageview = (ImageView) v.findViewById(R.id.dot_imageview);
        }
    }

    public RedeemsAdapter(Context mContext, int[] loyaltyPointsArray, Integer earned_points) {
        this.mContext = mContext;
        this.earned_points = earned_points;
        this.loyaltyPointsArray = loyaltyPointsArray;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.redeem_point_list_item, parent, false));
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        if (this.loyaltyPointsArray[position] * 10 <= this.earned_points.intValue()) {
            holder.redeem_points_textview.setBackgroundResource(R.drawable.button_bg_round);
            holder.redeem_value_textview.setTextColor(this.mContext.getResources().getColor(R.color.ham_burg_new));
        } else {
            holder.redeem_points_textview.setBackgroundResource(R.drawable.button_bg_round_grey);
            holder.redeem_value_textview.setTextColor(this.mContext.getResources().getColor(R.color.ham_ash));
        }
        holder.redeem_points_textview.setText("$" + this.loyaltyPointsArray[position]);
        holder.redeem_value_textview.setText((this.loyaltyPointsArray[position] * 10) + " Points");
        if (position == this.loyaltyPointsArray.length - 1) {
            holder.dot_imageview.setVisibility(View.GONE);
        } else {
            holder.dot_imageview.setVisibility(View.GONE);
        }
    }

    public int getItemCount() {
        return this.loyaltyPointsArray.length;
    }
}
