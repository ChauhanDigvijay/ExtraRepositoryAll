package com.fishbowl.basicmodule.Services;


import android.util.Log;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBServiceArrayCallback;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Models.Member;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by digvijay(dj)
 */
public class FBUserService {

    public Member member;
    private FBSdk fbSdk;
    public static FBUserService instance ;


    public String access_token=null;


    public static FBUserService sharedInstance(){
        if(instance==null){
            instance=new FBUserService();

        }
        return instance;
    }

    public void init(FBSdk _clpsdk){
        fbSdk =_clpsdk;
        member=new Member();

    }

    public void createMember(JSONObject parameter,final FBCreateMemberCallback callback){



        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        String str=parameter.toString();

        FBService.getInstance().post(FBConstant.FBCreateMemberApi, str,getHeader1(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){


                if(error==null&&response!=null){

                    callback.onCreateMemberCallback(response,null);

                }else{
                    callback.onCreateMemberCallback(null,error);
                }
            }

        });

    }


    public void createMemberforjamba(JSONObject parameter,final FBCreateMemberCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        String str=parameter.toString();

        FBService.getInstance().post(FBConstant.FBCreateMemberApi, str,getHeadercreatejamba(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){


                if(error==null&&response!=null){

                    callback.onCreateMemberCallback(response,null);

                }else{
                    callback.onCreateMemberCallback(null,error);
                }
            }

        });

    }

    public void loginMember(JSONObject parameter,final FBLoginMemberCallback callback){


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        String str=parameter.toString();
        FBService.getInstance().post(FBConstant.FBLoginApi, str,getHeader2(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

                if(error==null&&response!=null){

                    callback.onLoginMemberCallback(response,null);

                }else{

                    callback.onLoginMemberCallback(null,error);
                }

            }

        });
    }


    public void loginMemberforjamba(JSONObject parameter,String spendGoBaseUrl,final FBLoginMemberCallback callback){


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        String str=parameter.toString();
        FBService.getInstance().post(FBConstant.FBLoginApiForJamba, str,getHeaderloginjamba(spendGoBaseUrl), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

                if(error==null&&response!=null){

                    callback.onLoginMemberCallback(response,null);

                }else{

                    callback.onLoginMemberCallback(null,error);
                }

            }

        });
    }
