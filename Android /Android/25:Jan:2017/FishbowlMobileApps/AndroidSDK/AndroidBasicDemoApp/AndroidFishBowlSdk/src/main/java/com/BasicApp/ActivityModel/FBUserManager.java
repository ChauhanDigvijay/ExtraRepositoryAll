package com.BasicApp.ActivityModel;

import com.BasicApp.BusinessLogic.Models.Member;
import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Controllers.FBSdkData;
import com.fishbowl.basicmodule.Interfaces.FBServiceCallback;
import com.fishbowl.basicmodule.Services.FBService;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by mohdvaseem on 27/05/16.
 */

public class FBUserManager {

    public Member member;
    private FBSdk clpsdk;
    public static FBUserManager instance ;


    public String access_token=null;


    public static FBUserManager sharedInstance(){
        if(instance==null){
            instance=new FBUserManager();

        }
        return instance;
    }

    public void init(FBSdk _clpsdk){
        clpsdk=_clpsdk;
        member=new Member();

    }

    public void createMember(JSONObject parameter,final FBCreateMemberCallback callback){

        final FBSdkData FBSdkData = clpsdk.getFBSdkData();

        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
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


    public void loginMember(JSONObject parameter,final FBLoginMemberCallback callback){


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

    public void getMember(JSONObject parameter,final FBGetMemberCallback callback){

        String str=parameter.toString();

        FBService.getInstance().get(FBConstant.FBGetMemberApi,null, getHeader3(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response,Exception  error,String errorMessage){

                if(error==null&&response!=null){

                    member.initWithJson(response);

                    callback.onGetMemberCallback(response,null);
                }else{
                    callback.onGetMemberCallback(null,error);
                }

            }
        });

    }


    public void memberUpdate(JSONObject parameter,final FBMemberUpdateCallback callback){
        final FBSdkData FBSdkData = clpsdk.getFBSdkData();

        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
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
        final FBSdkData FBSdkData = clpsdk.getFBSdkData();

        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
            return;
        }
        String str=parameter.toString();
        FBService.getInstance().post(FBConstant.DeviceUpdateApi,str, deviceUpdateHeader(), new FBServiceCallback(){

            public void onServiceCallback(JSONObject response, Exception error,String errorMessage){

                if(error==null&&response!=null){
                    //member.initWithJson(response);
                    callback.onDeviceUpdateCallback(response,null);
                }else {
                    callback.onDeviceUpdateCallback(null,error);
                }
            }
        });

    }






    public void changePassword(JSONObject parameter,final FBChangePasswordCallback callback){
        final FBSdkData FBSdkData = clpsdk.getFBSdkData();

        if (!FBUtility.isNetworkAvailable(clpsdk.context)) {
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


    public void logout(JSONObject parameter,final FBLogoutCallback callback){

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

      /*
     *Jamba:
      ClientID: 201969E1BFD242E189FE7B6297B1B5A5
      ClientSecret: C65A0DC0F28C469FB7376F972DEFBCB8

      Deltaco:
      ClientID: 201969E1BFD242E189FE7B6297B1B5A6
      ClientSecret: C65A0DC0F28C469FB7376F972DEFBCB7
      */

        //Create Member
 HashMap<String,String> getHeader1(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobile");
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
        header.put("tenantid","1173");
        //header.put("response_type","code");
        //header.put("scope","READ");
        return header;
 }
    //Create Member preet
    HashMap<String,String> getHeader1preet(){

         HashMap<String,String> header=new HashMap<String, String>();
         header.put("Content-Type","application/json");
         header.put("Application","mobilesdk");
         header.put("tenantid","1173");
         return header;
    }
    //Create  Device Update
    HashMap<String,String> deviceUpdateHeader(){

        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobilesdk");
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        header.put("access_token",FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid","1173");
        return header;
    }




    //Login
    HashMap<String,String> getHeader2(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","ipad");
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
        header.put("response_type","token");
        header.put("scope","read");
        header.put("state","stateless");
        header.put("tenantid","1173");
        return header;
    }


    //Get Member
    HashMap<String,String> getHeader3(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","ipad");
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        header.put("access_token",FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid","1173");
        return header;
    }

    //Get Member Update
    HashMap<String,String> updateMemberHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobile");
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
        header.put("access_token", FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid","1173");
        return header;
    }


    //
    HashMap<String,String> changePassHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobile");
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
        header.put("access_token",FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid","1173");
        return header;
    }

    //Get Member Update
    HashMap<String,String> logoutHeader(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        header.put("client_secret","C65A0DC0F28C469FB7376F972DEFBCB8");
        header.put("access_token",FBPreferences.sharedInstance(clpsdk.context).getAccessTokenforapp());
        header.put("tenantid","1173");
        return header;
    }

    //Login

    /*HashMap<String,String> getHeaderNAT(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobile");
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        return header;
    }

    HashMap<String,String> getHeaderAT(){
        HashMap<String,String> header=new HashMap<String, String>();
        header.put("Content-Type","application/json");
        header.put("Application","mobile");
        header.put("client_id","201969E1BFD242E189FE7B6297B1B5A5");
        header.put("access_token",access_token);
        return header;
    }
*/

/*
Jamba:
ClientID: 201969E1BFD242E189FE7B6297B1B5A5
ClientSecret: C65A0DC0F28C469FB7376F972DEFBCB8

Deltaco:
ClientID: 201969E1BFD242E189FE7B6297B1B5A6
ClientSecret: C65A0DC0F28C469FB7376F972DEFBCB7
*/


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
    public interface FBLogoutCallback{
        public void onLogoutCallback(JSONObject response, Exception error);
    }



}
