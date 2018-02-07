package com.hbh.honeybaked.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.module.GridModule;
import com.hbh.honeybaked.supportingfiles.Utility;
import java.util.List;

public class CustomGridAdapter extends BaseAdapter {
    List<GridModule> gridModuleList;
    private Context mContext;

    public CustomGridAdapter(Context c, List<GridModule> gridModuleList) {
        this.mContext = c;
        this.gridModuleList = gridModuleList;
    }

    public int getCount() {
        return this.gridModuleList.size();
    }

    public Object getItem(int position) {
        return this.gridModuleList.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        if (convertView == null) {
            grid = inflater.inflate(R.layout.grid_custom_layout, null);
        } else {
            grid = convertView;
        }
        GridModule menuModel = (GridModule) getItem(position);
        ImageView imageView = (ImageView) grid.findViewById(R.id.grid_image);
        ((TextView) grid.findViewById(R.id.grid_text)).setText(Html.fromHtml(menuModel.getsProductName().replaceAll("\\|", MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR)));
        Utility.loadImagesToView(this.mContext, menuModel.getsImageUrl(), imageView, R.drawable.launcher_icon);
        return grid;
    }
}
