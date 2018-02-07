package com.olo.jambajuice.Adapters;

/**
 * Created by vt021 on 21/10/17.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.olo.jambajuice.R;
import com.wearehathway.apps.olo.Models.OloUpsellItems;

import java.util.List;


public class UpSellAdapter extends RecyclerView.Adapter<UpSellAdapter.UpSellViewHolder> {

    private List<OloUpsellItems> oloUpsellItemsList;

    public UpSellAdapter(List<OloUpsellItems> oloUpsellItemsList) {
        this.oloUpsellItemsList = oloUpsellItemsList;
    }

    @Override
    public UpSellViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upsell_row_boost, parent, false);
        return new UpSellViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final UpSellViewHolder holder, final int position) {
        holder.tv_upsell_detail.setText(oloUpsellItemsList.get(position).getName());
        holder.tv_price.setText(oloUpsellItemsList.get(position).getCost());

        if (oloUpsellItemsList.get(position).isSelected()) {
            holder.img_selected.setVisibility(View.VISIBLE);
            holder.modifierQuantityLayout.setVisibility(View.VISIBLE);
        } else {
            holder.img_selected.setVisibility(View.GONE);
            holder.modifierQuantityLayout.setVisibility(View.GONE);
        }

        int minQuantity = oloUpsellItemsList.get(position).getMinquantity();
        int maxQuantity = oloUpsellItemsList.get(position).getMaxquantity();
        final int length = (maxQuantity - minQuantity) + 1;
        String items[] = new String[length];
        for (int i = minQuantity - 1; i < maxQuantity; i++) {
            items[(i - minQuantity) + 1] = String.valueOf(i + 1);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.view.getContext(), R.layout.modifier_quantity_spinner_textview, items);
        holder.modifierQuantity.setAdapter(adapter);
        holder.modifierQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                oloUpsellItemsList.get(position).setSelectedQuantity(Integer.parseInt(holder.modifierQuantity.getSelectedItem().toString()));
                holder.tv_price_quantity.setText(Integer.parseInt(holder.modifierQuantity.getSelectedItem().toString()) + " x");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oloUpsellItemsList.get(position).isSelected()) {
                    oloUpsellItemsList.get(position).setSelected(false);
                    oloUpsellItemsList.get(position).setSelectedQuantity(1);
                    holder.modifierQuantity.setSelection(0);
                    holder.tv_price_quantity.setVisibility(View.GONE);
                    holder.img_selected.setVisibility(View.GONE);
                    holder.modifierQuantityLayout.setVisibility(View.GONE);
                } else {
                    oloUpsellItemsList.get(position).setSelected(true);
                    holder.tv_price_quantity.setVisibility(View.VISIBLE);
                    holder.modifierQuantity.setSelection(0);
                    holder.img_selected.setVisibility(View.VISIBLE);
                    holder.modifierQuantityLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return oloUpsellItemsList.size();
    }

    public class UpSellViewHolder extends RecyclerView.ViewHolder {
        TextView tv_upsell_detail;
        TextView tv_price_quantity;
        TextView tv_price;
        ImageView img_selected;
        Spinner modifierQuantity;
        RelativeLayout modifierQuantityLayout;
        View view;

        UpSellViewHolder(View view) {
            super(view);
            this.view = view;
            tv_upsell_detail = (TextView) view.findViewById(R.id.tv_upsell_detail);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_price_quantity = (TextView) view.findViewById(R.id.tv_price_quantity);
            img_selected = (ImageView) view.findViewById(R.id.img_selected);
            modifierQuantity = (Spinner) view.findViewById(R.id.modifierQuantity);
            modifierQuantityLayout = (RelativeLayout) view.findViewById(R.id.modifierQuantityLayout);
        }
    }
}
