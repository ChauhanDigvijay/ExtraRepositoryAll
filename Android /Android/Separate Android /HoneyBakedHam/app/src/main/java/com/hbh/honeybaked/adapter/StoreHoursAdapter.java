package com.hbh.honeybaked.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.helper.PreferenceHelper;
import com.hbh.honeybaked.listener.AdapterListener;

public class StoreHoursAdapter extends BaseAdapter {
    private AdapterListener adapterListener;
    private Context context;
    private LayoutInflater inflater = null;
    private int itemHeight;
    private PreferenceHelper preferenceHelper;
    String[] store_hr_data = null;

    public class ViewHolder {
        TextView store_hrs_head_tv;
        TextView store_hrs_time_tv;
        LinearLayout week_parent;
    }

    public StoreHoursAdapter(Context context, String[] store_hr_data, AdapterListener adapterListener) {
        this.inflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.context = context;
        this.store_hr_data = store_hr_data;
        this.adapterListener = adapterListener;
        this.preferenceHelper = new PreferenceHelper(context);
    }

    public int getCount() {
        return this.store_hr_data.length;
    }

    public Object getItem(int i) {
        return Integer.valueOf(i);
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        View vi = view;
        if (view == null) {
            holder = new ViewHolder();
            vi = this.inflater.inflate(R.layout.store_hours_custom_layout, null);
            holder.store_hrs_head_tv = (TextView) vi.findViewById(R.id.store_hrs_head_tv);
            holder.store_hrs_time_tv = (TextView) vi.findViewById(R.id.store_hrs_time_tv);
            holder.week_parent = (LinearLayout) vi.findViewById(R.id.week_parent);
            if (this.preferenceHelper.getIntValue("store_hours_listview_height") <= 0) {
                holder.week_parent.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        holder.week_parent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        StoreHoursAdapter.this.itemHeight = holder.week_parent.getMeasuredHeight();
                        StoreHoursAdapter.this.preferenceHelper.saveIntValue("store_hours_listview_height", StoreHoursAdapter.this.itemHeight);
                        StoreHoursAdapter.this.adapterListener.performAdapterAction("set_store_hours_listview_height", Integer.valueOf(StoreHoursAdapter.this.itemHeight));
                    }
                });
            } else {
                this.adapterListener.performAdapterAction("set_store_hours_listview_height", Integer.valueOf(this.preferenceHelper.getIntValue("store_hours_listview_height")));
            }
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        String[] time_arr = this.store_hr_data[position].split(":");
        holder.store_hrs_head_tv.setText(time_arr[0] + ":");
        holder.store_hrs_time_tv.setText(this.store_hr_data[position].substring(time_arr[0].length() + 1));
        return vi;
    }
}
