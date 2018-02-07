package com.olo.jambajuice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductCategoryDetail.BaseProductCategoryDetailViewHolder;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductCategoryDetail.ProductCategoryDetailHeaderViewHolder;
import com.olo.jambajuice.Activites.NonGeneric.Menu.ProductCategoryDetail.ProductCategoryDetailViewHolder;
import com.olo.jambajuice.BusinessLogic.Models.Product;
import com.olo.jambajuice.BusinessLogic.Models.ProductCategory;
import com.olo.jambajuice.R;

import java.util.List;

/**
 * Created by Nauman Afzaal on 13/05/15.
 */
public class ProductCategoryDetailAdapter extends RecyclerView.Adapter<BaseProductCategoryDetailViewHolder> {

    private static final int ITEM_TYPE = 1;
    private static final int HEADER_TYPE = 0;
    LayoutInflater infalInflater;
    private List<Product> data;
    private ProductCategory category;
    private Activity context;

    public ProductCategoryDetailAdapter(Activity activity, ProductCategory category, List<Product> data) {
        this.data = data;
        this.context = activity;
        this.category = category;
        infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    private boolean isHeader(int position) {
        return position == 0;
    }

    @Override
    public BaseProductCategoryDetailViewHolder onCreateViewHolder(ViewGroup viewGroup, int type) {
        // create a new view
        View v = null;
        // set the view's size, margins, paddings and layout parameters
        v = infalInflater.inflate(getRowXMLId(type), viewGroup, false);
        if (type == HEADER_TYPE) {
            return new ProductCategoryDetailHeaderViewHolder(v);
        } else {
            return new ProductCategoryDetailViewHolder(context, v, data);
        }
    }

    @Override
    public void onBindViewHolder(BaseProductCategoryDetailViewHolder viewHolder, int position) {
        if (isHeader(position)) {
            ProductCategoryDetailHeaderViewHolder headerViewHolder = (ProductCategoryDetailHeaderViewHolder) viewHolder;
            headerViewHolder.invalidate(category);
        } else {
            Product product = data.get(position - 1);
            ProductCategoryDetailViewHolder headerViewHolder = (ProductCategoryDetailViewHolder) viewHolder;
            headerViewHolder.invalidate(product);
        }

        if (viewHolder != null) {
            if (viewHolder.getContinueShoppingBtn() != null) {
                viewHolder.getContinueShoppingBtn().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.onBackPressed();
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? HEADER_TYPE : ITEM_TYPE;
    }

    public int getRowXMLId(int type) {
        return type == HEADER_TYPE ? R.layout.row_product_category_header : R.layout.row_product;
    }

    public void addData(List<Product> data) {

        this.data.addAll(data);
        this.notifyDataSetChanged();
    }
}