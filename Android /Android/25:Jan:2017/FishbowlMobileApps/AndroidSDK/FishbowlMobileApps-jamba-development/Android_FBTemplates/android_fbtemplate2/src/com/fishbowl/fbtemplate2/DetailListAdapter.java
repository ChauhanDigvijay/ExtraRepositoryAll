package com.fishbowl.fbtemplate2;


/**
 * Created by Digvijay Chauhan on 7/12/15.
 */
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailListAdapter extends ArrayAdapter<DetailItem> {

	Context context;

	public DetailListAdapter(Context context, int resourceId,
			List<DetailItem> items) {
		super(context, resourceId, items);
		this.context = context;
	}
	
	/*private view holder class*/
	private class ViewHolder {
		ImageView imageView;
		TextView txtTitle;
		TextView txtDesc;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		DetailItem rowItem = getItem(position);
		
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_detail, null);
			holder = new ViewHolder();
			holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
			holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
			convertView.setTag(holder);
		} else 
			holder = (ViewHolder) convertView.getTag();
				
		holder.txtDesc.setText(rowItem.getDesc());
		holder.txtTitle.setText(rowItem.getTitle());
		holder.imageView.setImageResource(rowItem.getImageId());
		
		return convertView;
	}
}