package com.BasicApp.ActivityModel.LoyaltyCard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.BasicApp.Adapters.MyActivityListAdapter;
import com.BasicApp.BusinessLogic.Interfaces.LoyaltyActivitySummaryCallback;
import com.BasicApp.BusinessLogic.Models.LoyaltyActivityList;
import com.BasicApp.BusinessLogic.Models.LoyaltyActivityListItem;
import com.BasicApp.BusinessLogic.Services.ActivityService;
import com.BasicApp.BusinessLogic.Services.ActivityServiceSort;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.BasicApp.Utils.StringUtilities;
import com.basicmodule.sdk.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.BasicApp.BusinessLogic.Models.LoyaltyActivityList.getLoyaltyList;

public class MyActivityModel extends FragmentActivity {



    public static Spinner spinner1;
    public ListView lvDetail;
    public Map<String, String> allareaToId = new HashMap<String, String>();
    LinearLayout date_col, activity_col, earnedpoint_col, checknumber_col, point_col;

    ArrayList myList = new ArrayList();
    ProgressBarHandler p;
    ArrayList<LoyaltyActivityListItem> mylist;
    MyActivityListAdapter activitylist;
    Button bt_edit;
    ArrayAdapter<String> dataAdapter;

    private RelativeLayout toolbar;
    private DrawerLayout drawerLayout;
    ImageView backbutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_activity_list);
        p = new ProgressBarHandler(MyActivityModel.this);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        lvDetail = (ListView) findViewById(R.id.lvCustomList);
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.menu_navigator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else
                    drawerLayout.openDrawer(GravityCompat.END);
            }

        });
        backbutton = (ImageView) toolbar.findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyActivityModel.this.finish();
            }
        });

        p.show();

        bt_edit = (Button) findViewById(R.id.btn_apply);
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getloyaltyactivity();


            }
        });

        if (getLoyaltyList() != null) {
            if (getLoyaltyList().size() > 0) {

                mylist = getLoyaltyList();
                activitylist = new MyActivityListAdapter(MyActivityModel.this, mylist);

                lvDetail.setAdapter(activitylist);
                p.dismiss();
            } else {

                p.dismiss();
                showalertoffer();
            }
        } else {
            p.dismiss();
            showalertoffer();
        }


        spinner1 = (Spinner) findViewById(R.id.spinner1);
        List<String> categories = new ArrayList<String>();
        categories.add("Points Earned");
        categories.add("Reward Earned");
        categories.add("Points to Reward Conversion");
        categories.add(0, "Filter Activity");
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String label = parentView.getItemAtPosition(position).toString();
                if (label.equalsIgnoreCase("Filter Activity")) {

                    TextView selectedText = (TextView) parentView.getChildAt(0);
                    if (selectedText != null) {
                        selectedText.setTextColor(ContextCompat.getColor(MyActivityModel.this, R.color.textHint));
                    }
                } else {
                    TextView selectedText = (TextView) parentView.getChildAt(0);
                    if (selectedText != null) {
                        selectedText.setTextColor(ContextCompat.getColor(MyActivityModel.this, R.color.orignalText));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });


        dataAdapter = new ArrayAdapter<String>(MyActivityModel.this, R.layout.spinner_item, categories) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.textHint));
                } else {
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.orignalText));
                }
                return view;
            }
        };
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner1.setAdapter(dataAdapter);


        date_col = (LinearLayout) findViewById(R.id.date_col);
        date_col.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ImageView imageView = (ImageView) view.findViewById(R.id.date_arrow);
                Integer integer = (Integer) imageView.getTag();
                integer = integer == null ? 0 : integer;
                if (integer.equals(R.drawable.arrowdown)) {
                    imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.arrowup));
                    ;
                    imageView.setTag(R.drawable.arrowup);
                    getloyaltyactivityReloader("eventDate", "DESC");

                } else if (integer.equals(R.drawable.arrowup)) {
                    imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.arrowdown));
                    ;
                    imageView.setTag(R.drawable.arrowdown);
                    getloyaltyactivityReloader("eventDate", "ASC");

                } else {
                    imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.arrowup));
                    imageView.setTag(R.drawable.arrowup);
                    getloyaltyactivityReloader("eventDate", "DESC");

                }
            }
        });
        activity_col = (LinearLayout) findViewById(R.id.activity_col);
        activity_col.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageView imageView = (ImageView) view.findViewById(R.id.activity_arrow);
                Integer integer = (Integer) imageView.getTag();
                integer = integer == null ? 0 : integer;
                if (integer.equals(R.drawable.arrowdown)) {
                    imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.arrowup));
                    imageView.setTag(R.drawable.arrowup);
                    getloyaltyactivityReloader("description", "DESC");

                } else if (integer.equals(R.drawable.arrowup)) {
                    imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.arrowdown));
                    imageView.setTag(R.drawable.arrowdown);
                    getloyaltyactivityReloader("description", "ASC");

                } else {
                    imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.arrowup));
                    imageView.setTag(R.drawable.arrowup);
                    getloyaltyactivityReloader("description", "DESC");

                }
            }
        });
        point_col = (LinearLayout) findViewById(R.id.point_col);
        point_col.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   getloyaltyactivityReloader(x,y);

                ImageView imageView = (ImageView) view.findViewById(R.id.point_arrow);
                Integer integer = (Integer) imageView.getTag();
                integer = integer == null ? 0 : integer;
                if (integer.equals(R.drawable.arrowdown)) {
                    imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.arrowup));
                    ;
                    imageView.setTag(R.drawable.arrowup);
                    getloyaltyactivityReloader("balance", "DESC");

                } else if (integer.equals(R.drawable.arrowup)) {
                    imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.arrowdown));
                    ;
                    imageView.setTag(R.drawable.arrowdown);
                    getloyaltyactivityReloader("balance", "ASC");

                } else {
                    imageView.setImageDrawable(view.getResources().getDrawable(R.drawable.arrowup));
                    imageView.setTag(R.drawable.arrowup);
                    getloyaltyactivityReloader("balance", "DESC");

                }


            }
        });

        FBUtils.setUpNavigationDrawerModel(MyActivityModel.this);
    }


    public void getloyaltyactivity() {



        String passarea = spinner1.getSelectedItem().toString();
        if(!passarea.equalsIgnoreCase("Filter Activity"))
        {
            p.show();
            String area = null;
            if (passarea.equalsIgnoreCase("Points Earned")) {
                area = "POINT_RULE";
            } else if (passarea.equalsIgnoreCase("Reward Earned")) {
                area = "REWARD_RULE";
            } else if (passarea.equalsIgnoreCase("PointBank Reward Claim")) {
                area = "CLAIM";
            } else if (passarea.equalsIgnoreCase("Points to Reward Conversion")) {
                area = "REDEMPTION";
            }

            JSONObject obj = new JSONObject();
            try {
                obj.put("areaType", area);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ActivityService.getActivity(MyActivityModel.this, obj, new LoyaltyActivitySummaryCallback() {


                @Override
                public void onLoyaltyActivitySummaryCallback(LoyaltyActivityList rewardSummary, Exception error) {


                    if (rewardSummary != null) {
                        if (getLoyaltyList() != null) {
                            if (getLoyaltyList().size() > 0) {
                                p.dismiss();
                                mylist = getLoyaltyList();
                                activitylist = new MyActivityListAdapter(MyActivityModel.this, mylist);
                                lvDetail.setAdapter(activitylist);
                                activitylist.notifyDataSetChanged();

                            } else {

                                p.dismiss();
                                if (mylist != null) {
                                    if (mylist.size() > 0) {
                                        mylist.clear();
                                        activitylist.notifyDataSetChanged();

                                    }
                                }
                                showalertoffer();

                            }
                        } else {
                            p.dismiss();
                            if (mylist != null) {
                                if (mylist.size() > 0) {
                                    mylist.clear();

                                    activitylist.notifyDataSetChanged();
                                }
                            }
                            showalertoffer();

                        }

                    } else {


                        FBUtils.tryHandleTokenExpiry(MyActivityModel.this, error);
                        p.dismiss();
                    }
                }
            });
        }}




    public void showalertoffer() {
        AlertDialog alertDialog = new AlertDialog.Builder(MyActivityModel.this).create();
        alertDialog.setMessage("No Activity Rule  for this type");
        alertDialog.setIcon(R.drawable.ic_launcher);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    public void getloyaltyactivityReloader(String a, String b) {
        p.show();
        String activityType;
        String passarea = null;

        if (StringUtilities.isValidString(spinner1.getSelectedItem().toString())) {
            passarea = spinner1.getSelectedItem().toString();
        } else {
            passarea = "";
        }

        if (passarea.equalsIgnoreCase("Points Earned")) {
            activityType = "POINT_RULE";
        } else if (passarea.equalsIgnoreCase("Reward Earned")) {
            activityType = "REWARD_RULE";
        } else if (passarea.equalsIgnoreCase("PointBank Reward Claim")) {
            activityType = "CLAIM";
        } else if (passarea.equalsIgnoreCase("Points to Reward Conversion")) {
            activityType = "REDEMPTION";
        } else if (passarea.equalsIgnoreCase("Filter Activity")) {
            activityType = "";
        } else {
            activityType = "";
        }

        org.json.JSONArray jsonarray = new org.json.JSONArray();
        try {
            JSONObject obj = new JSONObject();
            obj.put("fieldName", a);
            obj.put("direction", b);
            jsonarray.put(obj);

        } catch (Exception e) {
            e.printStackTrace();
        }


        ActivityServiceSort.getActivity(MyActivityModel.this, jsonarray, activityType, new LoyaltyActivitySummaryCallback() {


            @Override
            public void onLoyaltyActivitySummaryCallback(LoyaltyActivityList rewardSummary, Exception error) {
                {


                    if (rewardSummary != null) {
                        if (getLoyaltyList() != null) {
                            if (getLoyaltyList().size() > 0) {
                                p.dismiss();
                                mylist = getLoyaltyList();
                                activitylist = new MyActivityListAdapter(MyActivityModel.this, mylist);
                                lvDetail.setAdapter(activitylist);
                                activitylist.notifyDataSetChanged();
                                p.dismiss();
                            } else {

                                p.hide();
                                if (mylist != null) {
                                    if (mylist.size() > 0) {
                                        mylist.clear();
                                        activitylist.notifyDataSetChanged();

                                    }
                                }
                                showalertoffer();

                            }
                        } else {
                            p.dismiss();
                            if (mylist != null) {
                                if (mylist.size() > 0) {
                                    mylist.clear();

                                    activitylist.notifyDataSetChanged();
                                }
                            }
                            showalertoffer();

                        }

                    } else {


                        FBUtils.tryHandleTokenExpiry(MyActivityModel.this, error);
                        p.dismiss();
                    }
                }


                if (rewardSummary != null) {

                } else {

                }
            }
        });
    }


}



