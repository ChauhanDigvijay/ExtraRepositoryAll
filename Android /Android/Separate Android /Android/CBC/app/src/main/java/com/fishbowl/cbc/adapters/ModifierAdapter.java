package com.fishbowl.cbc.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fishbowl.cbc.R;
import com.fishbowl.cbc.businesslogic.models.ProductOption;
import com.fishbowl.cbc.businesslogic.models.StoreMenuProductModifier;

import java.util.HashMap;
import java.util.List;

/**
 * Created by VT027 on 4/26/2017.
 */

public class ModifierAdapter extends RecyclerView.Adapter {

    private static final int VIEWTYPE_HEADING = 0;
    private static final int VIEWTYPE_ITEMS = 1;
    private List<StoreMenuProductModifier> storeMenuList;
    private HashMap<Integer, List<ProductOption>> listDataChild;
    private List<ProductOption> listDataHeader;
    private LinearLayout linearLayout;

    public ModifierAdapter(LinearLayout linearLayout, List<StoreMenuProductModifier> storeMenuProductModifiers, List<ProductOption> listDataHeader, HashMap<Integer, List<ProductOption>> listDataChild) {
        this.storeMenuList = storeMenuProductModifiers;
        this.listDataChild = new HashMap<>(listDataChild);
        this.listDataHeader = listDataHeader;
        this.linearLayout = linearLayout;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEWTYPE_ITEMS) {
            //inflate your layout and pass it to view holder
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_modifier, parent, false);
            return new ModifierViewHolder(view, viewType, listDataHeader);
        } else if (viewType == VIEWTYPE_HEADING) {
            //inflate your layout and pass it to view holder
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_heading, parent, false);
            return new HeaderViewHolder(view);
        }
        return null;
        //return new ModifierAdapter.ModifierViewHolder(view,viewType,listDataHeader);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ModifierViewHolder) {
            ProductOption option = listDataChild.get(0).get(position);
            ((ModifierViewHolder) holder).invalidate(position, option, storeMenuList);
        } else {
            StoreMenuProductModifier storemenu = storeMenuList.get(position);
            ((HeaderViewHolder) holder).invalidate(storemenu);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEWTYPE_HEADING;
        } else {
            return VIEWTYPE_ITEMS;
        }
    }

    @Override
    public int getItemCount() {
        return listDataChild.get(0).size();
    }

    public class ModifierViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CheckBox checkBox;
        RadioGroup radioGroup;
        TextView optionTitle, cost;
        List<ProductOption> menuList;
        List<ProductOption> subOptions;

        public ModifierViewHolder(View itemView, int viewType, List<ProductOption> storeMenuList) {
            super(itemView);
            this.menuList = storeMenuList;
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
            radioGroup = (RadioGroup) itemView.findViewById(R.id.radioGroup);
            cost = (TextView) itemView.findViewById(R.id.Cost);
            optionTitle = (TextView) itemView.findViewById(R.id.optionTitle);
            checkBox.setVisibility(View.VISIBLE);
            optionTitle.setVisibility(View.VISIBLE);
            cost.setVisibility(View.VISIBLE);
            itemView.setOnClickListener(this);
        }

        public void invalidate(int position, ProductOption menuOptions, List<StoreMenuProductModifier> storeMenuList) {
            if (menuOptions.getSubOptions() != null) subOptions = menuOptions.getSubOptions();
            if (menuOptions.isDefault()) {
                checkBox.setChecked(true);
            }
            optionTitle.setText(menuOptions.getName());
            cost.setText(String.valueOf(menuOptions.getCost()));

        }

        @Override
        public void onClick(View v) {
            GridView gridView;
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
            } else {
                checkBox.setChecked(true);
                if (subOptions != null) {
                    gridView = new GridView(v.getContext());
                    GridAdapter adapter = new GridAdapter(v.getContext(), subOptions);
                    gridView.setAdapter(adapter);
                    linearLayout.addView(gridView);
                }
            }
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView optionTitle;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            optionTitle = (TextView) itemView.findViewById(R.id.category_head);
        }

        public void invalidate(StoreMenuProductModifier storeMenuList) {
            optionTitle.setText(storeMenuList.getDescription());

        }

    }
}
