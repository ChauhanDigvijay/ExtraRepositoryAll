package com.BasicApp.Activites.NonGeneric.Menus.MenusLanding;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.BasicApp.Fragments.DetailMenu_Fragment;
import com.BasicApp.Fragments.ImageMenu_Fragment.ListItemSelectedListener;
import com.BasicApp.Utils.FBUtils;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;

public class MenuActivity extends ActionBarActivity implements ListItemSelectedListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    public static ListItemSelectedListener itemListerner;
    ActionBar mActionbar;
    DetailMenu_Fragment detailsFragment;
    private Toolbar toolbar;
    private ImageLoader mImageLoader;
    private NetworkImageView background;
    private DrawerLayout drawerLayout;

    public static ListItemSelectedListener getItemListerner() {
        return itemListerner;
    }

    public static void setItemListerner(ListItemSelectedListener itemListerner) {
        MenuActivity.itemListerner = itemListerner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_bistro);

        mActionbar = getSupportActionBar();
        mActionbar.hide();
        background = (NetworkImageView) findViewById(R.id.img_Back);

        FBUtils.setUpNavigationDrawerModel(MenuActivity.this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);

        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCustomBackPressed();
            }
        });
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else
                    drawerLayout.openDrawer(GravityCompat.END);
            }

        });
        detailsFragment = new DetailMenu_Fragment();
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        if (extras != null) {
            DetailMenu_Fragment detailsFragment = new DetailMenu_Fragment();
            detailsFragment.onActivityResult(2, 2, i);

        }


    }

    @Override
    public void onStart() {
        super.onStart();
        if (FBViewMobileSettingsService.sharedInstance().checkInButtonColor != null) {
            mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
            final String url = "http://" + FBViewMobileSettingsService.sharedInstance().signUpBackgroundImageUrl;
            mImageLoader.get(url, ImageLoader.getImageListener(background, R.drawable.bgimage, android.R.drawable.ic_dialog_alert));

        }
        setActionBar();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NewApi")
    protected void setActionBar() {

        setActionBarTitle();

    }

    public void setActionBarTitle() {

        Spannable text = new SpannableString(getSupportActionBar().getTitle());
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
    }


    @Override
    public void listItemSelectedListener(int pos) {

        DetailMenu_Fragment detailsFragment = new DetailMenu_Fragment();
        detailsFragment.getItemValue(pos, this);
    }

    public void onCustomBackPressed() {
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.this.finish();
            }
        });
    }

    @Override
    public boolean onQueryTextChange(String arg0) {

        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String arg0) {

        return false;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

}
