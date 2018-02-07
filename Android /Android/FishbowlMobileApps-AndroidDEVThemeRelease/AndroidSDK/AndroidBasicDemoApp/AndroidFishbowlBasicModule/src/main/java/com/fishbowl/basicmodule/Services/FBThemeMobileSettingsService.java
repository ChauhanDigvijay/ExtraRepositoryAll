package com.fishbowl.basicmodule.Services;
/**
 * Created by digvijay(dj) //hbh
 */

import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Models.FBThemeGeneralFieldSetting;
import com.fishbowl.basicmodule.Models.FBThemeLoginField;
import com.fishbowl.basicmodule.Models.FBThemeLoginFieldSetting;
import com.fishbowl.basicmodule.Models.FBThemeRegistrationField;
import com.fishbowl.basicmodule.Models.FBThemeRegistrationFieldSetting;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class FBThemeMobileSettingsService implements Serializable {

    //Constant;
    public static String PROFILEFIELD = "profilefield";
    public static String THEMECONFIGSETTING = "themeConfigSettings";
    public static String ThemeDetails = "themeDetails";
    public static String THEMECONFIGSETTINGS = "themeConfigSettings";
    public static String THEMECREATIVESETTINGS = "themeCreativeSettings";
    public static String FIELDS = "fields";
    public static FBThemeMobileSettingsService instance;

    public ArrayList<FBThemeRegistrationField> registerFields = new ArrayList<FBThemeRegistrationField>();
    public ArrayList<FBThemeLoginField> loginFields = new ArrayList<FBThemeLoginField>();
    public ArrayList<FBThemeRegistrationFieldSetting> registerFieldSetting = new ArrayList<FBThemeRegistrationFieldSetting>();
    public ArrayList<FBThemeLoginFieldSetting> loginFieldSetting = new ArrayList<FBThemeLoginFieldSetting>();
    public ArrayList<FBThemeGeneralFieldSetting> generalFieldSetting = new ArrayList<FBThemeGeneralFieldSetting>();

    private FBSdk fbSdk;

    public static FBThemeMobileSettingsService sharedInstance() {
        if (instance == null) {
            instance = new FBThemeMobileSettingsService();
        }
        return instance;
    }

    public void init(FBSdk _fbsdk) {

        fbSdk = _fbsdk;

    }

    public void initFromJson(JSONObject jsonObj) {

        try {


            if (jsonObj.has(ThemeDetails) && !jsonObj.isNull(ThemeDetails))
            {
                JSONObject ThemeDetails = new JSONObject(jsonObj.getString("themeDetails"));

                if (ThemeDetails.has(PROFILEFIELD) && !ThemeDetails.isNull(PROFILEFIELD))
                {
                    JSONArray profilefield = new JSONArray(ThemeDetails.getString(PROFILEFIELD));
                    int lenth = profilefield.length();
                    for (int i = 0; i < lenth; i++) {
                        JSONObject jsonfield = (JSONObject) profilefield.get(i);
                        if (jsonfield.has("pageName")) {
                            String    pageNameParam=   jsonfield.getString("pageName");
                            if (pageNameParam.equalsIgnoreCase("Registration")){
                                JSONArray fields = new JSONArray(jsonfield.getString(FIELDS));
                                int fieldslenth = fields.length();
                                for (int j = 0; j < fieldslenth; j++) {
                                    FBThemeRegistrationField regfield = new FBThemeRegistrationField();
                                    JSONObject jsoObj = (JSONObject) fields.get(j);
                                    regfield.initFromJson(jsoObj);
                                    registerFields.add(regfield);
                                    Collections.sort(registerFields,Collections.reverseOrder(new SortFBThemeRegistrationField()));
                                }
                            }
//                            else  if (pageNameParam.equalsIgnoreCase("Login")){
//
//                                JSONArray fields = new JSONArray(jsonfield.getString(FIELDS));
//                                int fieldslenth = fields.length();
//                                for (int j = 0; j < fieldslenth; j++) {
//                                    FBThemeLoginField regfield = new FBThemeLoginField();
//                                    JSONObject jsoObj = (JSONObject) fields.get(j);
//                                    regfield.initFromJson(jsoObj);
//                                    loginFields.add(regfield);
//                                }
//
//                            }
                        }
                    }
                }

                 if (ThemeDetails.has(THEMECONFIGSETTING) && !ThemeDetails.isNull(THEMECONFIGSETTING))
                {

                    JSONArray profilefield = new JSONArray(ThemeDetails.getString(THEMECONFIGSETTINGS));

                    int lenth = profilefield.length();
                    for (int i = 0; i < lenth; i++) {
                        JSONObject jsonfield = (JSONObject) profilefield.get(i);
                        if (jsonfield.has("pageName"))
                        {
                            String  pageNameParam=   jsonfield.getString("pageName");

                            if (pageNameParam.equalsIgnoreCase("Registration")) {
                                JSONArray fields = new JSONArray(jsonfield.getString(THEMECREATIVESETTINGS));
                                int fieldslenth = fields.length();
                                for (int j = 0; j < fieldslenth; j++) {
                                    FBThemeRegistrationFieldSetting regfield = new FBThemeRegistrationFieldSetting();
                                    JSONObject jsoObj = (JSONObject) fields.get(j);
                                    regfield.initFromJson(jsoObj);
                                    registerFieldSetting.add(regfield);
                                }
                            } else if (pageNameParam.equalsIgnoreCase("Login")) {
                                JSONArray fields = new JSONArray(jsonfield.getString(THEMECREATIVESETTINGS));
                                int fieldslenth = fields.length();
                                for (int j = 0; j < fieldslenth; j++) {
                                    FBThemeLoginFieldSetting regfield = new FBThemeLoginFieldSetting();
                                    JSONObject jsoObj = (JSONObject) fields.get(j);
                                    regfield.initFromJson(jsoObj);
                                    loginFieldSetting.add(regfield);
                                }

                            } else if (pageNameParam.equalsIgnoreCase("General")) {
                                JSONArray fields = new JSONArray(jsonfield.getString(THEMECREATIVESETTINGS));
                                int fieldslenth = fields.length();
                                for (int j = 0; j < fieldslenth; j++) {
                                    FBThemeGeneralFieldSetting regfield = new FBThemeGeneralFieldSetting();
                                    JSONObject jsoObj = (JSONObject) fields.get(j);
                                    regfield.initFromJson(jsoObj);
                                    generalFieldSetting.add(regfield);
                                }

                            }
                        }
                    }

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getThemeSettings(JSONObject parameter, final FBThemeSettingsCallback callback) {

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        FBService.getInstance().get(FBConstant.FBThemeSettingsApi, null, getHeader3(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                if (error == null && response != null) {
                    FBThemeMobileSettingsService.sharedInstance().initFromJson(response);
                    callback.onThemeSettingsCallback(response, null);
                } else {
                    callback.onThemeSettingsCallback(null, error);
                }
            }

        });
    }

    //Login
    HashMap<String, String> getHeader3() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        return header;
    }

    public interface FBThemeSettingsCallback {
        public void onThemeSettingsCallback(JSONObject response, Exception error);
    }
    class SortFBThemeRegistrationField implements Comparator<FBThemeRegistrationField> {

        @Override
        public int compare(FBThemeRegistrationField e1, FBThemeRegistrationField e2) {
            if(e1.getConfigDisplaySeq() < e2.getConfigDisplaySeq()){
                return 1;
            } else {
                return -1;
            }


        }
    }

}
