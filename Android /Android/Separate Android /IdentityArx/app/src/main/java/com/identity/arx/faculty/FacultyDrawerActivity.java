package com.identity.arx.faculty;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

import com.identity.arx.R;
import com.identity.arx.DrawerActivity;
import com.identity.arx.db.IdentityAxsOpenHelper;
import com.identity.arx.workinprogress.ContructionFragment;


public class FacultyDrawerActivity extends DrawerActivity {
    IdentityAxsOpenHelper db1;
    String[] tableList = new String[]{IdentityAxsOpenHelper.USER_TABLE, IdentityAxsOpenHelper.COURSE_MASTER_TABLE, IdentityAxsOpenHelper.INSTITUTE_TABLE, "lecture_schedule_table"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_drawer);
        initData();
        getSupportFragmentManager().beginTransaction().add((int) R.id.content_drawer, new FacultyHomeFragment()).commit();
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Fragment detailFragment = null;
        if (id == R.id.my_profile_faculty) {
            detailFragment = new ProfileFragment();
        } else if (id == R.id.home_faculty) {
            detailFragment = new FacultyHomeFragment();
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
