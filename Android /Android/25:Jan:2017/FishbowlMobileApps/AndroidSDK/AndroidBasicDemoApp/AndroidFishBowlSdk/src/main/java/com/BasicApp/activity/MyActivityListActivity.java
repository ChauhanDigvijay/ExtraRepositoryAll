package com.BasicApp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.BasicApp.Adapters.MyActivityListAdapter;
import com.BasicApp.BusinessLogic.Models.MyActivityListData;
import com.BasicApp.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.basicmodule.sdk.R;

import java.util.ArrayList;

/**
 * Created by schaudhary_ic on 09-Nov-16.
 */

public class MyActivityListActivity extends FragmentActivity  {
    Button log;
    private ImageLoader mImageLoader;
    private NetworkImageView titleimage,titlebackground;
    TextView title,title_welcome;

    LinearLayout profile_way;
    LinearLayout layout_button;
    NetworkImageView backgroundImage;
    public ListView lvDetail;
    ArrayList myList = new ArrayList();
    private android.support.v7.widget.Toolbar toolbar;
    ProgressBarHandler p;

    String[] date = new String[]{
            "09/10/16", "10/10/16", "11/10/16", "11/10/16",
            "12/10/16", "13/10/16", "14/10/16", "15/10/16","16/10/16"
    };
    String[] activity = new String[]{
            "Points Earn",  "Points Earn",  "Redemption", "Points to Rewards Conversion",
            "Points Earn", "Converted", "Redemption",  "Points Earn", "Points Earn"
    };
    String[] null1 = new String[]{
            "6 Points", "Free Entree", "$1 off juice", "10 points to $1 off juice",
            "100 points", "45 points", "Free Coffee", "10 points","5 points"
    };
    String[] null2 = new String[]{
            "Check123456", "", "Check123456", "",
            "Check45678", "", "", "",""
    };
    String[] null3 = new String[]{
            "20 Points", "25 Points", "15 Points", "9 Points",
            "100 Points", "32 Points", "45 Points", "15 Points","5 Points"
    };
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myactivity);
        p = new ProgressBarHandler(this);
        getDataInList();
        lvDetail = (ListView) findViewById(R.id.lvCustomList);
        p.show();

        if (myList != null) {

            lvDetail.setAdapter(new MyActivityListAdapter(this, myList));
            p.hide();
        }
        toolbar= (Toolbar) findViewById(R.id.tool_bar);
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyActivityListActivity.this.finish();
            }
        });
    }
    @Override
    public void onStart()
    {
        super.onStart();
    }

    private void getDataInList() {
        p.show();
        for (int i = 0; i<date.length; i++) {
            // Create a new object for each list item
            MyActivityListData ld = new MyActivityListData();
            ld.setDateData(date[i]);
            ld.setActivityData(activity[i]);
            ld.setNullData1(null1[i]);
            ld.setNullData2(null2[i]);
            ld.setNullData3(null3[i]);
            // Add this object into the ArrayList myList
            myList.add(ld);
            p.hide();
        }
    }

}
