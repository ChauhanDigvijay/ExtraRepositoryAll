package com.identity.arx.general;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import java.io.File;

public class MasterActivity extends AppCompatActivity {
    public String Name;
    public ActionBar actionBar;
    public String ins_id;
    public SharedPreferences sharedPreference;
    public Editor sharedPreferenceEdit;
    public String userName;

    protected void setActionBarTitle(String actionBarTitle) {
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);
        this.actionBar.setTitle((CharSequence) actionBarTitle);
        setSharedPrefrence();
    }

    public void setSharedPrefrence() {
        this.sharedPreference = AppSharedPrefrence.getSharedPrefrence(this);
        this.sharedPreferenceEdit = this.sharedPreference.edit();
        this.userName = this.sharedPreference.getString("USER_NAME", "");
        this.Name = this.sharedPreference.getString("NAME", "");
        this.ins_id = this.sharedPreference.getString("INTITUTE_ID", "");
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 16908332:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void clearSharedPrefrence() {
        this.sharedPreference = AppSharedPrefrence.getSharedPrefrence(this);
        this.sharedPreferenceEdit.clear();
        this.sharedPreferenceEdit.commit();
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static void deleteCache(Context context) {
        try {
            deleteDir(context.getCacheDir());
        } catch (Exception e) {
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir == null || !dir.isFile()) {
            return false;
        } else {
            return dir.delete();
        }
    }
}