//dont used this method its for demo
    public void loginMemberforjambatest(JSONObject parameter,final FBLoginMemberCallback callback){


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        String str=parameter.toString();
        FBService.getInstance().post(FBConstant.FBLoginApiForJamba, str,getHeaderloginjambatest(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

                if(error==null&&response!=null){

                    callback.onLoginMemberCallback(response,null);

                }else{

                    callback.onLoginMemberCallback(null,error);
                }

            }

        });
    }


    public void getMember(JSONObject parameter,final FBGetMemberCallback callback){


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();

        FBService.getInstance().get(FBConstant.FBGetMemberApi,null, getHeader3(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response,Exception  error,String errorMessage){

                if(error==null&&response!=null){

                    member.initWithJson(response,fbSdk.context);

                    callback.onGetMemberCallback(response,null);
                }else{
                    callback.onGetMemberCallback(null,error);
                }

            }
        });

    }

    public void getMemberforjamba(JSONObject parameter,String ExternalCustomerId,final FBGetMemberCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();

        String url = "member/getMemberByExternalCustomerId" + "/" + ExternalCustomerId ;

        FBService.getInstance().get(url,null, getHeadermemberjamba(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response,Exception  error,String errorMessage){

                if(error==null&&response!=null){

                    //  member.initWithJson(response,fbSdk.context);

                    callback.onGetMemberCallback(response,null);
                }else{
                    callback.onGetMemberCallback(null,error);
                }

            }
        });

    }


    public void memberUpdate(JSONObject parameter,final FBMemberUpdateCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();
        FBService.getInstance().put(FBConstant.FBMemberUpdateApi,str, updateMemberHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

                if(error==null&&response!=null){
                    // member.initWithJson(response);
                    callback.onMemberUpdateCallback(response,null);
                }else {
                    callback.onMemberUpdateCallback(null,error);
                }
            }
        });

    }


    public void deviceUpdate(JSONObject parameter,final FBDeviceUpdateCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();
        FBService.getInstance().post(FBConstant.DeviceUpdateApi,str, deviceUpdateHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

                if(error==null&&response!=null){
                    callback.onDeviceUpdateCallback(response,null);
                }else {
                    callback.onDeviceUpdateCallback(null,error);
                }
            }
        });

    }


    public void deviceUpdateffojamba(JSONObject parameter,final FBDeviceUpdateCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();
        FBService.getInstance().post(FBConstant.DeviceUpdateApiForJamba,str, deviceUpdateHeaderjamba(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

                if(error==null&&response!=null){
                    callback.onDeviceUpdateCallback(response,null);
                }else {
                    callback.onDeviceUpdateCallback(null,error);
                }
            }
        });

    }






    public void changePassword(JSONObject parameter,final FBChangePasswordCallback callback){


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        String str=parameter.toString();
        FBService.getInstance().put(FBConstant.FBChangePasswordApi,str, changePassHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
                if(error==null&&response!=null){
                    callback.onChangePasswordCallback(response,null);
                }else {
                    callback.onChangePasswordCallback(null,error);
                }
            }
        });

    }

    public void forgetPassword(JSONObject parameter,final FBforgetPasswordCallback callback){


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        String str=parameter.toString();
        FBService.getInstance().post(FBConstant.FBforgetPasswordApi,str, forgetPassHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
                if(error==null&&response!=null){
                    callback.onFBforgetPasswordCallback(response,null);
                }else {
                    callback.onFBforgetPasswordCallback(null,error);
                }
            }
        });

    }

    public void favourteStoreUpdate(JSONObject parameter,final FBFavouriteStoreUpdateCallback callback){


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();
        FBService.getInstance().put(FBConstant.FBMStoreUpdateApi,str, updateFavouriteStoreHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

                if(error==null&&response!=null){
                    callback.onFBFavouriteStoreUpdateCallback(response,null);
                }else {
                    callback.onFBFavouriteStoreUpdateCallback(null,error);
                }
            }
        });

    }

    public void getState(JSONObject parameter,final FBStateCallback callback){


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();

        FBService.getInstance().get(FBConstant.FBStates,null, updateMemberHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response,Exception  error,String errorMessage){

                if(error==null&&response!=null){

                    //   member.initWithJson(response);

                    callback.onStateCallback(response,null);
                }else{
                    callback.onStateCallback(null,error);
                }

            }
        });

    }


    public void getCountry(JSONObject parameter,final FBCountryCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();

        FBService.getInstance().get(FBConstant.FBCountry,null, updateMemberHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response,Exception  error,String errorMessage){

                if(error==null&&response!=null)
                {

                    callback.onCountryCallback(response,null);
                }
                else
                {
                    callback.onCountryCallback(null,error);
                }

            }
        });

    }

    public void getUserLogout() {
        final JSONObject object = new JSONObject();


        FBUserService.sharedInstance().logout(object, new FBUserService.FBLogoutCallback() {
            @Override
            public void onLogoutCallback(JSONObject response, Exception error) {


            }
        });
    }

    public void logout(JSONObject parameter,final FBLogoutCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();
        FBService.getInstance().post(FBConstant.FBLogoutApi, str,logoutHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

                if(error==null&&response!=null){
                    callback.onLogoutCallback(response,null);
                }else{
                    callback.onLogoutCallback(null,error);
                }
            }

        });
    }





    public void contactUs(JSONObject parameter, final FBContactUsCallback callback) {


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        String str = parameter.toString();
        FBService.getInstance().post(FBConstant.FBContactUsApi, str, contactUsHeader(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                if (error == null && response != null) {
                    Log.d(errorMessage,response.toString());
                    callback.onContactUsCallback(response, null);
                } else {
                    callback.onContactUsCallback(null, error);
                }
            }
        });

    }

    public void getLoyaltyAreaType(JSONObject parameter, final FBLoyaltyAreaTypeCallback callback) {

        String str = parameter.toString();

        FBService.getInstance().get(FBConstant.FBLoyaltyAreaType, null, getHeader3(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {

                    //     areaType.areaJson(response);

                    callback.onLoyaltyAreaTypeCallback(response, null);
                } else {
                    callback.onLoyaltyAreaTypeCallback(null, error);
                }

            }
        });

    }

    public void getLoyaltyMessageType(JSONObject parameter, final FBLoyaltyMessageTypeCallback callback) {

        String str = parameter.toString();

        FBService.getInstance().get(FBConstant.FBLoyaltyMessageType, null, getHeader3(), new FBServiceCallback() {

            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {

                if (error == null && response != null) {

                    //     areaType.areaJson(response);

                    callback.onLoyaltyMessageTypeCallback(response, null);
                } else {
                    callback.onLoyaltyMessageTypeCallback(null, error);
                }

            }
        });

    }
    public void getBonusRuleList(JSONArray parameter, final FBBonusRuleListCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str  = parameter.toString();
        boolean dj=true;
        FBService.getInstance().makeCustomArrayRequest(FBConstant.FBBonusApi,null,getHeaderforbnus(),true, new FBServiceArrayCallback() {
            @Override
            public void onFBServiceArrayCallback(JSONArray response, Exception error, String errorMessage) {
                if (error==null && response!=null){
                    callback.onBonusRuleListCallback(response,null);
                }
                else{
                    callback.onBonusRuleListCallback(null,error);
                }
            }
        });
    }





    public void getFamilyApi(JSONObject parameter,String storeId,final FBFamilyApiCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }

        String url = FBConstant.FBGetFamilyApi+"?storeId="+storeId;
        String str=parameter.toString();
        FBService.getInstance().get(url, null, menuCategoryHeader(), new FBServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                if (error==null&&response!=null){
                    callback.onFamilyApiCallback(response,null);
                }
                else{
                    callback.onFamilyApiCallback(null,error);
                }
            }
        });
    }

    //    public void getMenuCategory(JSONObject parameter,String storeId,String familyId,final FBMenuCategoryCallback callback){
