package com.BasicApp.ActivityModel.Contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.BasicApp.BusinessLogic.Models.AreaType;
import com.BasicApp.BusinessLogic.Models.MessageType;
import com.BasicApp.Utils.FBUtils;
import com.BasicApp.Utils.ProgressBarHandler;
import com.android.volley.toolbox.ImageLoader;
import com.basicmodule.sdk.R;
import com.fishbowl.basicmodule.Services.FBUserService;
import com.fishbowl.basicmodule.Services.FBViewMobileSettingsService;
import com.fishbowl.basicmodule.Utils.CustomVolleyRequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by schaudhary_ic on 01-Dec-16.
 */

public class ContactUsModelActivity extends Activity {
    private ImageLoader mImageLoader;
    RelativeLayout toolbar;
    ImageView backbutton,menu_navigator;
    TextView toolbarTittle;
    private DrawerLayout drawerLayout;
    public static Spinner messageSpinner,areaSpinner;
    EditText subject,message;
    Button submit,reset;
    ProgressBarHandler p;
    public List<AreaType> allAreaListfromServer = new ArrayList<AreaType>();
    public List<MessageType> allMessageListfromServer = new ArrayList<MessageType>();
    int areaPosition,messagePosition;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> list1 = new ArrayList<String>();
    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;
    public Map<String, Integer> allmessageoId = new HashMap<String, Integer>();
    public Map<String, Integer> allareaToId = new HashMap<String, Integer>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = (RelativeLayout) findViewById(R.id.tool_bar);
        toolbarTittle = (TextView) findViewById(R.id.title_text);
        backbutton = (ImageView) toolbar.findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        FBUtils.setUpNavigationDrawerModel(ContactUsModelActivity.this);
        menu_navigator = (ImageView) toolbar.findViewById(R.id.menu_navigator);
        menu_navigator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                    drawerLayout.closeDrawer(GravityCompat.END);
                } else
                    drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        p = new ProgressBarHandler(this);
        messageSpinner = (Spinner) findViewById(R.id.spinner1);
        areaSpinner = (Spinner) findViewById(R.id.spinner2);
        subject = (EditText) findViewById(R.id.et_subject);
        message = (EditText) findViewById(R.id.et_message);
        submit = (Button) findViewById(R.id.btn_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactUs();
            }
        });
        reset = (Button) findViewById(R.id.btn_reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subject.getText().clear();
                message.getText().clear();
                messageSpinner.setSelection(0);
                areaSpinner.setSelection(0);
            }
        });
        getLoyaltyMessageType();
        getLoyaltyAreaType();

        list.add(0,"Select Option");
        adapter1 = new ArrayAdapter<String>(this,R.layout.spinner_item, list){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                }
                else {
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                return view;
            }
        };
        adapter1.setDropDownViewResource(R.layout.spinner_item);
        messageSpinner.setAdapter(adapter1);


        adapter1.notifyDataSetChanged();

        messageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                // Toast.makeText(getActivity(), "Selected", Toast.LENGTH_SHORT).show();
                // parentView.setBackgroundColor(Color.BLACK);
                //    ((TextView) selectedItemView).setTextColor(Color.parseColor("#E3170D"));

                //  adapter1.getDropDownView()
                TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(ContextCompat.getColor(ContactUsModelActivity.this, R.color.black));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
