package com.fishbowl.cbc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fishbowl.cbc.R;
import com.fishbowl.cbc.businesslogic.models.ProductOption;

import java.util.List;

/**
 * Created by VT027 on 4/27/2017.
 */

class GridAdapter extends BaseAdapter {

    private Context mContext;
    private List<ProductOption> modoptions;

    public GridAdapter(Context context, List<ProductOption> options) {
        this.mContext = context;
        this.modoptions = options;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return modoptions.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CheckBox checkBox;
        RadioGroup radioGroup;
        TextView cost;

        if (convertView == null) {
            // If convertView is null then inflate the appropriate layout file
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_modifier, null);
        }
        checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
        radioGroup = (RadioGroup) convertView.findViewById(R.id.radioGroup);
        cost = (TextView) convertView.findViewById(R.id.Cost);
        if (modoptions.get(position).isDefault()) {
            checkBox.setChecked(true);
        }
        cost.setText((int) modoptions.get(position).getCost());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }
            }
        });
        return convertView;
    }
}