//
//        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
//            return;
//        }//storeId=647&familyId=2
//        String url = FBConstant.FBMenuCategory+"?storeId="+storeId+"&familyId="+familyId;
//        String str=parameter.toString();
//        FBService.getInstance().get(url,null, menuCategoryHeader(), new FBServiceCallback(){
//
//            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
//                if(error==null&&response!=null)
//                {
//                    //member.initWithJson(response);
//                    callback.onMenuCategoryCallback(response,null);
//                }
//                else
//                {
//                    callback.onMenuCategoryCallback(null,error);
//                }
//            }
//        });
//
//    }
    public void getMenuCategory(JSONObject parameter,String storeId,final FBMenuCategoryCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }//storeId=647&familyId=2
        String url = FBConstant.FBMenuCategory+"?storeId="+storeId;
        String str=parameter.toString();
        FBService.getInstance().get(url,null, menuCategoryHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
                if(error==null&&response!=null)
                {
                    //member.initWithJson(response);
                    callback.onMenuCategoryCallback(response,null);
                }
                else
                {
                    callback.onMenuCategoryCallback(null,error);
                }
            }
        });

    }




//
//    public void getMenuSubCategory(JSONObject parameter,String storeId,String familyId,String categoryId,final FBMenuSubCategoryCallback callback){
//        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
//            return;
//        }//storeId=647&familyId=2&categoryId=2
//        String url = FBConstant.FBMenuSubCategory+"storeId="+storeId+"&familyId="+familyId+"&categoryId="+categoryId;
//        String str=parameter.toString();
//        FBService.getInstance().get(url, null, menuCategoryHeader(), new FBServiceCallback() {
//            @Override
//            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
//                if(error==null&&response!=null)
//                {
//                    //member.initWithJson(response);
//                    callback.onMenuSubCategoryCallback(response,null);
//                }
//                else
//                {
//                    callback.onMenuSubCategoryCallback(null,error);
//                }
//            }
//        });
//    }

    public void getMenuSubCategory(JSONObject parameter,String storeId,String categoryId,final FBMenuSubCategoryCallback callback){
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }//storeId=647&familyId=2&categoryId=2
        String url = FBConstant.FBMenuSubCategory+"?storeId="+storeId+"&categoryId="+categoryId;
        String str=parameter.toString();
        FBService.getInstance().get(url, null, menuCategoryHeader(), new FBServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                if(error==null&&response!=null)
                {
                    //member.initWithJson(response);
                    callback.onMenuSubCategoryCallback(response,null);
                }
                else
                {
                    callback.onMenuSubCategoryCallback(null,error);
                }
            }
        });
    }



