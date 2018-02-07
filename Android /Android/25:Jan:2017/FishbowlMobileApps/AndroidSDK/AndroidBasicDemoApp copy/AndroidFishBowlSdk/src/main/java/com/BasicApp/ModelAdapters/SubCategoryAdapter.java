package com.BasicApp.ModelAdapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Filter;
import android.view.View;
import android.view.ViewGroup;

import com.BasicApp.BusinessLogic.Models.Category;
import com.BasicApp.BusinessLogic.Models.Product;
import com.BasicApp.Fragments.SubCategoryFragment;
import com.basicmodule.sdk.R;

import java.util.ArrayList;

public class SubCategoryAdapter  extends RecyclerView.Adapter<SubCategoryHolder>{

    ArrayList<Category> categories;
    Activity context;
    Category category;
    int catposition;
    public SubCategoryFragment.MyAdapter mAdapter;

    public SubCategoryAdapter(Activity _context, Category _category,int catposition1 ){
        context=_context;
        category=_category;
        catposition=catposition1;
    }

    public Filter getFilter(){
        Filter filter=null;
        return filter;
    }




    @Override
    public SubCategoryHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(getRowXMLId(), viewGroup, false);
        // set the view's size, margins, paddings and layout parameters
        SubCategoryHolder vh = new SubCategoryHolder(context, v, category,catposition);
        return vh;
    }

    @Override
    public void onBindViewHolder(SubCategoryHolder holder, int position) {

        Product product=   category.products.get(position);
        holder.invalidate(product);
    }



    public int getRowXMLId()
    {
        return R.layout.subheaderlist;
    }

    @Override
    public int getItemCount() {
        return category.products.size();
    }


}
