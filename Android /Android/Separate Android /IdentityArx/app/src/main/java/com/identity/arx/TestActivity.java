package com.identity.arx;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.MaterialViewPager.Listener;
import com.github.florent37.materialviewpager.header.HeaderDesign;

public class TestActivity extends AppCompatActivity {
    MaterialViewPager mViewPager;

    class C07632 implements Listener {
        C07632() {
        }

        public HeaderDesign getHeaderDesign(int page) {
            switch (page) {
                case 0:
                    return HeaderDesign.fromColorResAndUrl(R.color.green, "http://phandroid.s3.amazonaws.com/wp-content/uploads/2014/06/android_google_moutain_google_now_1920x1080_wallpaper_Wallpaper-HD_2560x1600_www.paperhi.com_-640x400.jpg");
                case 1:
                    return HeaderDesign.fromColorResAndUrl(R.color.blue, "http://www.hdiphonewallpapers.us/phone-wallpapers/540x960-1/540x960-mobile-wallpapers-hd-2218x5ox3.jpg");
                case 2:
                    return HeaderDesign.fromColorResAndUrl(R.color.cyan, "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                case 3:
                    return HeaderDesign.fromColorResAndUrl(R.color.red, "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                default:
                    return null;
            }
        }
    }

    class C07643 implements OnClickListener {
        C07643() {
        }

        public void onClick(View v) {
            TestActivity.this.mViewPager.notifyHeaderChanged();
            Toast.makeText(TestActivity.this.getApplicationContext(), "Yes, the title is clickable", 0).show();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_test);
        this.mViewPager = (MaterialViewPager) findViewById(R.id.materialViewPager);
        Toolbar toolbar = this.mViewPager.getToolbar();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setTitle((CharSequence) "");
            }
        }
        this.mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            public Fragment getItem(int position) {
                int i = position % 2;
                return RecyclerViewFragment.newInstance();
            }

            public int getCount() {
                return 2;
            }

            public CharSequence getPageTitle(int position) {
                switch (position % 2) {
                    case 0:
                        return "Attendence";
                    case 1:
                        return "Directories";
                    case 2:
                        return "Professionnel";
                    case 3:
                        return "Divertissement";
                    default:
                        return "";
                }
            }
        });
        this.mViewPager.setMaterialViewPagerListener(new C07632());
        this.mViewPager.getViewPager().setOffscreenPageLimit(this.mViewPager.getViewPager().getAdapter().getCount());
        this.mViewPager.getPagerTitleStrip().setViewPager(this.mViewPager.getViewPager());
        View logo = findViewById(R.id.logo_white);
        if (logo != null) {
            logo.setOnClickListener(new C07643());
        }
    }
}