//    public void getDrawerProductList(JSONObject parameter,String storeId,String familyId,String categoryId,String subCategoryId,final FBMenuDrawerCallback callback){
//
//        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
//            return;
//        }//storeId:647&familyId:2&categoryId:2&subCategoryId:2
//        String url = FBConstant.FBMenuDrawer+"?storeId:"+storeId+" &familyId:"+familyId+"&categoryId:"+categoryId+"&subCategoryId:"+subCategoryId;
//        String str=parameter.toString();
//        FBService.getInstance().get(url,null, menuCategoryHeader(), new FBServiceCallback(){
//
//            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
//                if(error==null&&response!=null)
//                {
//                    //member.initWithJson(response);
//                    callback.onMenuDrawerCallback(response,null);
//                }
//                else
//                {
//                    callback.onMenuDrawerCallback(null,error);
//                }
//            }
//        });
//
//    }


    public void getDrawerProductList(JSONObject parameter,String storeId,String categoryId,String subCategoryId,final FBMenuDrawerCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }//storeId:647&familyId:2&categoryId:2&subCategoryId:2
        String url = FBConstant.FBMenuDrawer+"?storeId="+storeId+"&categoryId="+categoryId+"&subCategoryId="+subCategoryId;
        String str=parameter.toString();
        FBService.getInstance().get(url,null, menuCategoryHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
                if(error==null&&response!=null)
                {
                    //member.initWithJson(response);
                    callback.onMenuDrawerCallback(response,null);
                }
                else
                {
                    callback.onMenuDrawerCallback(null,error);
                }
            }
        });

    }

    public void getMenuProduct(JSONObject parameter,String storeId,String categoryId,String subCategoryId,String productId,final FBMenuProductCallback callback){

        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }//storeId=647&familyId=1&categoryId=0&subCategoryId=1&productId=1
        String url = FBConstant.FBMenuProduct+"?storeId="+storeId+"&categoryId="+categoryId+"&subCategoryId="+subCategoryId+"&productId="+productId;
        String str=parameter.toString();
        FBService.getInstance().get(url,null, menuCategoryHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
                if(error==null&&response!=null)
                {
                    //member.initWithJson(response);
                    callback.onMenuProductCallback(response,null);
                }
                else
                {
                    callback.onMenuProductCallback(null,error);
                }
            }
        });

    }




    public void getMenuCaegorySubCategoryCallback(JSONObject parameter,String storeId,String familyId,final FBMenuCategorySubCategoryCallback callback){
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }//storeId=647&familyId=2
        String url = FBConstant.FBMenuCategorySubCategory+"storeId="+storeId+"&familyId="+familyId;
        String str=parameter.toString();
        FBService.getInstance().get(url, null, menuCategoryHeader(), new FBServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                if(error==null&&response!=null)
                {
                    //member.initWithJson(response);
                    callback.onMenuCategorySubCategoryCallback(response,null);
                }
                else
                {
                    callback.onMenuCategorySubCategoryCallback(null,error);
                }
            }
        });
    }

    public  void getMenuProductList(JSONObject parameter,String storeId,String familyId,String categoryId,final FBMenuCategoryProductListCallback callback){
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }//storeId=647&familyId=2&categoryId=2
        String url = FBConstant.FBMenuCategoryProductList+"?storeId="+storeId+"& familyId="+familyId+"&categoryId="+categoryId;
        String str=parameter.toString();
        FBService.getInstance().get(url,null, menuCategoryHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){
                if(error==null&&response!=null)
                {
                    //member.initWithJson(response);
                    callback.onMenuCategoryProductListCallback(response,null);
                }
                else
                {
                    callback.onMenuCategoryProductListCallback(null,error);
                }
            }
        });
    }
    public void getAllRewardOfferApi(JSONObject parameter,final FBAllRewardOfferCallback callback){
        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();
        FBService.getInstance().get(FBConstant.FBGetAllRewardOffer, null, updateMemberHeader(), new FBServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                if(error==null&&response!=null){

                    callback.onAllRewardOfferCallback(response,null);
                }else {
                    callback.onAllRewardOfferCallback(null,error);
                }
            }
        });
    }


    public void getEmailOfferDetail(JSONObject offer, String Channelid,final FBLoyaltyEmailOfferDetail callback) {
//            // getClient().get(context, SERVER_URL + "mobile/getofferforcustomer" + "/" + customerId + "/" + "0", entity, "application/json", new JsonHttpResponseHandler() {

        {
            if (true) {

                String url = "mobile/getEmailOfferDetail" + "/" + Channelid;
                FBService.getInstance().get(url, null, getHeaderforemail(), new FBServiceCallback() {
                    public void onServiceCallback(JSONObject response, Exception error,String errorMessage) {
                        try {


                            if (response != null) {
                                callback.onFBLoyaltyEmailOfferDetailCallback(response, error);
                            }
                            else
                            {
                                callback.onFBLoyaltyEmailOfferDetailCallback(null, error);
                            }

                        } catch (Exception e) {

                        }

                    }
                });
            }

        }
    }


    public void getTokenApi(JSONObject parameter, final FBGetTokenCallback callback){


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();
        FBService.getInstance().put(FBConstant.FBGetToken,str, getTokenHeader(), new FBServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                if (error==null && response!=null){

                    callback.onGetTokencallback(response,null);
                }else {
                    callback.onGetTokencallback(null,error);
                }
            }


        });

    }

    public void guestUserApi(JSONObject parameter, final FBGuestUserCallback callback){


        if (!FBUtility.isNetworkAvailable(fbSdk.context)) {
            return;
        }
        String str=parameter.toString();
        FBService.getInstance().post(FBConstant.FBGuestUser,str, guestUserHeader(), new FBServiceCallback() {
            @Override
            public void onServiceCallback(JSONObject response, Exception error, String errorMessage) {
                if (error==null && response!=null){

                    callback.onGuestUserCallback(response,null);
                }else {
                    callback.onGuestUserCallback(null,error);
                }
            }


        });

    }

    //Create Member
    HashMap<String,String> getHeader1(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        //header.put("response_type","code");
        //header.put("scope","READ");
        return header;
    }


    //bonus api
    HashMap<String,String> getHeaderforbnus(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        // header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        //header.put("response_type","code");
        //header.put("scope","READ");
        return header;
    }

    //Create Member preet
    HashMap<String,String> getHeader1preet(){

        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }
    //Create  Device Update
    HashMap<String,String> deviceUpdateHeader(){

        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }


    //Create  Device Update for jamba
    HashMap<String,String> deviceUpdateHeaderjamba(){

        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    //Create Member for jamba
    HashMap<String,String> getHeadercreatejamba(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

//    //Create Member for jamba
//    HashMap<String,String> getHeadercreatejamba(){
//        HashMap<String,String> header=new HashMap<String, String>();
//        header.put("Content-Type","application/json");
//        header.put("Application","mobile");
//        header.put("client_id", FBConstant.client_id);
//        header.put("client_secret", FBConstant.client_secret);
//        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
//        // header.put("access_token",FBPreferences.sharedInstance(fbSdk.context).getAccessToken());
//        return header;
//    }


    HashMap<String, String> getHeaderforemail() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        return header;
    }


    //Login
    HashMap<String,String> getHeader2(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("response_type","token");
        header.put("scope","read");
        header.put("state","stateless");
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    //Loginforjamba
    HashMap<String,String> getHeaderloginjamba(String spendGoBaseUrl){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("X-Class-Signature", FBPreferences.sharedInstance(fbSdk.context).getClassSignatureforapp());
        header.put("X-Class-Key","jambamobile");
        header.put("ExternalCustomerId",FBPreferences.sharedInstance(fbSdk.context).getExternalCustomerIdforapp());
        header.put("externalAccessToken",FBPreferences.sharedInstance(fbSdk.context).getSpendGoAuthTokenforapp());
        header.put("SpendGoApiBaseUrl",spendGoBaseUrl);
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    //dont used this header its for demo
    HashMap<String,String> getHeaderloginjambatest(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("ThirdPartyName","Fishbowl");
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }




    //Get Member
    HashMap<String,String> getHeader3(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }




    //Get Member for jamba
    HashMap<String,String> getHeadermemberjamba(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }




    //Get Member Update
    HashMap<String,String> updateMemberHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }


    //
    HashMap<String,String> changePassHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    HashMap<String,String> forgetPassHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("access_token",FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("response_type","token");
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }
    //Get Member Update
    HashMap<String,String> logoutHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }


    HashMap<String,String> updateFavouriteStoreHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("response_type","token");
        // header.put("tenantid","1173");
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }

    HashMap<String, String> contactUsHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        header.put("scope", "read");


        return header;
    }

    HashMap<String, String> menuCategoryHeader() {
        HashMap<String, String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Application", "mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("access_token", FBPreferences.sharedInstance(fbSdk.context).getAccessTokenforapp());
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        //header.put("scope", "read");


        return header;
    }


    HashMap<String,String> getTokenHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("tenantid", FBConstant.client_tenantid);
     //   header.put("Accept","application/json");
        return header;
    }

    HashMap<String,String> guestUserHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id", FBConstant.client_id);
        header.put("client_secret", FBConstant.client_secret);
        header.put("deviceId", FBUtility.getAndroidDeviceID(fbSdk.context));
        header.put("tenantid", FBConstant.client_tenantid);
        return header;
    }



    public interface FBStateCallback{
        public void onStateCallback(JSONObject response, Exception error);
    }

    public interface FBCountryCallback{
        public void onCountryCallback(JSONObject response, Exception error);
    }
    public interface FBFavouriteStoreUpdateCallback{
        public void onFBFavouriteStoreUpdateCallback(JSONObject response, Exception error);
    }

    //Interface for
    public interface FBCreateMemberCallback{
        public void onCreateMemberCallback(JSONObject response, Exception error);
    }

    public interface FBLoginMemberCallback{
        public void onLoginMemberCallback(JSONObject response, Exception error);
    }

    public interface FBGetMemberCallback{
        public void onGetMemberCallback(JSONObject response, Exception error);
    }

    public interface FBMemberUpdateCallback{
        public void onMemberUpdateCallback(JSONObject response, Exception error);
    }

    public interface FBDeviceUpdateCallback{
        public void onDeviceUpdateCallback(JSONObject response, Exception error);
    }

    public interface FBChangePasswordCallback{
        public void onChangePasswordCallback(JSONObject response, Exception error);
    }
    public interface FBforgetPasswordCallback{
        public void onFBforgetPasswordCallback(JSONObject response, Exception error);
    }


    public interface FBLogoutCallback{
        public void onLogoutCallback(JSONObject response, Exception error);
    }
    public interface FBContactUsCallback{
        public void onContactUsCallback(JSONObject response, Exception error);
    }

    public interface  FBLoyaltyAreaTypeCallback{
        public void onLoyaltyAreaTypeCallback(JSONObject response, Exception error);
    }

    public interface  FBLoyaltyMessageTypeCallback{
        public void onLoyaltyMessageTypeCallback(JSONObject response, Exception error);
    }

    public interface  FBBonusRuleListCallback{
        public void onBonusRuleListCallback(JSONArray response, Exception error);
    }

    public interface FBMenuCategoryCallback{
        public void onMenuCategoryCallback(JSONObject response, Exception error);
    }
    public interface FBMenuSubCategoryCallback{
        public void onMenuSubCategoryCallback(JSONObject response, Exception error);
    }
    public interface FBMenuCategorySubCategoryCallback{
        public void onMenuCategorySubCategoryCallback(JSONObject response, Exception error);
    }

    public interface FBMenuDrawerCallback{
        public void onMenuDrawerCallback(JSONObject response, Exception error);
    }
    public interface FBMenuProductCallback{
        public void onMenuProductCallback(JSONObject response, Exception error);
    }
    public interface FBMenuCategoryProductListCallback{
        public void onMenuCategoryProductListCallback(JSONObject response, Exception error);
    }
    public interface FBFamilyApiCallback{
        public void onFamilyApiCallback(JSONObject response, Exception error);
    }
    public interface  FBLoyaltyEmailOfferDetail{
        public void onFBLoyaltyEmailOfferDetailCallback(JSONObject response, Exception error);
    }

    public interface FBAllRewardOfferCallback{
        public void onAllRewardOfferCallback(JSONObject response, Exception error);
    }

    public interface FBGetTokenCallback{
        public void onGetTokencallback(JSONObject response, Exception error);
    }

    public  interface  FBGuestUserCallback{
        public void  onGuestUserCallback(JSONObject response, Exception error);
    }

}

