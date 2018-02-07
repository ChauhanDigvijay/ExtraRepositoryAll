package com.identity.arx.faculty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;

import com.identity.arx.R;
import com.identity.arx.general.MasterActivity;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class weeklyScheduleLectureActivity extends MasterActivity implements MaterialTabListener {
    ViewPagerAdapter adapter;
    ViewPager pager;
    MaterialTabHost tabHost;

    class C08121 extends SimpleOnPageChangeListener {
        C08121() {
        }

        public void onPageSelected(int position) {
            weeklyScheduleLectureActivity.this.tabHost.setSelectedNavigationItem(position);
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int num) {
            String selectDay = "NA";
            switch (num) {
                case 0:
                    selectDay = "Mon";
                    break;
                case 1:
                    selectDay = "Tues";
                    break;
                case 2:
                    selectDay = "wed";
                    break;
                case 3:
                    selectDay = "Thurs";
                    break;
                case 4:
                    selectDay = "Fri";
                    break;
                case 5:
                    selectDay = "Sat";
                    break;
                case 6:
                    selectDay = "Sun";
                    break;
            }
            return PlaceholderFragment.newInstance(selectDay);
        }

        public int getCount() {
            return 7;
        }

        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Mon";
                case 1:
                    return "Tues";
                case 2:
                    return "wed";
                case 3:
                    return "Thurs";
                case 4:
                    return "Fri";
                case 5:
                    return "Sat";
                case 6:
                    return "Sun";
                default:
                    return null;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_weekly_lectures);
        setActionBarTitle("Weekly Lecture Schedule");
        this.tabHost = (MaterialTabHost) findViewById(R.id.tabHost);
        this.pager = (ViewPager) findViewById(R.id.pager);
        this.adapter = new ViewPagerAdapter(getSupportFragmentManager());
        this.pager.setAdapter(this.adapter);
        this.pager.setOnPageChangeListener(new C08121());
        for (int i = 0; i < this.adapter.getCount(); i++) {
            this.tabHost.addTab(this.tabHost.newTab().setText(this.adapter.getPageTitle(i)).setTabListener(this));
        }
    }

    public void onTabSelected(MaterialTab tab) {
        this.pager.setCurrentItem(tab.getPosition());
    }

    public void onTabReselected(MaterialTab tab) {
    }

    public void onTabUnselected(MaterialTab tab) {
    }
}
