package com.BasicApp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.BasicApp.Utils.FBUtils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Models.FBOfferItem;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;
import com.fishbowl.basicmodule.Utils.StringUtilities;

import java.util.Date;
import java.util.List;


/**
 * Created by digvijaychauhan on 31/07/16.
 */

public class  OfferAdapter extends ArrayAdapter<FBOfferItem> {

    Context context;
    private ImageLoader mImageLoader;
    public OfferAdapter(Context context, int resourceId,
                         List<FBOfferItem> items) {
        super(context, resourceId, items);
        this.context = context;
        mImageLoader = CustomVolleyRequestQueue.getInstance(context)
                .getImageLoader();
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtTitle1;
        TextView txtTitle;
        TextView time;
        NetworkImageView puss_img;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        FBOfferItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_offer_item_bistro, null);
            holder = new ViewHolder();

            holder.txtTitle = (TextView) convertView.findViewById(R.id.tv_name);
            holder.txtTitle1 = (TextView) convertView.findViewById(R.id.tv_expiry);
            holder.time = (TextView) convertView.findViewById(R.id.tv_name1);
         //   holder.puss_img=(NetworkImageView)convertView.findViewById(R.id.img_one);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (StringUtilities.isValidString(rowItem.getDatetime())) {
            Date d2 = FBUtils.getDateFromString(rowItem.getDatetime(), null);
            if (d2==null){

                 d2 = FBUtils.getDateFromStringSlash(rowItem.getDatetime(), null);
            }

            int diifer = FBUtils.daysBetween(new Date(), d2);
            if (String.valueOf(diifer)!=null){
            if (diifer > 0) {
                holder.time.setText("Expires in" + " " + diifer + " " + "days");
            }
            }
            else{

            }
        }

        holder.txtTitle.setText(rowItem.getOfferIItem());
        holder.txtTitle1.setText(rowItem.getOfferIName());

//        String url = rowItem.getOfferIOther();
//        if(url!=null) {
//            mImageLoader.get(url, ImageLoader.getImageListener(  holder.puss_img, R.drawable.mybrestoofferimage, R.drawable.mybrestoofferimage
//            ));
//            holder.puss_img.setImageUrl(url, mImageLoader);
//        }
//        else
//        {
//            holder.puss_img.setBackgroundResource(R.drawable.mybrestoofferimage);
//        }


        return convertView;
    }

    public void setOffer(List<FBOfferItem> rewards) {
        clear();
        addAll(rewards);
        this.notifyDataSetChanged();
    }
}