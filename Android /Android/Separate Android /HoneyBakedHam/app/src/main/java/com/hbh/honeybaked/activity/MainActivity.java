package com.hbh.honeybaked.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.Preferences.FBPreferences;
import com.facebook.appevents.AppEventsConstants;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBUserService.FBLogoutCallback;
import com.hbh.honeybaked.R;
import com.hbh.honeybaked.adapter.BottomMenusAdapter;
import com.hbh.honeybaked.adapter.LeftDrawerMenuAdapter;
import com.hbh.honeybaked.applications.Application;
import com.hbh.honeybaked.base.BaseActivity;
import com.hbh.honeybaked.constants.AppConstants;
import com.hbh.honeybaked.constants.PreferenceConstants;
import com.hbh.honeybaked.dialogs.NotificationDialog;
import com.hbh.honeybaked.fragment.CustomerServiceFragment;
import com.hbh.honeybaked.fragment.HomePageFragment;
import com.hbh.honeybaked.fragment.MenuDetailsFragment;
import com.hbh.honeybaked.fragment.MenuFragment;
import com.hbh.honeybaked.fragment.MyLoyaltyFragment;
import com.hbh.honeybaked.fragment.MyOrderFragment;
import com.hbh.honeybaked.fragment.OfferFragment;
import com.hbh.honeybaked.fragment.PromoCodeFragment;
import com.hbh.honeybaked.fragment.RecipeFragmentDetails;
import com.hbh.honeybaked.fragment.StoreLocatorDeatilsFragment;
import com.hbh.honeybaked.fragment.StoreLocatorFragment;
import com.hbh.honeybaked.fragment.UserProfileFragment;
import com.hbh.honeybaked.module.GridModule;
import com.hbh.honeybaked.module.MenuModel;
import com.hbh.honeybaked.supportingfiles.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {
    LeftDrawerMenuAdapter adapter;
    boolean back_boolean = false;
    ArrayList<MenuModel> buttom_menu = new ArrayList();
    LinearLayout content_frame;
    String currenTag = "";
    String currentTag = "";
    ArrayList<MenuModel> dataList = new ArrayList();
    String detailsTag = "";
    int[] follow_img = new int[]{R.drawable.fb_icon, R.drawable.instagram_icon, R.drawable.instagram_icon, R.drawable.pin_icon};
    HashMap<String, List<GridModule>> grid_child_data = new HashMap();
    List<String> grid_header_data = new ArrayList();
    int groupPosition = 0;
    private BottomMenusAdapter hlvAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    MenuModel mMenuModel = null;
    HashMap<String, String> map = new HashMap();
    String notificationType;
    boolean redirect_flag = false;
    String sOferUrl;
    ArrayList<HashMap<String, String>> store_list = new ArrayList();
    String store_searchvalue;

    class C17024 implements FBLogoutCallback {
        C17024() {
        }

        public void onLogoutCallback(JSONObject jsonObject, Exception e) {
            MainActivity.this.setProgressDialog(AppConstants.HIDE_PROGRESS_DIALOG, Boolean.valueOf(false));
            MainActivity.this.hbha_pref_helper.saveIntValue("login_flag", 0);
            MainActivity.this.head_user_set_img_vw.setVisibility(View.GONE);
            MainActivity.this.hb_dbHelper.openDb();
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_fnm", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_lnm", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_mail", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_pw", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_dob", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_ph_no", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_conf_pw", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_conf_ph_no", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_store_id", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_store_nm", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_store_add", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_store_city", "");
            MainActivity.this.hbha_pref_helper.saveStringValue("reg_store_ph", "");
            MainActivity.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LASTLOGINTIME, "");
            MainActivity.this.hbha_pref_helper.saveBooleanValue("last_reg_ph_no_flg", false);
            MainActivity.this.hbha_pref_helper.saveIntValue("rule_id", 0);
            MainActivity.this.hbha_pref_helper.saveBooleanValue("reward_rule", false);
            MainActivity.this.hbha_pref_helper.saveBooleanValue("reg_email_option_check", false);
            MainActivity.this.hbha_pref_helper.saveBooleanValue("reg_privacy_terms_check", false);
            MainActivity.this.hb_dbHelper.deleteTable("hbha_offer_table");
            MainActivity.this.hb_dbHelper.deleteTable("hbha_reward_table");
            MainActivity.this.hbha_pref_helper.saveStringValue(PreferenceConstants.PREFERENCE_LASTLOGINTIME, "");
            MainActivity.this.hbha_pref_helper.saveBooleanValue(PreferenceConstants.PREFERENCE_SIGNUP_FIRSTTIME, false);
            FBPreferences.sharedInstance(MainActivity.this).delete("user_id_forapp");
            MainActivity.this.startActivity(new Intent(MainActivity.this, LoginMainActivity.class));
            MainActivity.this.finish();
        }
    }

    private class DrawerItemClickListener implements OnItemClickListener {
        private DrawerItemClickListener() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        initViews();
        this.hb_dbHelper.openDb();
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerList = (ListView) findViewById(R.id.left_drawer);
        this.content_frame = (LinearLayout) findViewById(R.id.content_frameq);
        this.mDrawerToggle = new ActionBarDrawerToggle(this, this.mDrawerLayout, R.drawable.launcher_icon, 0, 0) {
            @SuppressLint({"NewApi"})
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (VERSION.SDK_INT < 11) {
                    return;
                }
                if (MainActivity.this.mDrawerLayout.isDrawerVisible(3)) {
                    MainActivity.this.content_frame.setTranslationX(((float) MainActivity.this.mDrawerList.getWidth()) * slideOffset);
                } else {
                    MainActivity.this.content_frame.setTranslationX(Float.valueOf("-" + (((float) MainActivity.this.mDrawerList.getWidth()) * slideOffset)).floatValue());
                }
            }

            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        this.dataList.clear();
        this.dataList.add(new MenuModel("Home", "", ""));
        Cursor cursor = this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_menu_table", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                this.dataList.add(new MenuModel(cursor.getString(1), cursor.getString(3), cursor.getString(4)));
            }
        }
        cursor.close();
        this.adapter = new LeftDrawerMenuAdapter(this, R.layout.bottom_menu_layout, this.dataList);
        this.mDrawerList.setAdapter(this.adapter);
        this.mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        this.mDrawerLayout.setDrawerListener(this.mDrawerToggle);
        Intent push_in = getIntent();
        if (push_in != null) {
            this.sOferUrl = push_in.getStringExtra("offerUrl");
            this.notificationType = push_in.getStringExtra("notificationType");
        }
        if (Utility.isEmptyString(this.notificationType) || this.notificationType.equalsIgnoreCase("me")) {
            performFragmentActivityAction(AppConstants.HOME_PAGE, Boolean.valueOf(true));
        } else if (this.notificationType.equalsIgnoreCase("pb")) {
            this.currentTag = AppConstants.HOME_PAGE;
            performFragmentActivityAction(AppConstants.LOYALTY_PAGE, new MenuModel("My Loyalty", "", ""));
        } else if (!this.notificationType.equalsIgnoreCase("re")) {
            performFragmentActivityAction(AppConstants.HOME_PAGE, Boolean.valueOf(true));
        } else if (Utility.isEmptyString(this.sOferUrl)) {
            performFragmentActivityAction(AppConstants.HOME_PAGE, Boolean.valueOf(true));
        } else {
            this.currentTag = AppConstants.HOME_PAGE;
            performFragmentActivityAction(AppConstants.SHOPONLINE, new MenuModel("Offers & Rewards", "", this.sOferUrl));
        }
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.mDrawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (this.mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return false;
    }

    public void performAdapterAction(String tagName, Object data) {
        this.currentTag = tagName;
        this.currenTag = tagName;
        clearStoredata();
        closeDrawer();
        this.back_boolean = false;
        this.hbha_pref_helper.saveIntValue(PreferenceConstants.PREFERENCE_REWARDS_OFFERS_PAGE, 0);
        this.header_bottom_layout.setVisibility(View.VISIBLE);
        if (tagName.equals(AppConstants.LOGOUT_TEXT)) {
            shoDialog(AppConstants.LOGOUT_TEXT, ((Boolean) data).booleanValue());
        } else if (tagName.equals(AppConstants.HOME_PAGE)) {
            goHome(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.STORE_MAIN_PAGE)) {
            goStore(data, true);
        } else if (tagName.equalsIgnoreCase(AppConstants.LOYALTY_PAGE)) {
            goLoyalty(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.SHOPONLINE)) {
            this.hbha_pref_helper.saveStringValue("store_flag", AppEventsConstants.EVENT_PARAM_VALUE_NO);
            if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue("store"))) {
                Utility.showToast(this, "Please select store .");
            } else {
                shopOnline(data);
            }
        } else if (tagName.equalsIgnoreCase(AppConstants.CONTACT_US_PAGE)) {
            goCustomer(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.MYOFFER_PAGE)) {
            goOffer(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.RECIPE_MAIN_PAGE)) {
           // goRecipe(data);
        } else if (!tagName.equalsIgnoreCase(AppConstants.MENUPAGE)) {
        } else {
            if (Utility.isEmptyString(this.hbha_pref_helper.getStringValue("store"))) {
                Utility.showToast(this, "Please select store .");
            } else {
                goMenu(data);
            }
        }
    }

    public void performFragmentActivityAction(String tagName, Object data) {
        closeDrawer();
        if (tagName.equalsIgnoreCase(AppConstants.SET_HEADING)) {
            setHeading((String) data);
        } else {
            this.currenTag = tagName;
        }
        if (tagName.equals(AppConstants.HOME_PAGE)) {
            goHome(data);
        } else if (tagName.equals(AppConstants.STORE_MAIN_PAGE) || tagName.equals(AppConstants.STORE_MAIN_PAGE1)) {
            goStore(data, tagName.equals(AppConstants.STORE_MAIN_PAGE));
        } else if (tagName.equalsIgnoreCase(AppConstants.STORE_DETAILS_PAGE)) {
            this.map = (HashMap) data;
            goStoreDatils(data);
        } else if (tagName.equals(AppConstants.SHOPONLINE)) {
            if (data == null) {
                return;
            }
            if (((MenuModel) data).getMenu_title().equalsIgnoreCase("Reserve For Pick Up")) {
                Cursor cursor = this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_menu_table where sub_title='Reserve For Pick Up'", null);
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        shopOnline(new MenuModel(cursor.getString(1), cursor.getString(3), cursor.getString(4)));
                    }
                }
                cursor.close();
                return;
            }
            shopOnline(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.MYOFFER_PAGE)) {
            goOffer(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.RECIPE_MAIN_PAGE)) {
            //goRecipe(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.MENUPAGE)) {
            goMenu(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.LOYALTY_PAGE)) {
            goLoyalty(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.CONTACT_US_PAGE)) {
            clearStoredata();
            goCustomer(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.USER_PROFILE_PAGE)) {
            goUserProfile(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.RECIPE_DETAIL_PAGE)) {
            goRecipeDetail();
        } else if (tagName.equalsIgnoreCase(AppConstants.MENUPAGE_DETAILS)) {
            goMenuDetails(data);
            this.detailsTag = tagName;
        } else if (tagName.equalsIgnoreCase(AppConstants.MENUPAGE_DETAIL)) {
          //  goMenuDetail(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.STORE_MAIN_SAVE)) {
            Object[] values = (Object[]) data;
            this.store_list = (ArrayList) values[0];
            this.store_searchvalue = String.valueOf(values[1]);
        } else if (tagName.equalsIgnoreCase(AppConstants.SHOW_PROMOCODE)) {
            goPromoCode(data);
        }
    }

    public void performDialogAction(String tagName, Object data) {
        if (tagName.equals(AppConstants.SHOPONLINE)) {
            this.currenTag = tagName;
            this.currentTag = AppConstants.HOME_PAGE;
            shopOnline(data);
        } else if (tagName.equalsIgnoreCase(AppConstants.LOYALTY_PAGE)) {
            this.currenTag = tagName;
            this.currentTag = AppConstants.HOME_PAGE;
            goLoyalty(data);
        } else {
            super.performDialogAction(tagName, data);
        }
    }

    private void closeDrawer() {
        if (this.mDrawerLayout != null && this.mDrawerLayout.isDrawerVisible(8388611)) {
            this.mDrawerLayout.closeDrawers();
        }
    }

    private void clearGriddata() {
        if (this.grid_header_data.size() > 0 || this.grid_child_data.size() > 0) {
            this.grid_child_data.clear();
            this.grid_header_data.clear();
            this.groupPosition = 0;
        }
    }

    private void clearStoredata() {
        if (this.store_list.size() > 0) {
            this.store_list.clear();
            this.store_searchvalue = "";
            this.detailsTag = "";
        }
    }

    private void goPromoCode(Object data) {
        if (data != null) {
            MenuModel menuModel = (MenuModel) data;
            setMenuModel(menuModel);
            PromoCodeFragment promoCodeFragment = PromoCodeFragment.newInstances(menuModel);
            if (this.back_boolean) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, promoCodeFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, promoCodeFragment).commit();
            }
            setHeading("MY COUPON");
            this.back_boolean = false;
        }
    }

    private void goMenuDetails(Object data) {
        if (data != null) {
            Object[] dataTOPass = (Object[]) data;
            Object[] dataToPass1 = (Object[]) dataTOPass[0];
            GridModule gridModule = (GridModule) dataToPass1[1];
            String product_sub_catagory_id = ((String) dataToPass1[0]).split("######")[0];
            this.grid_header_data = (List) dataToPass1[3];
            this.grid_child_data = (HashMap) dataToPass1[4];
            this.groupPosition = ((Integer) dataToPass1[5]).intValue();
            MenuDetailsFragment menuDetailsFragment = MenuDetailsFragment.newInstances(gridModule, product_sub_catagory_id, ((Integer) dataTOPass[1]).intValue(), ((Integer) dataTOPass[2]).intValue());
            if (this.back_boolean) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, menuDetailsFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, menuDetailsFragment).commit();
            }
            this.back_boolean = false;
        }
    }

