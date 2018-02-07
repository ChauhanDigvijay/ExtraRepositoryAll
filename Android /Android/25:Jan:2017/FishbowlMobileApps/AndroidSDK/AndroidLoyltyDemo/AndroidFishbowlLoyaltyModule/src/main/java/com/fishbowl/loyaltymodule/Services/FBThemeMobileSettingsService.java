package com.fishbowl.loyaltymodule.Services;
/**
 * Created by digvijay(dj) //hbh
 */


import com.fishbowl.loyaltymodule.Controllers.FBSdk;
import com.fishbowl.loyaltymodule.Interfaces.FBServiceCallback;
import com.fishbowl.loyaltymodule.Models.FBThemeGeneralFieldSetting;
import com.fishbowl.loyaltymodule.Models.FBThemeLoginField;
import com.fishbowl.loyaltymodule.Models.FBThemeLoginFieldSetting;
import com.fishbowl.loyaltymodule.Models.FBThemeRegistrationField;
import com.fishbowl.loyaltymodule.Models.FBThemeRegistrationFieldSetting;
import com.fishbowl.loyaltymodule.Utils.FBConstant;
import com.fishbowl.loyaltymodule.Utils.FBUtility;
import com.fishbowl.loyaltymodule.Utils.StringUtilities;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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

    public static ArrayList<String> customfielddatabasename = new ArrayList<String>();

    public Map<String, String> registermapsetting = new HashMap<String, String>();//storesMap

    public Map<String, String> loginmapsetting = new HashMap<String, String>();//storesMap

    public Map<String, String> generalmapsetting = new HashMap<String, String>();//storesMap

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
                                    if(regfield.customField)
                                    {
                                        if(StringUtilities.isValidString(regfield.databaseName)) {

                                            customfielddatabasename.add(regfield.databaseName.toLowerCase());
                                        }
                                    }
                                    Collections.sort(registerFields,Collections.reverseOrder(new SortFBThemeRegistrationField()));
                                }
                            }
                            else  if (pageNameParam.equalsIgnoreCase("Login")){

                                JSONArray fields = new JSONArray(jsonfield.getString(FIELDS));
                                int fieldslenth = fields.length();
                                for (int j = 0; j < fieldslenth; j++) {
                                    FBThemeLoginField regfield = new FBThemeLoginField();
                                    JSONObject jsoObj = (JSONObject) fields.get(j);
                                    regfield.initFromJson(jsoObj);
                                    loginFields.add(regfield);
                                }

                            }
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
                                    registermapsetting.put(regfield.getConfigName(), regfield.getConfigValue());
                                    registerFieldSetting.add(regfield);
                                }
                            } else if (pageNameParam.equalsIgnoreCase("Login")) {
                                JSONArray fields = new JSONArray(jsonfield.getString(THEMECREATIVESETTINGS));
                                int fieldslenth = fields.length();
                                for (int j = 0; j < fieldslenth; j++) {
                                    FBThemeLoginFieldSetting regfield = new FBThemeLoginFieldSetting();
                                    JSONObject jsoObj = (JSONObject) fields.get(j);
                                    regfield.initFromJson(jsoObj);
                                    loginmapsetting.put(regfield.getConfigName(), regfield.getConfigValue());
                                    loginFieldSetting.add(regfield);
                                }

                            } else if (pageNameParam.equalsIgnoreCase("General")) {
                                JSONArray fields = new JSONArray(jsonfield.getString(THEMECREATIVESETTINGS));
                                int fieldslenth = fields.length();
                                for (int j = 0; j < fieldslenth; j++) {
                                    FBThemeGeneralFieldSetting regfield = new FBThemeGeneralFieldSetting();
                                    JSONObject jsoObj = (JSONObject) fields.get(j);
                                    regfield.initFromJson(jsoObj);
                                    generalmapsetting.put(regfield.getConfigName(), regfield.getConfigValue());
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
        header.put("Application", "kiosk");
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
