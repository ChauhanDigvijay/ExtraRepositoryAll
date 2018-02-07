package com.BasicApp.ModelAdapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.MenuSubCategory;
import com.basicmodule.sdk.R;

import java.util.ArrayList;

public class MenuSubCategoryRecyclerViewAdapter extends RecyclerView.Adapter<MenuSubCategoryRecyclerViewAdapter.MenuSubCategoryHolder> {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<MenuSubCategory> mDataset;
    private static MyClickListener myClickListener;
    @Override
    public MenuSubCategoryRecyclerViewAdapter.MenuSubCategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);

        MenuSubCategoryHolder menuSubCategoryHolder = new MenuSubCategoryHolder(view);
        return menuSubCategoryHolder;

    }

    @Override
    public void onBindViewHolder(MenuSubCategoryRecyclerViewAdapter.MenuSubCategoryHolder holder, int position) {
        holder.productName.setText(mDataset.get(position).getProductSubCategoryName());
    }
    public void addItem(MenuSubCategory dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class MenuSubCategoryHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        TextView productName;
        TextView dateTime;
        public MenuSubCategoryHolder(View itemView) {
            super(itemView);
            productName = (TextView) itemView.findViewById(R.id.textView);
           // dateTime = (TextView) itemView.findViewById(R.id.textView2);
            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
         //   myClickListener.onItemClick(getPosition(), v);
         //   Toast.makeText(context, "Item Clicked at "+ getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public MenuSubCategoryRecyclerViewAdapter(ArrayList<MenuSubCategory> myDataset) {
        mDataset = myDataset;
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
