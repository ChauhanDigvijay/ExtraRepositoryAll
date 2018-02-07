package com.hbh.honeybaked.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBUserService.FBMenuCategoryCallback;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.adapter.MenusFragmentAdapter;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.module.MenuFrgmentModule;
import com.hbh.honeybaked.supportingfiles.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuFragment extends BaseFragment {
    public static HashMap<String, String> menu_api_list = new HashMap();
    MenusFragmentAdapter menu_adpter = null;
    ListView menu_list;
    ArrayList<MenuFrgmentModule> menus_fragment_list = new ArrayList();
    TextView no_data_tv;



    class C17542 implements FBMenuCategoryCallback {
        C17542() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void onMenuCategoryCallback(JSONObject r14, Exception r15) {
            /*
            r13 = this;
            if (r14 == 0) goto L_0x00f1;
        L_0x0002:
            r8 = "successFlag";
            r8 = r14.optBoolean(r8);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = 1;
            if (r8 != r9) goto L_0x00b7;
        L_0x000b:
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = r8.menu_list;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = 0;
            r8.setVisibility(r9);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = r8.no_data_tv;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = 8;
            r8.setVisibility(r9);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = "productCategoryList";
            r2 = r14.getJSONArray(r8);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r0 = 0;
        L_0x0023:
            r8 = r2.length();	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            if (r0 >= r8) goto L_0x0082;
        L_0x0029:
            r1 = r2.getJSONObject(r0);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = "productCategoryId";
            r5 = r1.optInt(r8);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = "productCategoryName";
            r6 = r1.optString(r8);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = "productCategoryDescription";
            r4 = r1.optString(r8);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r3 = 0;
            r8 = r6.trim();	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = "Over The Counter Menu";
            r8 = r8.equalsIgnoreCase(r9);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            if (r8 != 0) goto L_0x0058;
        L_0x004c:
            r8 = r6.trim();	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = "Product Menu";
            r8 = r8.equalsIgnoreCase(r9);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            if (r8 == 0) goto L_0x0072;
        L_0x0058:
            r3 = 2130837792; // 0x7f020120 float:1.7280548E38 double:1.05277375E-314;
        L_0x005b:
            r7 = new com.hbh.honeybaked.module.MenuFrgmentModule;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = "Over The Counter";
            r9 = "Product";
            r8 = r6.replace(r8, r9);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r7.<init>(r5, r8, r4, r3);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = r8.menus_fragment_list;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8.add(r7);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r0 = r0 + 1;
            goto L_0x0023;
        L_0x0072:
            r8 = r6.trim();	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = "Lunch Menu";
            r8 = r8.equalsIgnoreCase(r9);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            if (r8 == 0) goto L_0x005b;
        L_0x007e:
            r3 = 2130837741; // 0x7f0200ed float:1.7280445E38 double:1.0527737247E-314;
            goto L_0x005b;
        L_0x0082:
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = r8.getActivity();	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            if (r8 == 0) goto L_0x00aa;
        L_0x008a:
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = new com.hbh.honeybaked.adapter.MenusFragmentAdapter;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r10 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r10 = r10.getActivity();	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r11 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r12 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r12 = r12.menus_fragment_list;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9.<init>(r10, r11, r12);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8.menu_adpter = r9;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = r8.menu_list;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = r9.menu_adpter;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8.setAdapter(r9);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
        L_0x00aa:
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);
            r8.performDialogAction(r9, r10);
        L_0x00b6:
            return;
        L_0x00b7:
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8.performDialogAction(r9, r10);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = r8.menu_list;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = 8;
            r8.setVisibility(r9);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r8 = r8.no_data_tv;	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            r9 = 0;
            r8.setVisibility(r9);	 Catch:{ Exception -> 0x00d5, all -> 0x00e3 }
            goto L_0x00aa;
        L_0x00d5:
            r8 = move-exception;
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);
            r8.performDialogAction(r9, r10);
            goto L_0x00b6;
        L_0x00e3:
            r8 = move-exception;
            r9 = com.hbh.honeybaked.fragment.MenuFragment.this;
            r10 = "hide_progress_dialog";
            r11 = 0;
            r11 = java.lang.Boolean.valueOf(r11);
            r9.performDialogAction(r10, r11);
            throw r8;
        L_0x00f1:
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;
            r9 = "hide_progress_dialog";
            r10 = 0;
            r10 = java.lang.Boolean.valueOf(r10);
            r8.performDialogAction(r9, r10);
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;
            r8 = r8.getActivity();
            com.hbh.honeybaked.supportingfiles.Utility.tryHandleTokenExpiry(r8, r15);
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;
            r8 = r8.menu_list;
            r9 = 8;
            r8.setVisibility(r9);
            r8 = com.hbh.honeybaked.fragment.MenuFragment.this;
            r8 = r8.no_data_tv;
            r9 = 0;
            r8.setVisibility(r9);
            goto L_0x00b6;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.hbh.honeybaked.fragment.MenuFragment.2.onMenuCategoryCallback(org.json.JSONObject, java.lang.Exception):void");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);
        this.menu_list = (ListView) v.findViewById(R.id.menu_list);
        this.no_data_tv = (TextView) v.findViewById(R.id.no_menu_tv);
        this.hb_dbHelper.openDb();
        Cursor api_url_cur = this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_api_url_table where sub_title ='" + this.menu_name + "'", null);
        if (api_url_cur.getCount() > 0) {
            while (api_url_cur.moveToNext()) {
                String id = api_url_cur.getString(0);
                String sub_title = api_url_cur.getString(1);
                menu_api_list.put(api_url_cur.getString(2), api_url_cur.getString(3));
            }
        }
        api_url_cur.close();
        if (this.cd.isConnectingToInternet()) {
            getMenuCategory();
        } else {
            Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
            this.menu_list.setVisibility(View.GONE);
            this.no_data_tv.setText("Please try again later!");
            this.no_data_tv.setVisibility(View.GONE);
        }
        //this.menu_list.setOnItemClickListener(new C17531());
        return v;
    }

    public void getMenuCategory() {
        performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(false));
        FBUserService.sharedInstance().getMenuCategory(new JSONObject(), this.hbha_pref_helper.getStringValue("store_id"), new C17542());
    }

    public void onClick(View v) {
    }

    public void performAdapterAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }
}