//                TextView selectedText = (TextView) parentView.getChildAt(0);
//                if (selectedText != null) {
//                    selectedText.setTextColor(Color.RED);
//                }
            }

        });

        list1.add(0,"Select Option");
        adapter2 = new ArrayAdapter<String>(this, R.layout.spinner_item, list1){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.gray));
                }
                else {
                    tv.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                return view;
            }
        };
        areaSpinner.setAdapter(adapter2);
        adapter2.setDropDownViewResource(R.layout.spinner_item);
        adapter2.notifyDataSetChanged();
        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //Toast.makeText(getActivity(), "Selected", Toast.LENGTH_SHORT).show();
                TextView selectedText = (TextView) parentView.getChildAt(0);
                if (selectedText != null) {
                    selectedText.setTextColor(ContextCompat.getColor(ContactUsModelActivity.this, R.color.black));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });


        areaSpinner.setPrompt("Area");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mImageLoader = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getImageLoader();
        String buttoncolor= FBViewMobileSettingsService.sharedInstance().checkInButtonColor;
        if(buttoncolor!=null)
        {
            //    toolbarTittle.setTextColor(Color.parseColor("#" + FBViewMobileSettingsService.sharedInstance().checkInButtonColor));
        }
    }


    public void getLoyaltyAreaType(){
        p.show();
        JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getLoyaltyAreaType(object, new FBUserService.FBLoyaltyAreaTypeCallback()
        {
            public void onLoyaltyAreaTypeCallback(JSONObject response, Exception error)
            {try
            {
                if (response != null && error==null)
                {
                    if (!response.has("loyaltyAreaType"))
                        return;

                    JSONArray getAreaArray = response.getJSONArray("loyaltyAreaType");
                    if (getAreaArray != null) {

                        for (int i = 0; i < getAreaArray.length(); i++) {
                            JSONObject myAreaObj = getAreaArray.getJSONObject(i);
                            AreaType areaListObj = new AreaType(myAreaObj);
                            list1.add(areaListObj.getAreaType());
                            allAreaListfromServer.add(areaListObj);
                            allareaToId.put(areaListObj.getAreaType(),areaListObj.getId());
                        }
                        p.hide();
                        adapter2.notifyDataSetChanged();
                    }
                }
                else
                {
                    FBUtils.tryHandleTokenExpiry(ContactUsModelActivity.this, error);

                }
            }
            catch (Exception e){}
            }
        });
    }

    public void getLoyaltyMessageType(){
        p.show();
        JSONObject object = new JSONObject();
        FBUserService.sharedInstance().getLoyaltyMessageType(object, new FBUserService.FBLoyaltyMessageTypeCallback()
        {
            public void onLoyaltyMessageTypeCallback(JSONObject response, Exception error)
            {try
            {
                if (response != null && error==null)
                {
                    if (!response.has("loyaltyMessageType"))
                        return;

                    JSONArray getMessageArray = response.getJSONArray("loyaltyMessageType");
                    if (getMessageArray != null) {

                        for (int i = 0; i < getMessageArray.length(); i++) {
                            JSONObject myAreaObj = getMessageArray.getJSONObject(i);
                            MessageType messageListObj = new MessageType(myAreaObj);
                            list.add(messageListObj.getMessageType());
                            allMessageListfromServer.add(messageListObj);
                            allmessageoId.put(messageListObj.getMessageType(),messageListObj.getId());
                        }
                        p.hide();
                    }
                }
                else
                {
                    FBUtils.tryHandleTokenExpiry(ContactUsModelActivity.this, error);

                }
            }
            catch (Exception e){}
            }
        });

    }

   public boolean checkValidation()
   {
       //  fname, lname, email, phone, address,password;
       if(!FBUtils.isValidString(subject.getText().toString()))
       {
           FBUtils.showAlert(this,"Message Subject is Empty ");
           return false;
       }
       if(!FBUtils.isValidString(messageSpinner.getSelectedItem().toString())||messageSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Option"))
       {
           FBUtils.showAlert(this,"Message Type is Empty ");
           return false;
       }
       if(!FBUtils.isValidString(areaSpinner.getSelectedItem().toString())||areaSpinner.getSelectedItem().toString().equalsIgnoreCase("Select Option"))
       {
           FBUtils.showAlert(this,"Area Type is Empty ");
           return false;
       }
       if(!FBUtils.isValidString(message.getText().toString())  )
       {
           FBUtils.showAlert(this,"Message is Empty");
           return false;
       }

       return true;
   }

    public void contactUs()
    {
        String sub = subject.getText().toString();
        String msg = message.getText().toString();
        String area = areaSpinner.getSelectedItem().toString();
        String msgType = messageSpinner.getSelectedItem().toString();
        JSONObject obj = new JSONObject();
        try {
            obj.put("subject", sub);
            obj.put("description", msg);
            obj.put("messageType", msgType);
            obj.put("areaType", area);
            obj.put("memberId", FBUserService.sharedInstance().member.customerID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // progressBar..Handler.show();
        p.show();
        FBUserService.sharedInstance().contactUs(obj, new FBUserService.FBContactUsCallback()
        {
            @Override
            public void onContactUsCallback(JSONObject response, Exception error)
            {
                // progressBarHandler.hide();
                if (response != null)
                {
                    if(response.has("successFlag")) {
                        try {
                            if(response.getString("successFlag").equalsIgnoreCase("true")) {
                                finish();
                                subject.getText().clear();
                                message.getText().clear();
                                messageSpinner.setSelection(0);
                                areaSpinner.setSelection(0);
                                p.dismiss();
                            }
                            else
                            {
                                p.hide();
                                alertmessage(response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }
                else
                {
                    p.hide();
                    FBUtils.showErrorAlert(ContactUsModelActivity.this, error);
                }
            }
        });
    }





    public  void alertmessage(String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.logo);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }


}
