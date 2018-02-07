package com.hbh.honeybaked.fragment;

import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBUserService.FBMenuProductCallback;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.module.GridModule;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.supportingfiles.Utility;

import org.json.JSONObject;

public class MenuDetailsFragment extends BaseFragment {
    GridModule gridModule;
    ImageView menu_details_arrow;
    TextView note_tv;
    TableRow product_back_tr;
    int product_catagory_id;
    TextView product_des_tv;
    int product_famaily_id;
    LinearLayout product_img_ll;
    TextView product_price_tv;
    int product_sub_catagory_id;
    TextView product_title_tv;

    class C17521 implements FBMenuProductCallback {
        C17521() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onMenuProductCallback(JSONObject r10, Exception r11) {
            /*
            r9 = this;
            r8 = 0;
            r6 = 4636737291354636288; // 0x4059000000000000 float:0.0 double:100.0;
            if (r10 == 0) goto L_0x00ae;
        L_0x0005:
            r2 = "productDetails";
            r2 = r10.has(r2);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            if (r2 == 0) goto L_0x0062;
        L_0x000d:
            r2 = "productDetails";
            r1 = r10.optJSONObject(r2);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r2 = "productPrice";
            r2 = r1.optDouble(r2);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r0 = java.lang.Double.valueOf(r2);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r2 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r2 = r2.gridModule;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r2 = r2.getP_short_desc();	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r2 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r2);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            if (r2 != 0) goto L_0x006e;
        L_0x002b:
            r2 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r2 = r2.product_price_tv;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r3.<init>();	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = "$ ";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = r0.doubleValue();	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = r4 * r6;
            r4 = java.lang.Math.round(r4);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = (double) r4;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = r4 / r6;
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = " ";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = r4.gridModule;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = r4.getP_short_desc();	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r3 = r3.toString();	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r2.setText(r3);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
        L_0x0062:
            r2 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;
            r3 = "hide_progress_dialog";
            r4 = java.lang.Boolean.valueOf(r8);
            r2.performDialogAction(r3, r4);
        L_0x006d:
            return;
        L_0x006e:
            r2 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r2 = r2.product_price_tv;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r3 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r3.<init>();	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = "$ ";
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = r0.doubleValue();	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = r4 * r6;
            r4 = java.lang.Math.round(r4);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = (double) r4;	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r4 = r4 / r6;
            r3 = r3.append(r4);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r3 = r3.toString();	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            r2.setText(r3);	 Catch:{ Exception -> 0x0094, all -> 0x00a1 }
            goto L_0x0062;
        L_0x0094:
            r2 = move-exception;
            r2 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;
            r3 = "hide_progress_dialog";
            r4 = java.lang.Boolean.valueOf(r8);
            r2.performDialogAction(r3, r4);
            goto L_0x006d;
        L_0x00a1:
            r2 = move-exception;
            r3 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;
            r4 = "hide_progress_dialog";
            r5 = java.lang.Boolean.valueOf(r8);
            r3.performDialogAction(r4, r5);
            throw r2;
        L_0x00ae:
            r2 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;
            r3 = "hide_progress_dialog";
            r4 = java.lang.Boolean.valueOf(r8);
            r2.performDialogAction(r3, r4);
            goto L_0x006d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.hbh.honeybaked.fragment.MenuDetailsFragment.1.onMenuProductCallback(org.json.JSONObject, java.lang.Exception):void");
        }
    }

//    class ProductDetailsBGTask extends AsyncTask<String, Integer, String> {
//        String url = "";
//
//        public ProductDetailsBGTask(String url) {
//            this.url = url;
//        }
//
//        protected void onPreExecute() {
//            MenuDetailsFragment.this.performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
//            super.onPreExecute();
//        }
//
//        protected String doInBackground(String... urls) {
//            String result = "";
//            try {
//                HashMap<String, String> fpw_map = new HashMap();
//                fpw_map.put(HttpRequest.HEADER_CONTENT_TYPE, "application/json");
//                fpw_map.put("client_id", "201969E1BFD242E189FE7B6297B1B5A3");
//                fpw_map.put("REDIS", "FALSE");
//                fpw_map.put("Application", "mobile");
//                result = new JSONParser().getStringFromGetUrl(this.url, fpw_map);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return result;
//        }
//
//        /* JADX WARNING: inconsistent code. */
//        /* Code decompiled incorrectly, please refer to instructions dump. */
//        protected void onPostExecute(String r9) {
//            /*
//            r8 = this;
//            r7 = 0;
//            r4 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r9);
//            if (r4 == 0) goto L_0x0009;
//        L_0x0007:
//            if (r9 != 0) goto L_0x00a4;
//        L_0x0009:
//            r1 = new org.json.JSONObject;	 Catch:{ JSONException -> 0x0087 }
//            r1.<init>(r9);	 Catch:{ JSONException -> 0x0087 }
//            r4 = "productDetails";
//            r4 = r1.has(r4);	 Catch:{ JSONException -> 0x0087 }
//            if (r4 == 0) goto L_0x0060;
//        L_0x0016:
//            r4 = "productDetails";
//            r3 = r1.optJSONObject(r4);	 Catch:{ JSONException -> 0x0087 }
//            r4 = "productPrice";
//            r4 = r3.optDouble(r4);	 Catch:{ JSONException -> 0x0087 }
//            r2 = java.lang.Double.valueOf(r4);	 Catch:{ JSONException -> 0x0087 }
//            r4 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;	 Catch:{ JSONException -> 0x0087 }
//            r4 = r4.gridModule;	 Catch:{ JSONException -> 0x0087 }
//            r4 = r4.getP_short_desc();	 Catch:{ JSONException -> 0x0087 }
//            r4 = com.hbh.honeybaked.supportingfiles.Utility.isEmptyString(r4);	 Catch:{ JSONException -> 0x0087 }
//            if (r4 != 0) goto L_0x006c;
//        L_0x0034:
//            r4 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;	 Catch:{ JSONException -> 0x0087 }
//            r4 = r4.product_price_tv;	 Catch:{ JSONException -> 0x0087 }
//            r5 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x0087 }
//            r5.<init>();	 Catch:{ JSONException -> 0x0087 }
//            r6 = "$ ";
//            r5 = r5.append(r6);	 Catch:{ JSONException -> 0x0087 }
//            r5 = r5.append(r2);	 Catch:{ JSONException -> 0x0087 }
//            r6 = " ";
//            r5 = r5.append(r6);	 Catch:{ JSONException -> 0x0087 }
//            r6 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;	 Catch:{ JSONException -> 0x0087 }
//            r6 = r6.gridModule;	 Catch:{ JSONException -> 0x0087 }
//            r6 = r6.getP_short_desc();	 Catch:{ JSONException -> 0x0087 }
//            r5 = r5.append(r6);	 Catch:{ JSONException -> 0x0087 }
//            r5 = r5.toString();	 Catch:{ JSONException -> 0x0087 }
//            r4.setText(r5);	 Catch:{ JSONException -> 0x0087 }
//        L_0x0060:
//            r4 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;
//            r5 = "hide_progress_dialog";
//            r6 = java.lang.Boolean.valueOf(r7);
//            r4.performDialogAction(r5, r6);
//        L_0x006b:
//            return;
//        L_0x006c:
//            r4 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;	 Catch:{ JSONException -> 0x0087 }
//            r4 = r4.product_price_tv;	 Catch:{ JSONException -> 0x0087 }
//            r5 = new java.lang.StringBuilder;	 Catch:{ JSONException -> 0x0087 }
//            r5.<init>();	 Catch:{ JSONException -> 0x0087 }
//            r6 = "$ ";
//            r5 = r5.append(r6);	 Catch:{ JSONException -> 0x0087 }
//            r5 = r5.append(r2);	 Catch:{ JSONException -> 0x0087 }
//            r5 = r5.toString();	 Catch:{ JSONException -> 0x0087 }
//            r4.setText(r5);	 Catch:{ JSONException -> 0x0087 }
//            goto L_0x0060;
//        L_0x0087:
//            r0 = move-exception;
//            r0.printStackTrace();	 Catch:{ all -> 0x0097 }
//            r4 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;
//            r5 = "hide_progress_dialog";
//            r6 = java.lang.Boolean.valueOf(r7);
//            r4.performDialogAction(r5, r6);
//            goto L_0x006b;
//        L_0x0097:
//            r4 = move-exception;
//            r5 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;
//            r6 = "hide_progress_dialog";
//            r7 = java.lang.Boolean.valueOf(r7);
//            r5.performDialogAction(r6, r7);
//            throw r4;
//        L_0x00a4:
//            r4 = com.hbh.honeybaked.fragment.MenuDetailsFragment.this;
//            r5 = "hide_progress_dialog";
//            r6 = java.lang.Boolean.valueOf(r7);
//            r4.performDialogAction(r5, r6);
//            goto L_0x006b;
//            */
//            throw new UnsupportedOperationException("Method not decompiled: com.hbh.honeybaked.fragment.MenuDetailsFragment.ProductDetailsBGTask.onPostExecute(java.lang.String):void");
//        }
//    }

    public static MenuDetailsFragment newInstances(GridModule gridModule, String product_sub_catagory_id, int product_famaily_id, int product_catagory_id) {
        MenuDetailsFragment menuDetailsFragment = new MenuDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("gridModule", gridModule);
        bundle.putInt("product_sub_catagory_id", Integer.parseInt(product_sub_catagory_id));
        bundle.putInt("product_famaily_id", product_famaily_id);
        bundle.putInt("product_catagory_id", product_catagory_id);
        menuDetailsFragment.setArguments(bundle);
        return menuDetailsFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_menu, container, false);
        if (getArguments() != null) {
            this.gridModule = (GridModule) getArguments().getSerializable("gridModule");
            this.product_famaily_id = getArguments().getInt("product_famaily_id", 0);
            this.product_catagory_id = getArguments().getInt("product_catagory_id", 0);
            this.product_sub_catagory_id = getArguments().getInt("product_sub_catagory_id", 0);
        }
        this.product_title_tv = (TextView) v.findViewById(R.id.product_title_tv);
        this.note_tv = (TextView) v.findViewById(R.id.note_tv);
        this.product_price_tv = (TextView) v.findViewById(R.id.product_price_tv);
        this.product_des_tv = (TextView) v.findViewById(R.id.product_des_tv);
        this.product_img_ll = (LinearLayout) v.findViewById(R.id.product_img_ll);
        this.product_back_tr = (TableRow) v.findViewById(R.id.product_back_tr);
        this.menu_details_arrow = (ImageView) v.findViewById(R.id.menu_details_arrow);
        setColor(getResources().getColor(R.color.ham_burg_new), this.menu_details_arrow);
        this.product_back_tr.setOnClickListener(this);
        SpannableString content = new SpannableString("Note: Prices may vary by store.");
        content.setSpan(new UnderlineSpan(), 0, 5, 0);
        this.note_tv.setText(content);
        if (this.gridModule != null) {
            this.product_title_tv.setText(Html.fromHtml(this.gridModule.getsProductName()));
            this.product_des_tv.setText(Html.fromHtml(this.gridModule.getsProductDescription()));
            Utility.loadImagesToView(getActivity(), this.gridModule.getsImageUrl(), this.product_img_ll, R.drawable.launcher_icon);
            if (this.cd.isConnectingToInternet()) {
                getMenuProductDetails();
            } else {
                Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
            }
        }
        return v;
    }

    public void getMenuProductDetails() {
        performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        FBUserService.sharedInstance().getMenuProduct(new JSONObject(), this.hbha_pref_helper.getStringValue("store_id"), String.valueOf(this.product_catagory_id), String.valueOf(this.product_sub_catagory_id), String.valueOf(this.gridModule.getProductId()), new C17521());
    }

    private void setColor(int color, ImageView img) {
        if (img.getDrawable() != null) {
            img.getDrawable().setColorFilter(color, Mode.SRC_ATOP);
            img.invalidate();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_back_tr:
                this.fragmentActivityListener.performFragmentActivityAction(AppConstants.MENUPAGE, new MenuModel("MENUS", "", ""));
                return;
            default:
                return;
        }
    }

    public void performAdapterAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }
}
