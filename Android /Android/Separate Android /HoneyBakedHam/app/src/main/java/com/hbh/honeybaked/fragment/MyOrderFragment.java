package com.hbh.honeybaked.fragment;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.base.BaseFragment;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.supportingfiles.Utility;

public class MyOrderFragment extends BaseFragment {
    WebView food_web_vw;
    private boolean mShowDialog = true;
    String m_link;
    String m_title;
    Bundle menu_Bundle = null;
    LinearLayout order_Lay;

    class C17601 extends WebViewClient {
        C17601() {
        }

        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        }

        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            Builder builder = new Builder(MyOrderFragment.this.getActivity());
           // builder.setMessage(R.string.notification_error_ssl_cert_invalid);
            builder.setPositiveButton("continue", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            builder.create().show();
        }

        public boolean shouldOverrideUrlLoading(WebView view, String URL) {
            if (URL.contains("<html>")) {
                view.loadData(URL, "text/html", "UTF-8");
            } else {
                view.loadUrl(URL);
            }
            return true;
        }

        public void onPageFinished(WebView view, String URL) {
            MyOrderFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
        }
    }

    public static MyOrderFragment newInstances(MenuModel menuModel) {
        MyOrderFragment myOrderFragment = new MyOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("MenuModel", menuModel);
        myOrderFragment.setArguments(bundle);
        return myOrderFragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setFlags(8192, 8192);
        View v = inflater.inflate(R.layout.fragment_myoder, container, false);
        this.menu_Bundle = getArguments();
        if (this.menu_Bundle != null) {
            MenuModel menuModel = (MenuModel) this.menu_Bundle.getSerializable("MenuModel");
            this.m_title = menuModel.getMenu_title();
            this.m_link = menuModel.getMenu_link();
        }
        this.order_Lay = (LinearLayout) v.findViewById(R.id.order_lay);
        this.food_web_vw = (WebView) v.findViewById(R.id.food_web_vw);
        this.food_web_vw.getSettings().setJavaScriptEnabled(true);
        this.food_web_vw.getSettings().setDomStorageEnabled(true);
        this.food_web_vw.getSettings().setLoadWithOverviewMode(true);
        this.food_web_vw.getSettings().setUseWideViewPort(true);
        if (this.menu_name.equalsIgnoreCase("Reserve for Pick up")) {
            if (!Utility.isEmptyString(this.hbha_pref_helper.getStringValue("city1")) && !Utility.isEmptyString(this.hbha_pref_helper.getStringValue("store_code1"))) {
                this.m_link = String.format(this.m_link.toString(), new Object[]{this.hbha_pref_helper.getStringValue("city1").replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, "").trim().toLowerCase(), this.hbha_pref_helper.getStringValue("store_code1")});
                this.hbha_pref_helper.saveStringValue("store_code1", "");
                this.hbha_pref_helper.saveStringValue("city1", "");
            } else if (!(Utility.isEmptyString(this.hbha_pref_helper.getStringValue("city")) || Utility.isEmptyString(this.hbha_pref_helper.getStringValue("store_code")))) {
                this.m_link = String.format(this.m_link.toString(), new Object[]{this.hbha_pref_helper.getStringValue("city").replace(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, "").trim().toLowerCase(), this.hbha_pref_helper.getStringValue("store_code")});
            }
        }
        return v;
    }

    public void loadWebview(String URL) {
        this.food_web_vw.setWebViewClient(new C17601());
        if (URL.contains("<html>")) {
            this.food_web_vw.loadData(URL, "text/html", "UTF-8");
        } else {
            this.food_web_vw.loadUrl(URL);
        }
    }

    public void performAdapterAction(String tagName, Object data) {
        super.performAdapterAction(tagName, data);
    }

    public void onResume() {
        super.onResume();
        if (this.mShowDialog) {
            performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(true));
            loadWebview(this.m_link);
            this.mShowDialog = false;
        }
    }

    public void onClick(View v) {
    }
}
