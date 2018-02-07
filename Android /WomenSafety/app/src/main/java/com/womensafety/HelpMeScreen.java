package com.womensafety;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.womensafety.Common.ContactDA;
import com.womensafety.Common.LocationUtility;
import com.womensafety.Common.LocationUtils;
import com.womensafety.Common.NetworkUtility;
import com.womensafety.Common.Preference;
import com.womensafety.constant.Constant;
import com.womensafety.object.ContactDO;
import com.womensafety.object.HelperDO;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class HelpMeScreen extends BaseActivity {
    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    ArrayList<ContactDO> arrList;
    int currentpage = 0;
    private ArrayList<HelperDO> arrayList;
    private LinearLayout llHelpMe;
    private LocationUtility locationUtility;
    private ViewPager pager;

    public void initialize() {
        this.llHelpMe = (LinearLayout) this.inflater.inflate(R.layout.helpme, null);
        this.llBody.addView(this.llHelpMe, -1, -1);
        this.pager = (ViewPager) this.llHelpMe.findViewById(R.id.pager);
        this.locationUtility = new LocationUtility(this);
        this.pager.setAdapter(new HelpAdapter(getSupportFragmentManager()));
    }

    public void onBackPressed() {
        if (this.currentpage == 5) {
            showDialog("Alert !", "Are you sure you want to exit?", "Yes", "No", 100, true);
        } else if (this.currentpage == 3 || this.currentpage == 1 || this.currentpage == 2 || this.currentpage == 4) {
            setPage(0);
        } else {
            showDialog("Alert !", "Are you sure you want to exit?", "Yes", "No", 100, true);
        }
    }

    public void onFButotnClicked(int TYPE) {
        super.onFButotnClicked(TYPE);
        if (TYPE == 100) {
            finish();
        }
    }

    public void performNext() {
        if (this.arrayList == null || this.arrayList.size() <= 0) {
            showToast("No Data Found.");
        } else {
            sortList(this.arrayList);
            Intent intent = new Intent(this, TrackHelperScreen.class);
            intent.putExtra("arrayList", this.arrayList);
            startActivity(intent);
        }
        hideLoader();
    }

    public void skipNow() {
        setPage(6);
        hideLoader();
    }

    private void getLocation() {
        if (NetworkUtility.isGPSEnable(this)) {
            showLoader("Fetiching Location...");
            this.locationUtility.getLocation(new C11471());
        }
    }

    private void getHelperList() {
        showLoader("Loading...");
        Constant.URL = String.format(Constant.URL, new Object[]{Double.valueOf(Constant.longitude), Double.valueOf(Constant.latitude)});
        this.arrayList = new ArrayList();
        if (NetworkUtility.isNetworkConnectionAvailable(this)) {
            new ServiceReuest().execute(new String[]{Constant.URL});
            return;
        }
        showToast("Internet connection is not available, Please check your network settings.");
    }

    public void sortList(ArrayList<HelperDO> arrayList) {
        Collections.sort(arrayList, new C06922());
    }

    public void onStop() {
        super.onStop();
        this.locationUtility.stopGpsLocUpdation();
    }

    private int getRandomNum() {
        return new Random().nextInt(7) + 1;
    }

    public void performInfo() {
        setPage(1);
    }

    public void performHelp() {
        Builder alert = new Builder(this);
        alert.setTitle("Alert !");
        alert.setMessage("Are you sure you want help?");
        alert.setPositiveButton("Yes", new C06933());
        alert.setNegativeButton("No", new C06944());
        alert.create().show();
    }

    public void performSignUp(String fname, String lname, String cell, String pin, String code) {
        hideKeyBoard(this.pager);
        if (TextUtils.isEmpty(fname)) {
            showToast("Please enter first name.");
        } else if (TextUtils.isEmpty(lname)) {
            showToast("Please enter last name.");
        } else if (TextUtils.isEmpty(cell)) {
            showToast("Please enter cellphone number.");
        } else if (TextUtils.isEmpty(pin)) {
            showToast("Please enter PIN number.");
        } else if (TextUtils.isEmpty(code)) {
            showToast("Please enter Pin-code.");
        } else {
            this.preference.saveString(Preference.USER_NAME, fname + " " + lname);
            this.preference.saveString(Preference.CELL, cell);
            this.preference.saveString(Preference.PIN, pin);
            this.preference.saveString(Preference.CODE, code);
            this.preference.commit();
            setPage(4);
        }
    }

    public void performLogin(String cell, String pin) {
        hideKeyBoard(this.pager);
        if (TextUtils.isEmpty(cell)) {
            showToast("Please enter cellphone number.");
        } else if (TextUtils.isEmpty(pin)) {
            showToast("Please enter PIN number.");
        } else {
            this.preference.saveString(Preference.CELL, cell);
            this.preference.saveString(Preference.PIN, pin);
            this.preference.commit();
            setPage(5);
        }
    }

    public void performRegister() {
        checkPermissions();
    }

    public void performLogin() {
        setPage(3);
    }

    private void setPage(int page) {
        this.currentpage = page;
        if (this.pager != null) {
            this.pager.setCurrentItem(page, true);
        }
    }

    private void checkPermissions() {
        try {
            if (VERSION.SDK_INT >= 23) {
                checkforPermissions();
            } else {
                loadContact();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkforPermissions() {
        int permission1 = ContextCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS");
        if (permission1 == 0) {
            loadContact();
        } else {
            requestAppPermission(permission1);
        }
    }

    private void requestAppPermission(int p1) {
        Toast.makeText(this, "Please allow all the permissions to continue.", 0).show();
        ArrayList<String> arrList = new ArrayList();
        if (p1 != 0) {
            arrList.add("android.permission.READ_CONTACTS");
        }
        String[] permissons = new String[arrList.size()];
        for (int i = 0; i < arrList.size(); i++) {
            permissons[i] = (String) arrList.get(i);
        }
        requestPermissions(permissons, 123);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 123) {
            checkforPermissions();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void loadContact() {
        showLoader("Fetching Contacts...");
        new Thread(new C06965()).start();
    }

    class C06922 implements Comparator<HelperDO> {
        C06922() {
        }

        public int compare(HelperDO object1, HelperDO object2) {
            return Double.compare(object1.distance, object2.distance);
        }
    }

    class C06933 implements OnClickListener {
        C06933() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            HelpMeScreen.this.getLocation();
        }
    }

    class C06944 implements OnClickListener {
        C06944() {
        }

        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    }

    class C06965 implements Runnable {

        C06965() {
        }

        public void run() {
            HelpMeScreen.this.arrList = new ContactDA().readContacts(HelpMeScreen.this);
            HelpMeScreen.this.runOnUiThread(new C06951());
        }

        class C06951 implements Runnable {
            C06951() {
            }

            public void run() {
                HelpMeScreen.this.hideLoader();
                HelpMeScreen.this.setPage(2);
            }
        }
    }

    public class ServiceReuest extends AsyncTask<String, Void, Void> {
        private String finalResult = "";

        protected void onPreExecute() {
            HelpMeScreen.this.showLoader("Please wait...");
        }

        protected Void doInBackground(String... urls) {
            if (Constant.arrayList == null) {
                Constant.arrayList = new ArrayList();
            }
            try {
                this.finalResult = requestContent(urls[0]);
                Gson gson = new Gson();
                if (!TextUtils.isEmpty(this.finalResult)) {
                    JSONArray jsonArray = new JSONObject(this.finalResult).getJSONArray("GetLocationResult");
                    if (jsonArray != null && jsonArray.length() > 0) {
                        int index = HelpMeScreen.this.getRandomNum();
                        if (index > jsonArray.length()) {
                            index = jsonArray.length();
                        }
                        for (int i = 0; i < index; i++) {
                            HelperDO helperDO = (HelperDO) gson.fromJson(jsonArray.getJSONObject(i).toString(), HelperDO.class);
                            helperDO.distance = (double) LocationUtils.getDist(Constant.latitude, Constant.longitude, helperDO.Latitude, helperDO.Longitude);
                            if (TextUtils.isEmpty(helperDO.helperName)) {
                                if (i < Constant.helperNames.length) {
                                    helperDO.helperName = Constant.helperNames[i];
                                } else {
                                    helperDO.helperName = Constant.helperNames[0];
                                }
                            }
                            HelpMeScreen.this.arrayList.add(helperDO);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                HelpMeScreen.this.hideLoader();
                HelpMeScreen.this.showToast("Network problem, please try again.");
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            HelpMeScreen.this.performNext();
        }

        public String requestContent(String url) {
            String result = null;
            InputStream instream = null;
            try {
                HttpEntity entity = new DefaultHttpClient().execute(new HttpGet(url)).getEntity();
                if (entity != null) {
                    instream = entity.getContent();
                    result = convertStreamToString(instream);
                }
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (Exception e) {
                        HelpMeScreen.this.hideLoader();
                        HelpMeScreen.this.showToast("Network problem, please try again.");
                    }
                }
            } catch (Exception e2) {
                HelpMeScreen.this.hideLoader();
                HelpMeScreen.this.showToast("Network problem, please try again.");
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (Exception e3) {
                        HelpMeScreen.this.hideLoader();
                        HelpMeScreen.this.showToast("Network problem, please try again.");
                    }
                }
            } catch (Throwable th) {
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (Exception e4) {
                        HelpMeScreen.this.hideLoader();
                        HelpMeScreen.this.showToast("Network problem, please try again.");
                    }
                }
            }
            return result;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public String convertStreamToString(InputStream r6) {
            /*
            r5 = this;
            r1 = new java.io.BufferedReader;
            r3 = new java.io.InputStreamReader;
            r3.<init>(r6);
            r1.<init>(r3);
            r2 = new java.lang.StringBuilder;
            r2.<init>();
            r0 = 0;
        L_0x0010:
            r0 = r1.readLine();	 Catch:{ IOException -> 0x002d, all -> 0x003c }
            if (r0 == 0) goto L_0x0036;
        L_0x0016:
            r3 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x002d, all -> 0x003c }
            r3.<init>();	 Catch:{ IOException -> 0x002d, all -> 0x003c }
            r3 = r3.append(r0);	 Catch:{ IOException -> 0x002d, all -> 0x003c }
            r4 = "\n";
            r3 = r3.append(r4);	 Catch:{ IOException -> 0x002d, all -> 0x003c }
            r3 = r3.toString();	 Catch:{ IOException -> 0x002d, all -> 0x003c }
            r2.append(r3);	 Catch:{ IOException -> 0x002d, all -> 0x003c }
            goto L_0x0010;
        L_0x002d:
            r3 = move-exception;
            r6.close();	 Catch:{ IOException -> 0x0041 }
        L_0x0031:
            r3 = r2.toString();
            return r3;
        L_0x0036:
            r6.close();	 Catch:{ IOException -> 0x003a }
            goto L_0x0031;
        L_0x003a:
            r3 = move-exception;
            goto L_0x0031;
        L_0x003c:
            r3 = move-exception;
            r6.close();	 Catch:{ IOException -> 0x0043 }
        L_0x0040:
            throw r3;
        L_0x0041:
            r3 = move-exception;
            goto L_0x0031;
        L_0x0043:
            r4 = move-exception;
            goto L_0x0040;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.womansafety.app.HelpMeScreen.ServiceReuest.convertStreamToString(java.io.InputStream):java.lang.String");
        }
    }

    class C11471 implements LocationUtility.LocationResult {
        C11471() {
        }

        public void gotLocation(final Location loc) {
            HelpMeScreen.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (loc != null) {
                        HelpMeScreen.this.locationUtility.stopGpsLocUpdation();
                        DecimalFormat df = new DecimalFormat("#.#######");
                        Constant.latitude = Double.parseDouble(df.format(loc.getLatitude()));
                        Constant.longitude = Double.parseDouble(df.format(loc.getLongitude()));
                        HelpMeScreen.this.getHelperList();
                        return;
                    }
                    HelpMeScreen.this.showToast("Unable to get your current location, Please check your location settings.");
                    HelpMeScreen.this.hideLoader();
                }
            });
        }
    }

    public class HelpAdapter extends FragmentStatePagerAdapter {
        public HelpAdapter(FragmentManager fm) {
            super(fm);
        }

        public void refresh() {
            notifyDataSetChanged();
        }

        public CharSequence getPageTitle(int position) {
            return "";
        }

        public int getCount() {
            return 6;
        }

        public int getItemPosition(Object object) {
            return -2;
        }

        public Fragment getItem(int position) {
            if (position == 0) {
                return new TakePledgeFragment();
            }
            if (position == 1) {
                return new HelpActionFragment();
            }
            if (position == 2) {
                return new HelpRegisterFragment();
            }
            if (position == 3) {
                return new HelpLoginFragment();
            }
            if (position == 4) {
                return new InviteFragment(HelpMeScreen.this.arrList);
            }
            if (position == 5) {
                return new HelpMeFragment();
            }
            return null;
        }
    }
}