//    private void goMenuDetail(Object data) {
//        GridRecipesFragment gridRecipesFragment = null;
//        if (!this.detailsTag.equalsIgnoreCase("")) {
//            gridRecipesFragment = GridRecipesFragment.newInstances(0, 0, (ArrayList) this.grid_header_data, this.grid_child_data, this.groupPosition);
//        } else if (data != null) {
//            Object[] objects = (Object[]) data;
//            gridRecipesFragment = GridRecipesFragment.newInstances(((Integer) objects[0]).intValue(), ((Integer) objects[1]).intValue(), (ArrayList) this.grid_header_data, this.grid_child_data, this.groupPosition);
//        }
//        if (this.back_boolean) {
//            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, gridRecipesFragment).commit();
//        } else {
//            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, gridRecipesFragment).commit();
//        }
//        setHeading(this.hbha_pref_helper.getStringValue(PreferenceConstants.PREFERENCE_MENUS_NAME));
//        this.detailsTag = "";
//        this.back_boolean = false;
//    }

    private void goStoreDatils(Object data) {
        if (data != null) {
            this.redirect_flag = true;
            Bundle bundle = new Bundle();
            bundle.putSerializable("bundle", (Serializable) data);
            StoreLocatorDeatilsFragment storeLocatorDeatilsFragment = new StoreLocatorDeatilsFragment();
            storeLocatorDeatilsFragment.setArguments(bundle);
            if (this.back_boolean) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, storeLocatorDeatilsFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, storeLocatorDeatilsFragment).commit();
            }
            setHeading("Store Details");
            this.detailsTag = AppConstants.STORE_DETAILS_PAGE;
            this.back_boolean = false;
        }
    }

    private void goUserProfile(Object data) {
        this.header_bottom_layout.setVisibility(View.GONE);
        this.header_bottom_right_tv.setVisibility(View.VISIBLE);
        this.header_bottom_title_tv.setText("MY PROFILE");
        if (data != null) {
            MenuModel menuModel = (MenuModel) data;
            setMenuModel(menuModel);
            UserProfileFragment userProfileFragment = new UserProfileFragment();
            if (this.back_boolean) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, userProfileFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, userProfileFragment).commit();
            }
            setHeading(menuModel.getMenu_title());
            setBottom(menuModel.getMenu_title());
            this.back_boolean = false;
        }
    }

    private void goRecipeDetail() {
        RecipeFragmentDetails recipeFragmentDetails = new RecipeFragmentDetails();
        if (this.back_boolean) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, recipeFragmentDetails).commit();
        } else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, recipeFragmentDetails).commit();
        }
        setHeading("RECIPES");
        this.back_boolean = false;
    }

    private void goMenu(Object data) {
        if (data != null) {
            clearGriddata();
            MenuModel menuModel = (MenuModel) data;
            setMenuModel(menuModel);
            Bundle bundle = new Bundle();
            bundle.putSerializable("MenuModel", menuModel);
            MenuFragment menuFragment = new MenuFragment();
            menuFragment.setArguments(bundle);
            if (this.back_boolean) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, menuFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, menuFragment).commit();
            }
            setHeading(menuModel.getMenu_title());
            setBottom(menuModel.getMenu_title());
            this.back_boolean = false;
        }
    }

