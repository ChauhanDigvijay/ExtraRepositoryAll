//package com.hbh.honeybaked.fragment;
//
//import android.annotation.TargetApi;
//import android.app.AlertDialog.Builder;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnDismissListener;
//import android.content.Intent;
//import android.content.res.AssetManager;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.net.http.SslError;
//import android.os.Build.VERSION;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.text.Html;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.view.WindowManager.LayoutParams;
//import android.webkit.SslErrorHandler;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import com.facebook.CallbackManager;
//import com.facebook.CallbackManager.Factory;
//import com.facebook.FacebookCallback;
//import com.facebook.FacebookException;
//import com.facebook.appevents.AppEventsConstants;
//import com.facebook.login.LoginManager;
//import com.facebook.login.LoginResult;
//import com.facebook.login.widget.LoginButton;
//import com.facebook.share.ShareApi;
//import com.facebook.share.model.ShareLinkContent;
//import com.facebook.share.model.SharePhoto;
//import com.facebook.share.model.SharePhotoContent;
//import com.facebook.share.widget.ShareDialog;
//import com.facebook.share.widget.ShareDialog.Mode;
//import com.google.firebase.analytics.FirebaseAnalytics.Param;
//import com.hbh.honeybaked.R;
//import com.hbh.honeybaked.base.BaseFragment;
//import com.hbh.honeybaked.constants.AppConstants;
//import com.hbh.honeybaked.supportingfiles.Utility;
//import com.pinterest.android.pdk.PDKBoard;
//import com.pinterest.android.pdk.PDKCallback;
//import com.pinterest.android.pdk.PDKClient;
//import com.pinterest.android.pdk.PDKException;
//import com.pinterest.android.pdk.PDKResponse;
//import com.pinterest.android.pdk.Utils;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Calendar;
//import java.util.Iterator;
//import java.util.List;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//public class RecipeFragment extends BaseFragment {
//    private static final String BOARD_FIELDS = "id,name,description,creator,image,counts,created_at";
//    int EMAIL_RESPONSE_CODE = 101;
//    String YOUR_APP_ID = "4885783653747016729";
//    List<PDKBoard> _boardList = new ArrayList();
//    private boolean _loading = false;
//    ArrayAdapter<String> adapter = null;
//    String boardId = "";
//    String boardNm = "";
//    Calendar cal = Calendar.getInstance();
//    private CallbackManager callbackManager;
//    ImageView e_share_btn;
//    boolean fb_login = false;
//    ImageView fb_share_btn;
//    String final_month = "";
//    int final_pdf = 0;
//    String final_url = "";
//    ArrayList<String> id_list = new ArrayList();
//    InputStream is = null;
//    WebView load_recipe;
//    LoginButton loginButton;
//    private LoginManager loginManager;
//    private PDKCallback myBoardsCallback;
//    private PDKResponse myBoardsResponse;
//    ArrayList<String> name_list = new ArrayList();
//    ImageView p_share_btn;
//    String[] pdf_arr = new String[]{"http://www.honeybaked.com/recipes/pdf/honeybaked-ham-recipe-smokey-turkey-tortilla-soup.pdf", "http://www.honeybaked.com/recipes/pdf/honeybaked-ham-recipe-stuffed-peppers.pdf", "http://www.honeybaked.com/recipes/pdf/honeybaked-ham-recipe-honeybaked-rigatoni-tomatoes-feta-cheese.pdf", "http://www.honeybaked.com/recipes/pdf/honeybaked-ham-recipe-asparagus-tart.pdf", "http://www.honeybaked.com/recipes/pdf/honeybaked-ham-recipe-turkey-mozzarella-sandwich.pdf", "http://www.honeybaked.com/recipes/pdf/honeybaked-ham-recipe-ham-ravioli-salad-with-vegetables.pdf", "http://www.honeybaked.com/recipes/pdf/honeybaked-ham-recipe-crustless-ham-cheese-picnic-pie.pdf"};
//    String[] pdf_img_arr = new String[]{"https://s3-us-west-1.amazonaws.com/fbjamba/hbh/ImageLibrary/recipe/1-jan-recipes-chicken-tortilla.jpg", "https://s3-us-west-1.amazonaws.com/fbjamba/hbh/ImageLibrary/recipe/2-feb-recipes-stuffed-peppers.jpg", "https://s3-us-west-1.amazonaws.com/fbjamba/hbh/ImageLibrary/recipe/3-mar-recipes-rigatoni.jpg", "https://s3-us-west-1.amazonaws.com/fbjamba/hbh/ImageLibrary/recipe/4-apr-recipes-asparagus-tart.jpg", "https://s3-us-west-1.amazonaws.com/fbjamba/hbh/ImageLibrary/recipe/5-may-recipes-caprese.jpg", "https://s3-us-west-1.amazonaws.com/fbjamba/hbh/ImageLibrary/recipe/6-jun-recipes-ham-and-pasta-salad.jpg", "https://s3-us-west-1.amazonaws.com/fbjamba/hbh/ImageLibrary/recipe/7-july-recipes-picnic-pie.jpg"};
//    private PDKClient pdkClient;
//    Dialog pinDialog = null;
//    String recipe_header = "";
//    TextView recipe_not_found;
//    int share_btn_flag = 0;
//    int show_pin_alert = 0;
//    String str = null;
//    int value = -1;
//
//    class C17641 extends PDKCallback {
//        C17641() {
//        }
//
//        public void onSuccess(PDKResponse response) {
//            RecipeFragment.this._loading = false;
//            RecipeFragment.this.myBoardsResponse = response;
//            RecipeFragment.this._boardList.clear();
//            RecipeFragment.this._boardList.addAll(response.getBoardList());
//            if (RecipeFragment.this.show_pin_alert == 0) {
//                RecipeFragment.this.showPinAlert();
//                return;
//            }
//            RecipeFragment.this.name_list.clear();
//            RecipeFragment.this.id_list.clear();
//            RecipeFragment.this.name_list.add("- Select board -");
//            RecipeFragment.this.id_list.add(AppEventsConstants.EVENT_PARAM_VALUE_NO);
//            for (int b = 0; b < RecipeFragment.this._boardList.size(); b++) {
//                PDKBoard boardItem = (PDKBoard) RecipeFragment.this._boardList.get(b);
//                RecipeFragment.this.name_list.add(boardItem.getName().toString());
//                RecipeFragment.this.id_list.add(boardItem.getUid().toString());
//            }
//            RecipeFragment.this.adapter = new ArrayAdapter(RecipeFragment.this.getActivity(), 17367048, RecipeFragment.this.name_list);
//        }
//
//        public void onFailure(PDKException exception) {
//            RecipeFragment.this._loading = false;
//            Log.e(getClass().getName(), exception.getDetailMessage());
//        }
//    }
//
//    class C17652 implements OnItemSelectedListener {
//        C17652() {
//        }
//
//        public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
//            RecipeFragment.this.boardNm = (String) RecipeFragment.this.name_list.get(arg2);
//            RecipeFragment.this.boardId = (String) RecipeFragment.this.id_list.get(arg2);
//        }
//
//        public void onNothingSelected(AdapterView<?> adapterView) {
//            RecipeFragment.this.boardNm = (String) RecipeFragment.this.name_list.get(0);
//            RecipeFragment.this.boardId = (String) RecipeFragment.this.id_list.get(0);
//        }
//    }
//
//    class C17693 implements OnClickListener {
//        C17693() {
//        }
//
//        public void onClick(View v) {
//            RecipeFragment.this.show_pin_alert = 1;
//            final Dialog board_dialog = new Dialog(RecipeFragment.this.getActivity());
//            board_dialog.getWindow().requestFeature(1);
//            board_dialog.setContentView(R.layout.create_board);
//            board_dialog.setCanceledOnTouchOutside(false);
//            board_dialog.setCancelable(false);
//            LayoutParams lp = new LayoutParams();
//            lp.copyFrom(board_dialog.getWindow().getAttributes());
//            lp.width = -1;
//            lp.height = -2;
//            lp.gravity = 17;
//            board_dialog.getWindow().setAttributes(lp);
//            Button pin_board_create_bt = (Button) board_dialog.findViewById(R.id.pin_board_create_bt);
//            final EditText pin_board_et = (EditText) board_dialog.findViewById(R.id.pin_board_et);
//            final EditText pin_board_des_et = (EditText) board_dialog.findViewById(R.id.pin_board_des_et);
//            ((Button) board_dialog.findViewById(R.id.pin_board_cancel_bt)).setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    board_dialog.dismiss();
//                }
//            });
//            pin_board_create_bt.setOnClickListener(new OnClickListener() {
//
//                class C17671 extends PDKCallback {
//                    C17671() {
//                    }
//
//                    public void onSuccess(PDKResponse response) {
//                        Log.d(getClass().getName(), response.getData().toString());
//                        board_dialog.dismiss();
//                        Utility.showToast(RecipeFragment.this.getActivity(), "Board Created");
//                        PDKClient.getInstance().getMyBoards(RecipeFragment.BOARD_FIELDS, RecipeFragment.this.myBoardsCallback);
//                    }
//
//                    public void onFailure(PDKException exception) {
//                        Log.e(getClass().getName(), exception.getDetailMessage());
//                    }
//                }
//
//                public void onClick(View v) {
//                    String bName = pin_board_et.getText().toString();
//                    if (Utils.isEmpty(bName)) {
//                        Utility.showToast(RecipeFragment.this.getActivity(), "Board name cannot be empty");
//                    } else {
//                        PDKClient.getInstance().createBoard(bName, pin_board_des_et.getText().toString(), new C17671());
//                    }
//                }
//            });
//            board_dialog.show();
//        }
//    }
//
//    class C17715 implements OnClickListener {
//        C17715() {
//        }
//
//        public void onClick(View v) {
//            RecipeFragment.this.show_pin_alert = 0;
//            RecipeFragment.this.pinDialog.dismiss();
//        }
//    }
//
//    class C17726 extends PDKCallback {
//        C17726() {
//        }
//
//        public void onSuccess(PDKResponse response) {
//            Log.d(getClass().getName(), response.getData().toString());
//            RecipeFragment.this.pinDialog.dismiss();
//            Utility.showToast(RecipeFragment.this.getActivity(), "Successfully Pinned");
//        }
//
//        public void onFailure(PDKException exception) {
//            Log.e(getClass().getName(), exception.getDetailMessage());
//        }
//    }
//
//    class C17757 extends WebViewClient {
//        C17757() {
//        }
//
//        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//        }
//
//        public boolean shouldOverrideUrlLoading(WebView view, String URL) {
//            view.loadUrl(URL);
//            return true;
//        }
//
//        public void onPageFinished(WebView view, String URL) {
//            RecipeFragment.this.performDialogAction(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
//        }
//
//        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
//            Builder builder = new Builder(RecipeFragment.this.getActivity());
//            builder.setMessage(R.string.notification_error_ssl_cert_invalid);
//            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    handler.proceed();
//                }
//            });
//            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    handler.cancel();
//                }
//            });
//            builder.create().show();
//        }
//    }
//
//    class C17768 implements FacebookCallback<LoginResult> {
//        C17768() {
//        }
//
//        public void onSuccess(LoginResult loginResult) {
//            RecipeFragment.this.hbha_pref_helper.saveBooleanValue("fb_login_flag", true);
//            RecipeFragment.this.sharePDFLinkToFacebook();
//        }
//
//        public void onCancel() {
//            System.out.println("onCancel");
//        }
//
//        public void onError(FacebookException exception) {
//            System.out.println("onError");
//        }
//    }
//
//    class C17779 extends PDKCallback {
//        C17779() {
//        }
//
//        public void onSuccess(PDKResponse response) {
//            Log.e(getClass().getName(), response.getData().toString());
//            RecipeFragment.this.hbha_pref_helper.saveBooleanValue("pin_login_flag", true);
//            RecipeFragment.this.show_pin_alert = 1;
//            PDKClient.getInstance().getMyBoards(RecipeFragment.BOARD_FIELDS, RecipeFragment.this.myBoardsCallback);
//            RecipeFragment.this.showPinAlert();
//        }
//
//        public void onFailure(PDKException exception) {
//            Log.e(getClass().getName(), exception.getDetailMessage());
//        }
//    }
//
//    public static RecipeFragment newInstances(int value) {
//        RecipeFragment recipeFragment = new RecipeFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(Param.VALUE, value);
//        recipeFragment.setArguments(bundle);
//        return recipeFragment;
//    }
//
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_recipes, container, false);
//        this.load_recipe = (WebView) v.findViewById(R.id.load_recipe);
//        this.recipe_not_found = (TextView) v.findViewById(R.id.recipe_not_found);
//        this.fb_share_btn = (ImageView) v.findViewById(R.id.fb_share_btn);
//        this.p_share_btn = (ImageView) v.findViewById(R.id.p_share_btn);
//        this.e_share_btn = (ImageView) v.findViewById(R.id.e_share_btn);
//        this.fb_share_btn.setOnClickListener(this);
//        this.p_share_btn.setOnClickListener(this);
//        this.e_share_btn.setOnClickListener(this);
//        this.pdkClient = PDKClient.configureInstance(getActivity(), this.YOUR_APP_ID);
//        this.pdkClient.onConnect(getActivity());
//        PDKClient pDKClient = this.pdkClient;
//        PDKClient.setDebugMode(true);
//        if (getArguments() != null) {
//            int i;
//            if (getArguments().getInt(Param.VALUE) == -1) {
//                i = this.cal.get(2);
//            } else {
//                i = getArguments().getInt(Param.VALUE);
//            }
//            this.value = i;
//        }
//        this.myBoardsCallback = new C17641();
//        this._loading = true;
//        switch (this.value) {
//            case 0:
//                this.final_pdf = 0;
//                this.final_month = "January";
//                setValue("file:///android_asset/html/January.html");
//                break;
//            case 1:
//                this.final_pdf = 1;
//                this.final_month = "February";
//                setValue("file:///android_asset/html/February.html");
//                break;
//            case 2:
//                this.final_pdf = 2;
//                this.final_month = "March";
//                setValue("file:///android_asset/html/March.html");
//                break;
//            case 3:
//                this.final_pdf = 3;
//                this.final_month = "April";
//                setValue("file:///android_asset/html/April.html");
//                break;
//            case 4:
//                this.final_pdf = 4;
//                this.final_month = "May";
//                setValue("file:///android_asset/html/May.html");
//                break;
//            case 5:
//                this.final_pdf = 5;
//                this.final_month = "June";
//                setValue("file:///android_asset/html/June.html");
//                break;
//            case 6:
//                this.final_pdf = 6;
//                this.final_month = "July";
//                setValue("file:///android_asset/html/July.html");
//                break;
//            default:
//                this.load_recipe.setVisibility(8);
//                this.recipe_not_found.setVisibility(0);
//                break;
//        }
//        htmlParse();
//        return v;
//    }
//
//    private void showPinAlert() {
//        String board_id = "";
//        this.pinDialog = new Dialog(getActivity());
//        this.pinDialog.getWindow().requestFeature(1);
//        this.pinDialog.setContentView(R.layout.custom_pinit_layout);
//        this.pinDialog.setCanceledOnTouchOutside(false);
//        this.pinDialog.setCancelable(false);
//        final EditText pin_et = (EditText) this.pinDialog.findViewById(R.id.pin_et);
//        ImageView pin_alert_close = (ImageView) this.pinDialog.findViewById(R.id.pin_alert_close);
//        Button pin_create_bt = (Button) this.pinDialog.findViewById(R.id.pin_create_bt);
//        Button pin_share_bt = (Button) this.pinDialog.findViewById(R.id.pin_share_bt);
//        Spinner pin_spinner = (Spinner) this.pinDialog.findViewById(R.id.pin_spinner);
//        Utility.loadImagesToView(getActivity(), this.pdf_img_arr[this.final_pdf], (ImageView) this.pinDialog.findViewById(R.id.pin_img), R.drawable.launcher_icon);
//        pin_et.setText(this.final_month + "'s Recipe: " + this.recipe_header);
//        this.name_list.clear();
//        this.id_list.clear();
//        this.name_list.add("- Select board -");
//        this.id_list.add(AppEventsConstants.EVENT_PARAM_VALUE_NO);
//        for (int b = 0; b < this._boardList.size(); b++) {
//            PDKBoard boardItem = (PDKBoard) this._boardList.get(b);
//            this.name_list.add(boardItem.getName().toString());
//            this.id_list.add(boardItem.getUid().toString());
//        }
//        this.adapter = new ArrayAdapter(getActivity(), 17367048, this.name_list);
//        pin_spinner.setAdapter(this.adapter);
//        this.adapter.notifyDataSetChanged();
//        this.adapter.setDropDownViewResource(17367049);
//        pin_spinner.setOnItemSelectedListener(new C17652());
//        pin_create_bt.setOnClickListener(new C17693());
//        pin_share_bt.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                RecipeFragment.this.show_pin_alert = 0;
//                if (RecipeFragment.this.boardNm.equalsIgnoreCase("- Select board -")) {
//                    if (RecipeFragment.this._boardList.size() == 0) {
//                        Utility.showToast(RecipeFragment.this.getActivity(), "Please create a board");
//                    } else {
//                        Utility.showToast(RecipeFragment.this.getActivity(), "Please select the board to pin");
//                    }
//                } else if (pin_et.getText().toString().equalsIgnoreCase("")) {
//                    Utility.showToast(RecipeFragment.this.getActivity(), "Please enter the description of the recipe");
//                } else {
//                    RecipeFragment.this.pinDialog.dismiss();
//                    RecipeFragment.this.shareImageLinkToPinterest(pin_et.getText().toString());
//                }
//            }
//        });
//        pin_alert_close.setOnClickListener(new C17715());
//        this.pinDialog.show();
//    }
//
//    private void shareImageLinkToPinterest(String title1) {
//        PDKClient.getInstance().createPin(title1, this.boardId, this.pdf_img_arr[this.final_pdf], this.pdf_arr[this.final_pdf], new C17726());
//    }
//
//    private void sharePDFLinkToFacebook() {
//        new ShareDialog(getActivity()).show(((ShareLinkContent.Builder) new ShareLinkContent.Builder().setImageUrl(Uri.parse(this.pdf_img_arr[this.final_pdf])).setContentTitle(this.recipe_header).setContentDescription("Receipe of the " + this.final_month).setContentUrl(Uri.parse(this.pdf_arr[this.final_pdf]))).build(), Mode.AUTOMATIC);
//    }
//
//    private void sharePhotoToFacebook() {
//        ShareApi.share(new SharePhotoContent.Builder().addPhoto(new SharePhoto.Builder().setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ham2)).setCaption("Malavika shared a picture4").build()).build(), null);
//    }
//
//    public void onActivityResult(int requestCode, int responseCode, Intent data) {
//        super.onActivityResult(requestCode, responseCode, data);
//        if (requestCode == this.EMAIL_RESPONSE_CODE) {
//            deleteEmailFile();
//        } else if (this.share_btn_flag == 1) {
//            this.callbackManager.onActivityResult(requestCode, responseCode, data);
//        } else if (this.share_btn_flag == 2) {
//            PDKClient.getInstance().onOauthResponse(requestCode, responseCode, data);
//        }
//    }
//
//    private void setValue(String url) {
//        performDialogAction(AppConstants.SHOW_PROGRESS_DIALOG, Boolean.valueOf(true));
//        this.load_recipe.setWebViewClient(new C17757());
//        this.final_url = url;
//        deleteEmailFile();
//        this.load_recipe.loadUrl(url);
//    }
//
//    public void performAdapterAction(String tagName, Object data) {
//        super.performAdapterAction(tagName, data);
//    }
//
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.fb_share_btn:
//                if (this.cd.isConnectingToInternet()) {
//                    this.share_btn_flag = 1;
//                    if (this.hbha_pref_helper.getBooleanValue("fb_login_flag")) {
//                        sharePDFLinkToFacebook();
//                        return;
//                    }
//                    this.callbackManager = Factory.create();
//                    List<String> permissionNeeds = Arrays.asList(new String[]{"publish_actions"});
//                    this.loginManager = LoginManager.getInstance();
//                    this.loginManager.logInWithPublishPermissions(this, permissionNeeds);
//                    this.loginManager.registerCallback(this.callbackManager, new C17768());
//                    return;
//                }
//                Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
//                return;
//            case R.id.p_share_btn:
//                if (this.cd.isConnectingToInternet()) {
//                    this.share_btn_flag = 2;
//                    this.show_pin_alert = 0;
//                    if (this.hbha_pref_helper.getBooleanValue("pin_login_flag")) {
//                        PDKClient.getInstance().getMyBoards(BOARD_FIELDS, this.myBoardsCallback);
//                        return;
//                    }
//                    List scopes = new ArrayList();
//                    scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PUBLIC);
//                    scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PUBLIC);
//                    scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_RELATIONSHIPS);
//                    scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_RELATIONSHIPS);
//                    scopes.add(PDKClient.PDKCLIENT_PERMISSION_READ_PRIVATE);
//                    scopes.add(PDKClient.PDKCLIENT_PERMISSION_WRITE_PRIVATE);
//                    this.pdkClient.login(getActivity(), scopes, new C17779());
//                    return;
//                }
//                Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
//                return;
//            case R.id.e_share_btn:
//                if (!this.cd.isConnectingToInternet()) {
//                    Utility.showToast(getActivity(), AppConstants.NO_CONNECTION_TEXT);
//                    return;
//                } else if (VERSION.SDK_INT < 23) {
//                    checkSDCard();
//                    return;
//                } else if (ActivityCompat.checkSelfPermission(getActivity(), "android.permission.READ_EXTERNAL_STORAGE") == 0 && ActivityCompat.checkSelfPermission(getActivity(), "android.permission.READ_EXTERNAL_STORAGE") == 0) {
//                    checkSDCard();
//                    return;
//                } else if (shouldShowRequestPermissionRationale("android.permission.READ_EXTERNAL_STORAGE") && shouldShowRequestPermissionRationale("android.permission.WRITE_EXTERNAL_STORAGE")) {
//                    Builder builder = new Builder(getActivity());
//                    builder.setTitle("This app needs storage access");
//                    builder.setMessage("Go to Settings and enable the storage permission");
//                    builder.setPositiveButton(17039370, null);
//                    builder.setOnDismissListener(new OnDismissListener() {
//                        @TargetApi(23)
//                        public void onDismiss(DialogInterface dialogInterface) {
//                            RecipeFragment.this.requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 3);
//                        }
//                    });
//                    builder.show();
//                    return;
//                } else {
//                    requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 3);
//                    return;
//                }
//            default:
//                return;
//        }
//    }
//
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode != 3) {
//            return;
//        }
//        if (grantResults[0] == 0) {
//            checkSDCard();
//            return;
//        }
//        Builder builder = new Builder(getActivity());
//        builder.setTitle("Functionality limited");
//        builder.setMessage("Since storage access has not been granted, this app will not be able to send mail.");
//        builder.setPositiveButton(17039370, null);
//        builder.setOnDismissListener(new OnDismissListener() {
//            public void onDismiss(DialogInterface dialog) {
//            }
//        });
//        builder.show();
//    }
//
//    public void htmlParse() {
//        try {
//            this.is = getActivity().getAssets().open("html/" + this.final_month + ".html");
//            byte[] buffer = new byte[this.is.available()];
//            this.is.read(buffer);
//            this.is.close();
//            this.str = new String(buffer);
//            this.str = this.str.replace("old string", "new string");
//            Document html = Jsoup.parse(this.str);
//            Elements elements = html.select("body").first().children();
//            this.str = "";
//            Iterator it = elements.iterator();
//            while (it.hasNext()) {
//                this.str += "\n" + String.valueOf((Element) it.next());
//            }
//            this.recipe_header = html.body().getElementsByTag("h1").text();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void checkSDCard() {
//        if (Boolean.valueOf(Environment.getExternalStorageState().equals("mounted")).booleanValue()) {
//            CopyAssets();
//        }
//    }
//
//    private void CopyAssets() {
//        Exception e;
//        AssetManager assetManager = getActivity().getAssets();
//        String[] files = null;
//        try {
//            files = assetManager.list("html");
//        } catch (IOException e2) {
//            Log.e("tag", e2.getMessage());
//        }
//        for (String filename : files) {
//            if (filename.equalsIgnoreCase(this.final_month + ".html")) {
//                try {
//                    InputStream in = assetManager.open("html/" + this.final_month + ".html");
//                    File filelocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filename);
//                    if (filelocation.exists()) {
//                        sendMailWithFile(filelocation);
//                    } else {
//                        OutputStream out = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" + filename);
//                        try {
//                            copyFile(in, out);
//                            in.close();
//                            out.flush();
//                            out.close();
//                            sendMailWithFile(filelocation);
//                        } catch (Exception e3) {
//                            e = e3;
//                            OutputStream outputStream = out;
//                            Log.e("tag", e.getMessage());
//                        }
//                    }
//                } catch (Exception e4) {
//                    e = e4;
//                }
//            }
//        }
//    }
//
//    private void copyFile(InputStream in, OutputStream out) throws IOException {
//        byte[] buffer = new byte[1024];
//        while (true) {
//            int read = in.read(buffer);
//            if (read != -1) {
//                out.write(buffer, 0, read);
//            } else {
//                return;
//            }
//        }
//    }
//
//    public void sendMailWithFile(File file_path) {
//        Uri path = Uri.fromFile(file_path);
//        Intent emailIntent = new Intent("android.intent.action.SENDTO", Uri.fromParts("mailto", "", null));
//        emailIntent.putExtra("android.intent.extra.SUBJECT", this.final_month + "'s Recipe: " + this.recipe_header);
//        emailIntent.putExtra("android.intent.extra.TEXT", Html.fromHtml(this.str));
//        emailIntent.putExtra("android.intent.extra.STREAM", path);
//        startActivityForResult(Intent.createChooser(emailIntent, "Send email..."), this.EMAIL_RESPONSE_CODE);
//    }
//
//    public void deleteEmailFile() {
//        File file_path1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), this.final_month + ".html");
//        if (file_path1.exists()) {
//            file_path1.delete();
//        }
//    }
//}
