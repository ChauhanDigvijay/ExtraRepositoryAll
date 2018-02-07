package com.identity.arx.student;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.identity.arx.DrawerActivity;
import com.identity.arx.R;
import com.identity.arx.workinprogress.ContructionFragment;


public class StudentDrawerActivity extends DrawerActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_drawer);
        initData();
        getSupportFragmentManager().beginTransaction().add((int) R.id.content_drawer, new StudentHomeFragment()).commit();
        setSharedPrefrence();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment detailFragment = null;
        if (id == R.id.my_profile_faculty) {
            detailFragment = new StudentProfileFragment();
        } else if (id == R.id.home_faculty) {
            detailFragment = new StudentHomeFragment();
        } else if (id == R.id.setting_faculty) {
            detailFragment = new ContructionFragment();
        } else if (id == R.id.Aboutandhelp_faculty) {
            detailFragment = new ContructionFragment();
        } else if (id == R.id.chat) {
            detailFragment = new ContructionFragment();
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.content_drawer, detailFragment).commit();
        ((DrawerLayout) findViewById(R.id.drawer_layout)).closeDrawer((int) GravityCompat.START);
        return true;
    }
}
