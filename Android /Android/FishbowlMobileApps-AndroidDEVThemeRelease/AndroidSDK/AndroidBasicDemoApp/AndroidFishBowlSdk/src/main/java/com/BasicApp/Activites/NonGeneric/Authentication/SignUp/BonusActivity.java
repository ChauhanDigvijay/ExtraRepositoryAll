package com.BasicApp.Activites.NonGeneric.Authentication.SignUp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.Bonus;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBUserService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by schaudhary_ic on 13-Dec-16.
 */

public class BonusActivity extends Activity {
    ListView bonusList;
    ArrayList<Bonus> bonusRuleList = new ArrayList<Bonus>();
    BonusAdapter adapter;
    Boolean rewardRule;
    int ruleId;
    String desc;
    ProgressBarHandler p;
    Timer t = new Timer();
    RelativeLayout toolbar ;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus);
        p = new ProgressBarHandler(this);
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        title = (TextView) toolbar.findViewById(R.id.title_text);
        title.setText("Bonus");
        toolbar.findViewById(R.id.backbutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bonusList = (ListView) findViewById(R.id.bonusList);
        getBonusRuleList();
        if (bonusRuleList!=null){
            adapter = new BonusAdapter(this,bonusRuleList);
            bonusList.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();

        TimerTask task = new TimerTask() {

            @Override
            public void run() {

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();

                    }
                });
            }
        };  t.scheduleAtFixedRate(task, 0, 1000);
        bonusList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bonus selectedBonus =  bonusRuleList.get(position);
                rewardRule = selectedBonus.getRewardRule();
                ruleId = selectedBonus.getId();
                desc = selectedBonus.getDescription();
            //    System.out.println(nameState);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("rewardRule",String.valueOf(rewardRule));
                returnIntent.putExtra("ruleId",String.valueOf(ruleId));
                returnIntent.putExtra("desc",desc);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
    public  void  getBonusRuleList(){
        p.show();
        final JSONArray object = new JSONArray();
        FBUserService.sharedInstance().getBonusRuleList(object, new FBUserService.FBBonusRuleListCallback()
        {

            public void onBonusRuleListCallback(JSONArray response, Exception error)
            {try
            {
                if (response != null && error==null)
                {

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject myBonusObj = response.getJSONObject(i);
                            Bonus bonusObj = new Bonus(myBonusObj);
                            bonusRuleList.add(bonusObj);
                        }
                    p.hide();

                }
                else
                {
                    FBUtils.tryHandleTokenExpiry(BonusActivity.this, error);

                }
            }
            catch (Exception e){

            }
            }
        });
    }
     public class BonusAdapter extends BaseAdapter{
         public List<Bonus> bonusList;
         public Context context;

         public BonusAdapter(Context context, List<Bonus> bonusList) {
             this.context = context;
             this.bonusList = bonusList;
         }
         @Override
         public int getCount() {
             return bonusList.size();
         }

         @Override
         public Object getItem(int position) {
             return bonusList.get(position);
         }

         @Override
         public long getItemId(int position) {
             return position;
         }

         @Override
         public View getView(int position, View convertView, ViewGroup parent) {
             p.show();
             if (convertView == null) {
                 convertView = LayoutInflater.from(context).inflate(R.layout.row_bonus_list, parent, false);
             }

             TextView bonusDescription = (TextView) convertView.findViewById(R.id.bonusDescription);
             Bonus bonusFetchedList =  bonusRuleList.get(position);
             if(bonusRuleList!=null)
                 bonusDescription.setText(bonusFetchedList.getDescription());
            p.hide();
             return convertView;
         }
     }
}
