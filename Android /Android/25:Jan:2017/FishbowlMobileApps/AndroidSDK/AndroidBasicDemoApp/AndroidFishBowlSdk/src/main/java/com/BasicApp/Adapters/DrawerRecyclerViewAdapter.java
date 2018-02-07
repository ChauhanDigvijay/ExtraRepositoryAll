package com.BasicApp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.BasicApp.BusinessLogic.Models.NewMenuDrawer;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import java.util.ArrayList;

public class DrawerRecyclerViewAdapter  extends RecyclerView.Adapter<DrawerRecyclerViewAdapter.ViewHolder> {

    private ArrayList<NewMenuDrawer> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ImageLoader mImageLoader;
    Context context;


    // data is passed into the constructor
    public DrawerRecyclerViewAdapter(Context context, ArrayList<NewMenuDrawer>data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grid_menu_drawer, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mImageLoader= CustomVolleyRequestQueue.getInstance(context).getImageLoader();
        holder.myTextView.setText(mData.get(position).getProductName());
        final String url =mData.get(position).getProductImageUrl();
        mImageLoader.get(url, ImageLoader.getImageListener(holder.imageView, R.drawable.menu_burger,  R.drawable.menu_burger));
        holder.imageView.setImageUrl(url,mImageLoader);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView myTextView;
        public NetworkImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.info_text);
            imageView = (NetworkImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
 /*   public String getItem(int id) {
        return mData[id];
    }*/

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}