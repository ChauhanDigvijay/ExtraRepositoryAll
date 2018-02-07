package com.identity.arx;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.identity.arx.db.IdentityAxsOpenHelper;
import com.identity.arx.general.MasterActivity;


public class DrawerActivity extends MasterActivity implements OnNavigationItemSelectedListener {
    IdentityAxsOpenHelper db1;
    String[] tableList = new String[]{IdentityAxsOpenHelper.USER_TABLE, IdentityAxsOpenHelper.COURSE_MASTER_TABLE, IdentityAxsOpenHelper.INSTITUTE_TABLE, "lecture_schedule_table"};

    class C07301 implements OnClickListener {
        C07301() {
        }

        public void onClick(DialogInterface arg0, int arg1) {
            Toast.makeText(DrawerActivity.this, "Great Choice", 1).show();
        }
    }

    class C07312 implements OnClickListener {
        C07312() {
        }

        public void onClick(DialogInterface dialog, int which) {
            DrawerActivity.this.clearSharedPrefrence();
            Intent intnt = new Intent(DrawerActivity.this.getApplicationContext(), SplashActivity.class);
            intnt.setFlags(1409318912);
            DrawerActivity.this.db1 = new IdentityAxsOpenHelper(DrawerActivity.this.getApplicationContext());
            for (String tableName : DrawerActivity.this.tableList) {
                DrawerActivity.this.db1.deleteRecordsFromTable(tableName, null);
            }
            DrawerActivity.this.startActivity(intnt);
            DrawerActivity.this.finish();
        }
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen((int) GravityCompat.START)) {
            drawer.closeDrawer((int) GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void initData() {
        setContentView((int) R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSharedPrefrence();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.name);
        ((TextView) header.findViewById(R.id.username)).setText(this.userName);
        name.setText(this.Name);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.action_settings) {
            return super.onOptionsItemSelected(item);
        }
        Builder alertDialogBuilder = new Builder(this);
        alertDialogBuilder.setMessage((CharSequence) "Are you sure, You want to Logout");
        alertDialogBuilder.setPositiveButton((CharSequence) "No", new C07301());
        alertDialogBuilder.setNegativeButton((CharSequence) "Yes", new C07312());
        alertDialogBuilder.create().show();
        return true;
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }
}