//    private void goRecipe(Object data) {
//        if (data != null) {
//            MenuModel menuModel = (MenuModel) data;
//            setMenuModel(menuModel);
//            RecipeFragment recipeFragment = RecipeFragment.newInstances(menuModel.getValue());
//            if (this.back_boolean) {
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, recipeFragment).commit();
//            } else {
//                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, recipeFragment).commit();
//            }
//            setHeading(menuModel.getMenu_title());
//            setBottom(menuModel.getMenu_title());
//            this.back_boolean = false;
//        }
//    }

    private void goCustomer(Object data) {
        if (data != null) {
            MenuModel menuModel = (MenuModel) data;
            setMenuModel(menuModel);
            CustomerServiceFragment customerServiceFragment = new CustomerServiceFragment();
            if (this.back_boolean) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, customerServiceFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, customerServiceFragment).commit();
            }
            setHeading(menuModel.getMenu_title());
            setBottom(menuModel.getMenu_title());
            this.back_boolean = false;
        }
    }

    private void goLoyalty(Object data) {
        if (data != null) {
            MenuModel menuModel = (MenuModel) data;
            setMenuModel(menuModel);
            MyLoyaltyFragment myLoyaltyFragment = new MyLoyaltyFragment();
            if (this.back_boolean) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, myLoyaltyFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, myLoyaltyFragment).commit();
            }
            setHeading(menuModel.getMenu_title());
            setBottom(menuModel.getMenu_title());
            this.back_boolean = false;
        }
    }

    private void goOffer(Object data) {
        if (data != null) {
            MenuModel menuModel = (MenuModel) data;
            setMenuModel(menuModel);
            OfferFragment offerFragment = new OfferFragment();
            if (this.back_boolean) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, offerFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, offerFragment).commit();
            }
            setHeading("MY REWARDS & OFFERS");
            setBottom(menuModel.getMenu_title());
            this.back_boolean = false;
        }
    }

    private void shopOnline(Object data) {
        if (!this.cd.isConnectingToInternet()) {
            Utility.showToast(this, AppConstants.NO_CONNECTION_TEXT);
        } else if (data != null) {
            MenuModel menuModel = (MenuModel) data;
            setMenuModel(menuModel);
            MyOrderFragment myOrderFragment = MyOrderFragment.newInstances(menuModel);
            if (this.back_boolean) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, myOrderFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, myOrderFragment).commit();
            }
            setHeading(menuModel.getMenu_title());
            setBottom(menuModel.getMenu_title());
            this.back_boolean = false;
        }
    }

    private void goStore(Object data, boolean isSearchEnabled) {
        if (data != null) {
            this.redirect_flag = false;
            MenuModel menuModel = (MenuModel) data;
            setMenuModel(menuModel);
            ArrayList<HashMap<String, String>> store_list1 = new ArrayList();
            store_list1.addAll(this.store_list);
            StoreLocatorFragment storeLocatorFragment = StoreLocatorFragment.newInstances(store_list1, this.store_searchvalue, isSearchEnabled);
            if (this.back_boolean) {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, storeLocatorFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, storeLocatorFragment).commit();
            }
            setHeading(menuModel.getMenu_title());
            setBottom(menuModel.getMenu_title().equalsIgnoreCase("Store Locator") ? "My Store" : menuModel.getMenu_title());
            if (!this.detailsTag.equalsIgnoreCase("") && this.currentTag.equalsIgnoreCase(AppConstants.STORE_MAIN_PAGE)) {
                this.detailsTag = "";
                clearStoredata();
            }
            this.back_boolean = false;
        }
    }

    private void goHome(Object data) {
        setBottom("Home");
        this.head_user_set_img_vw.setVisibility(View.VISIBLE);
        HomePageFragment homePageFragment = new HomePageFragment();
        if (((Boolean) data).booleanValue()) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).replace(R.id.login_main, homePageFragment).commit();
        } else {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).replace(R.id.login_main, homePageFragment).commit();
        }
        clearStoredata();
        this.back_boolean = false;
    }

    private void setMenuModel(MenuModel menuModel) {
        if (this.currentTag.equalsIgnoreCase(this.currenTag)) {
            this.mMenuModel = menuModel;
        }
    }

    public void onBackPressed() {
        if (this.mDrawerLayout.isDrawerVisible(8388611)) {
            this.mDrawerLayout.closeDrawers();
        } else if ((this.currenTag == AppConstants.HOME_PAGE && this.currentTag == AppConstants.HOME_PAGE) || (this.currenTag == AppConstants.HOME_PAGE && this.currentTag == AppConstants.SET_HEADER_VIEW)) {
            shoDialog(AppConstants.EXIT_TEXT, false);
        } else {
            goBack();
        }
    }

    private void setHeading(String menu_title) {
        this.header_bottom_layout.setVisibility(View.GONE);
        this.header_bottom_title_tv.setText(menu_title);
        if (this.currenTag.equalsIgnoreCase(AppConstants.USER_PROFILE_PAGE) || this.currenTag.equalsIgnoreCase(AppConstants.RECIPE_MAIN_PAGE)) {
            this.header_bottom_right_tv.setVisibility(View.GONE);
            if (this.currenTag.equalsIgnoreCase(AppConstants.USER_PROFILE_PAGE)) {
                this.head_user_set_img_vw.setVisibility(View.GONE);
                this.header_bottom_right_tv.setText("Log Out >");
                return;
            }
            this.head_user_set_img_vw.setVisibility(View.VISIBLE);
            this.header_bottom_right_tv.setText("View More Recipes >");
            return;
        }
        this.header_bottom_right_tv.setVisibility(View.VISIBLE);
        this.head_user_set_img_vw.setVisibility(View.VISIBLE);
    }

    private void setBottom(String name) {
        this.buttom_menu.clear();
        this.buttom_menu.add(new MenuModel("Home", "", ""));
        Cursor cursor = this.hb_dbHelper.getStringQuery("SELECT * FROM hbha_menu_table where title !='" + name + "' and title !='Contact Us'", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                this.buttom_menu.add(new MenuModel(cursor.getString(1), cursor.getString(3), cursor.getString(4)));
            }
        }
        cursor.close();
        if (this.currenTag.equalsIgnoreCase(AppConstants.HOME_PAGE)) {
            this.bottom_layout.setVisibility(View.GONE);
            this.home_folw_txt.setVisibility(View.GONE);
            this.bottom_layout.setBackgroundColor(getResources().getColor(R.color.row_color));
            this.hlvAdapter = new BottomMenusAdapter((Context) this, this.follow_img, true);
         //   this.hlv.setAdapter(this.hlvAdapter);
            return;
        }
        this.bottom_layout.setVisibility(View.GONE);
        this.bottom_layout.setBackgroundColor(getResources().getColor(R.color.white));
        this.home_folw_txt.setVisibility(View.GONE);
        this.hlvAdapter = new BottomMenusAdapter((Context) this, this.buttom_menu, false);
        //this.hlv.setAdapter(this.hlvAdapter);
        this.hlvAdapter.notifyDataSetChanged();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_bottom_left_img_vw:
                goBack();
                return;
            case R.id.header_bottom_right_tv:
                if (this.header_bottom_right_tv.getText().equals("Log Out >")) {
                    shoDialog(AppConstants.LOGOUT_TEXT, true);
                    return;
                } else {
                    performFragmentActivityAction(AppConstants.RECIPE_DETAIL_PAGE, Boolean.valueOf(false));
                    return;
                }
            case R.id.head_nav_img_vw:
                if (this.mDrawerLayout.isDrawerVisible(GravityCompat.START)) {
                    this.mDrawerLayout.closeDrawer(Gravity.START);
                    return;
                } else {
                    this.mDrawerLayout.openDrawer(Gravity.START);
                    return;
                }
            case R.id.head_img_tv:
                performAdapterAction(AppConstants.HOME_PAGE, Boolean.valueOf(false));
                return;
            case R.id.head_user_set_img_vw:
                if (this.currenTag.equalsIgnoreCase(AppConstants.USER_PROFILE_PAGE) || this.currenTag.equalsIgnoreCase(AppConstants.UPDATE_PROFILE_PAGE)) {
                    Fragment showPopFragment = getSupportFragmentManager().findFragmentById(R.id.login_main);
                    if (showPopFragment instanceof UserProfileFragment) {
                        UserProfileFragment showPopFragment2 = (UserProfileFragment) showPopFragment;
                        return;
                    }
                    return;
                }
                performFragmentActivityAction(AppConstants.USER_PROFILE_PAGE, new MenuModel("My Profile", "", ""));
                return;
            default:
                return;
        }
    }

    private void goBack() {
        this.back_boolean = true;
        this.header_bottom_layout.setVisibility(View.VISIBLE);
        if (this.currentTag == "" || this.currentTag == this.currenTag) {
            performFragmentActivityAction(AppConstants.HOME_PAGE, Boolean.valueOf(false));
            this.currentTag = AppConstants.HOME_PAGE;
        } else if (this.currentTag.equalsIgnoreCase(AppConstants.HOME_PAGE) || this.currenTag.equalsIgnoreCase(AppConstants.UPDATE_PROFILE_PAGE) || this.currenTag.equalsIgnoreCase(AppConstants.USER_PROFILE_PAGE)) {
            performFragmentActivityAction(AppConstants.HOME_PAGE, Boolean.valueOf(false));
            this.currentTag = AppConstants.HOME_PAGE;
        } else if (this.currentTag.equalsIgnoreCase(AppConstants.STORE_MAIN_PAGE) && this.currenTag.equalsIgnoreCase(AppConstants.SHOPONLINE)) {
            if (this.redirect_flag) {
                performFragmentActivityAction(AppConstants.STORE_DETAILS_PAGE, this.map);
            } else {
                performFragmentActivityAction(this.currentTag, this.mMenuModel);
            }
        } else if (this.currentTag.equalsIgnoreCase(AppConstants.MENUPAGE) && this.currenTag.equalsIgnoreCase(AppConstants.MENUPAGE_DETAILS)) {
            performFragmentActivityAction(AppConstants.MENUPAGE_DETAIL, this.mMenuModel);
        } else if (this.currentTag.equalsIgnoreCase(AppConstants.CONTACT_US_PAGE) && this.currenTag.equalsIgnoreCase(AppConstants.STORE_DETAILS_PAGE)) {
            performFragmentActivityAction(AppConstants.STORE_MAIN_PAGE, new MenuModel("My Store", "", ""));
        } else {
            performFragmentActivityAction(this.currentTag, this.mMenuModel);
        }
    }

    private void shoDialog(String content, final boolean logout) {
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(1);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.logout_alert_dialog_layout);
        Button fpw_cancel = (Button) dialog.findViewById(R.id.fpw_cancel);
        Button fpw_ok = (Button) dialog.findViewById(R.id.fpw_ok);
        ((TextView) dialog.findViewById(R.id.head_text)).setText(content);
        fpw_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (logout) {
                    MainActivity.this.logoutMember();
                    dialog.dismiss();
                    return;
                }
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");

                //intent.setFlags(VCardConfig.FLAG_APPEND_TYPE_PARAM);
                intent.putExtra("EXIT", true);
                MainActivity.this.startActivity(intent);
                dialog.dismiss();
            }
        });
        fpw_cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void logoutMember() {
        try {
            JSONObject logout_object = new JSONObject();
            logout_object.put("Application", "mobile");
            FBUserService.sharedInstance().logout(logout_object, new C17024());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showNotificationAlert(String sDialogType, String sDialogContent, String sUrl) {
        NotificationDialog.newInstance(sDialogType, sDialogContent, sUrl, this).show(getSupportFragmentManager(), "notificationDialog");
    }

    protected void onResume() {
        super.onResume();
        if (this.myApplication != null) {
            Application application = this.myApplication;
            Application.activityResumed();
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.myApplication != null) {
            Application application = this.myApplication;
            Application.activityPaused();
        }
    }
}
