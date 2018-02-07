package com.hbh.honeybaked.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.helper.PreferenceHelper;
import com.hbh.honeybaked.listener.AdapterListener;
import com.hbh.honeybaked.module.GridModule;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class GridMenuAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private HashMap<String, List<GridModule>> _listDataChild;
    private List<String> _listDataHeader;
    AdapterListener adapter_listener = null;
    protected PreferenceHelper hbha_pref_helper;
    String[] header_arr = null;
    LinearLayout layout;

    class C17101 implements OnGlobalLayoutListener {
        C17101() {
        }

        public void onGlobalLayout() {
            GridMenuAdapter.this.layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            GridMenuAdapter.this.hbha_pref_helper.saveIntValue("grid_height", GridMenuAdapter.this.layout.getMeasuredHeight());
        }
    }

    public GridMenuAdapter(Context context, AdapterListener adapter_listener, List<String> listDataHeader, HashMap<String, List<GridModule>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.adapter_listener = adapter_listener;
        this.hbha_pref_helper = new PreferenceHelper(this._context);
    }

    public Object getChild(int groupPosition, int childPosititon) {
        if (this._listDataChild.get(this._listDataHeader.get(groupPosition)) == null) {
            return Integer.valueOf(0);
        }
        return (Serializable) ((List) this._listDataChild.get(this._listDataHeader.get(groupPosition))).get(childPosititon);
    }

    public Object getChildItems(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.grid_child_layout, null);
            if (this.hbha_pref_helper.getIntValue("grid_height") == 0) {
                this.layout = (LinearLayout) convertView.findViewById(R.id.grid_main);
                this.layout.getViewTreeObserver().addOnGlobalLayoutListener(new C17101());
            }
        }
        List<GridModule> menuModelList = (List) getChildItems(groupPosition);
        GridView grid = (GridView) convertView.findViewById(R.id.grid_vw);
        if (this.hbha_pref_helper.getIntValue("grid_height") != 0) {
            int height;
            if (menuModelList == null || menuModelList.size() <= 1) {
                height = this.hbha_pref_helper.getIntValue("grid_height");
            } else {
                height = this.hbha_pref_helper.getIntValue("grid_height") * Math.round((float) ((menuModelList.size() + (menuModelList.size() % 2)) / 2));
            }
            grid.setLayoutParams(new LayoutParams(-1, height));
        }
        grid.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View v, int position, long id) {
                String[] ht = ((String) GridMenuAdapter.this.getGroup(groupPosition)).split("######");
                GridModule gridModule = (GridModule) ((List) GridMenuAdapter.this.getChildItems(groupPosition)).get(position);
                GridMenuAdapter.this.adapter_listener.performAdapterAction(AppConstants.GRID_CLICK, new Object[]{GridMenuAdapter.this._listDataHeader.get(groupPosition), gridModule, ht[0].toString(), GridMenuAdapter.this._listDataHeader, GridMenuAdapter.this._listDataChild, Integer.valueOf(groupPosition)});
            }
        });
        grid.setAdapter(new CustomGridAdapter(this._context, menuModelList));
        return convertView;
    }

    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)) == null ? 0 : 1;
    }

    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        this.header_arr = ((String) getGroup(groupPosition)).split("######");
        if (convertView == null) {
            convertView = ((LayoutInflater) this._context.getSystemService("layout_inflater")).inflate(R.layout.grid_header_layout, null);
        }
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.grid_header_tv);
        ImageView exban_indicator = (ImageView) convertView.findViewById(R.id.exban_indicator);
        if (isExpanded) {
            exban_indicator.setImageResource(R.drawable.white_arrow_down);
        } else {
            exban_indicator.setImageResource(R.drawable.white_arrow);
        }
        lblListHeader.setTypeface(null, 1);
        lblListHeader.setText(this.header_arr[1]);
        return convertView;
    }

    public boolean hasStableIds() {
        return false;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
