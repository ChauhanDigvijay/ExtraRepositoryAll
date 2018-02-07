//package com.hbh.honeybaked.activity;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.TextView;
//import com.hbh.honeybaked.R;
//import com.pinterest.android.pdk.PDKCallback;
//import com.pinterest.android.pdk.PDKClient;
//import com.pinterest.android.pdk.PDKException;
//import com.pinterest.android.pdk.PDKPin;
//import com.pinterest.android.pdk.PDKResponse;
//import com.squareup.picasso.Picasso;
//import java.util.ArrayList;
//import java.util.List;
//
//public class MyPinsActivity extends Activity {
//    private static final String PIN_FIELDS = "id,link,creator,image,counts,note,created_at,board,metadata";
//    private GridView gridView;
//    private boolean loading = false;
//    private PDKCallback myPinsCallback;
//    private PDKResponse myPinsResponse;
//    private PinsAdapter pinAdapter;
//
//    class C17031 extends PDKCallback {
//        C17031() {
//        }
//
//        public void onSuccess(PDKResponse response) {
//            MyPinsActivity.this.loading = false;
//            MyPinsActivity.this.myPinsResponse = response;
//            MyPinsActivity.this.pinAdapter.setPinList(response.getPinList());
//        }
//
//        public void onFailure(PDKException exception) {
//            MyPinsActivity.this.loading = false;
//            Log.e(getClass().getName(), exception.getDetailMessage());
//        }
//    }
//
//    private class PinsAdapter extends BaseAdapter {
//        private Context _context;
//        private List<PDKPin> _pinList;
//
//        private class ViewHolderItem {
//            ImageView imageView;
//            TextView textViewItem;
//
//            private ViewHolderItem() {
//            }
//        }
//
//        public PinsAdapter(Context c) {
//            this._context = c;
//        }
//
//        public void setPinList(List<PDKPin> list) {
//            if (this._pinList == null) {
//                this._pinList = new ArrayList();
//            }
//            if (list == null) {
//                this._pinList.clear();
//            } else {
//                this._pinList.addAll(list);
//            }
//            notifyDataSetChanged();
//        }
//
//        public List<PDKPin> getPinList() {
//            return this._pinList;
//        }
//
//        public int getCount() {
//            return this._pinList == null ? 0 : this._pinList.size();
//        }
//
//        public Object getItem(int position) {
//            return Integer.valueOf(position);
//        }
//
//        public long getItemId(int position) {
//            return (long) position;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolderItem viewHolder;
//            if (this._pinList.size() - position < 5) {
//                MyPinsActivity.this.loadNext();
//            }
//            if (convertView == null) {
//                convertView = ((Activity) this._context).getLayoutInflater().inflate(R.layout.list_item_pin, parent, false);
//                viewHolder = new ViewHolderItem();
//                viewHolder.textViewItem = (TextView) convertView.findViewById(R.id.title_view);
//                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
//                convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolderItem) convertView.getTag();
//            }
//            PDKPin pinItem = (PDKPin) this._pinList.get(position);
//            if (pinItem != null) {
//                viewHolder.textViewItem.setText(pinItem.getNote());
//                Picasso.with(this._context.getApplicationContext()).load(pinItem.getImageUrl()).into(viewHolder.imageView);
//            }
//            return convertView;
//        }
//    }
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_my_pins);
//        setTitle("My Pins");
//        this.pinAdapter = new PinsAdapter(this);
//        this.gridView = (GridView) findViewById(R.id.grid_view);
//        this.gridView.setAdapter(this.pinAdapter);
//        this.myPinsCallback = new C17031();
//        this.loading = true;
//        fetchPins();
//    }
//
//    protected void onResume() {
//        super.onResume();
//        fetchPins();
//    }
//
//    private void fetchPins() {
//        this.pinAdapter.setPinList(null);
//        PDKClient.getInstance().getMyPins(PIN_FIELDS, this.myPinsCallback);
//    }
//
//    private void loadNext() {
//        if (!this.loading && this.myPinsResponse.hasNext()) {
//            this.loading = true;
//            this.myPinsResponse.loadNext(this.myPinsCallback);
//        }
//    }
//}
