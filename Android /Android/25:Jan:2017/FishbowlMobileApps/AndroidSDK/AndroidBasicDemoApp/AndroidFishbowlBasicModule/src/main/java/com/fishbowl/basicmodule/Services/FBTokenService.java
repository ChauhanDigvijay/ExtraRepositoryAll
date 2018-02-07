package com.fishbowl.basicmodule.Services;

import com.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Analytics.FBAnalytic;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Utils.FBConstant;
import com.fishbowl.basicmodule.Utils.FBUtility;

import org.json.JSONObject;

/**
 * Created by digvijay(dj)
 */
public class FBTokenService {

    FBSdk fbsdk = null;
    public static FBTokenService instance;
    public static int count = 0;
    int getAllStoresCounter;


    public static FBTokenService sharedInstance(FBSdk _clpsdk) {
        if (instance == null) {
            instance = new FBTokenService(_clpsdk);

        }
        return instance;
    }

    public FBTokenService(FBSdk _fbsdk) {
        this.fbsdk = _fbsdk;
    }


    public  void getTokenFBAnalytic( final String  entity )
    {
        JSONObject object=new JSONObject();
        try
        {
            object.put("clientId",FBConstant.client_id);
            object.put("clientSecret",FBConstant.client_secret);
            object.put("deviceId", FBUtility.getAndroidDeviceID(fbsdk.context));
            object.put("tenantId",FBConstant.client_tenantid);

        }catch (Exception e){
            e.printStackTrace();
        }

        FBUserService.sharedInstance().getTokenApi(object, new FBUserService.FBGetTokenCallback()
        {


            @Override
            public void onGetTokencallback(JSONObject response, Exception error) {
                if(response!=null) {
                    try {
                        if(response.has("message")) {
                            //    FBUserManager.sharedInstance().access_token = response.getString("accessToken");

                            if (count < 2) {  // test: boolean test within (..)
                                count = count + 1;
                                String secratekey = response.getString("message");
                                FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
                                //	fbSdk.saveCustomer(customer, custRegListener);
                                //instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
                                FBAnalytic.sharedInstance().onTimerAfter(entity);
                            } else {
                                count = 0;
                            }
                        }else
                        {

                            if (count < 2)
                            {
                                count=count + 1;
                                FBAnalytic.sharedInstance().onTimer();
                            }
                            else
                            {
                                count=0;
                            }
                        }}
                    catch (Exception e) {
                        e.printStackTrace();
                    }

                }else {
                    //   FBUtils.showErrorAlert(SignIn.this,error);
                }
            }

        });


    }


    //
//	public void getToken(final Context context, final String sdkPointingUrl, final FBSdk.OnFBSdkRegisterListener sdkListener, final FBConfig FBConfig, final FBCustomerItem customer){
//
//
//
//		JSONObject object=new JSONObject();
//		try {
//			//  String email=eemail.getText().toString();
//           /* Long number=Long.valueOf(email);
//            String usaNumber;
//            if(number==(long)number){
//                usaNumber=FBUtils.getPhoneDash(number);
//                object.put("username",usaNumber);
//            }else {
//                object.put("username",eemail.getText().toString());
//            }
//            */
//			object.put("username", FBConstant.guest_username);
//			object.put("password", FBConstant.guest_password);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
//			public void onLoginMemberCallback(JSONObject response, Exception error){
//
//				if(response!=null) {
//					try {
//
//
//
//						if (count < 2)
//						{  // test: boolean test within (..)
//							count=count + 1;
//
//							String secratekey=response.getString("accessToken");
//							FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
//							fbsdk.initialation(context,sdkPointingUrl, sdkListener, FBConfig, customer);
//						}else
//						{
//							count=0;
//						}
//
//						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
//
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}else {
//					//   FBUtils.showErrorAlert(SignIn.this,error);
//				}
//			}
//
//		});
//	}
//
//
//
//	public void getTokenAllStore(){
//
//		JSONObject object=new JSONObject();
//		try {
//			//  String email=eemail.getText().toString();
//           /* Long number=Long.valueOf(email);
//            String usaNumber;
//            if(number==(long)number){
//                usaNumber=FBUtils.getPhoneDash(number);
//                object.put("username",usaNumber);
//            }else {
//                object.put("username",eemail.getText().toString());
//            }
//            */
//			object.put("username", FBConstant.guest_username);
//			object.put("password", FBConstant.guest_password);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
//			public void onLoginMemberCallback(JSONObject response, Exception error){
//
//				if(response!=null) {
//					try {
//						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
//
//						if(response.has("accessToken")) {
//							if (count < 2) {  // test: boolean test within (..)
//								count = count + 1;
//
//
//								String secratekey = response.getString("accessToken");
//								FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
//								fbsdk.getAllStores();
//							} else {
//								count = 0;
//							}
//						}
//						else
//						{
//
//							if (count < 2)
//							{
//								count=count + 1;
//								fbsdk.getAllStores();
//							}
//							else
//							{
//								count=0;
//							}
//						}
//						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}else {
//					//   FBUtils.showErrorAlert(SignIn.this,error);
//				}
//			}
//
//		});
//	}
//
//
//	public  void getTokenMobileSetting()
//	{
//
//
//
//		JSONObject object=new JSONObject();
//		try {
//			//  String email=eemail.getText().toString();
//           /* Long number=Long.valueOf(email);
//            String usaNumber;
//            if(number==(long)number){
//                usaNumber=FBUtils.getPhoneDash(number);
//                object.put("username",usaNumber);
//            }else {
//                object.put("username",eemail.getText().toString());
//            }
//            */
//			object.put("username", FBConstant.guest_username);
//			object.put("password", FBConstant.guest_password);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
//			public void onLoginMemberCallback(JSONObject response, Exception error){
//
//				if(response!=null) {
//					try {
//						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
//
//						if(response.has("accessToken")) {
//							if (count < 2) {  // test: boolean test within (..)
//								count = count + 1;
//
//
//								String secratekey = response.getString("accessToken");
//								FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
//								fbsdk.mobileSettings();
//							} else {
//								count = 0;
//							}
//						}	else
//							{
//
//								if (count < 2)
//								{
//									count=count + 1;
//									fbsdk.mobileSettings();
//								}
//								else
//								{
//									count=0;
//								}
//							}
//						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}else {
//					//   FBUtils.showErrorAlert(SignIn.this,error);
//				}
//			}
//
//		});
//
//
//	}
//
//	public  void getTokenRegisterCustomer(final FBCustomerItem customer,
//										  final FBSdk.OnFBSdkRegisterListener sdkRegListener)
//
//	{
//
//
//
//
//		JSONObject object=new JSONObject();
//		try {
//			//  String email=eemail.getText().toString();
//           /* Long number=Long.valueOf(email);
//            String usaNumber;
//            if(number==(long)number){
//                usaNumber=FBUtils.getPhoneDash(number);
//                object.put("username",usaNumber);
//            }else {
//                object.put("username",eemail.getText().toString());
//            }
//            */
//			object.put("username", FBConstant.guest_username);
//			object.put("password", FBConstant.guest_password);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
//			public void onLoginMemberCallback(JSONObject response, Exception error){
//
//				if(response!=null) {
//
//
//					try {
//						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
//
//						if(response.has("accessToken")) {
//							if (count < 2) {  // test: boolean test within (..)
//								count = count + 1;
//								String secratekey = response.getString("accessToken");
//								FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
//								fbsdk.registerCustomer(customer, sdkRegListener);
//
//
//							} else {
//								count = 0;
//							}
//						}else
//						{
//
//							if (count < 2)
//							{
//								count=count + 1;
//								fbsdk.registerCustomer(customer, sdkRegListener);
//							}
//							else
//							{
//								count=0;
//							}
//						}
//
//						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}else {
//					//   FBUtils.showErrorAlert(SignIn.this,error);
//				}
//			}
//
//		});
//	}
//
//
//	public  void getTokenSaveCustomer(final FBCustomerItem customer,
//									  final FBSdk.OnFBCustomerRegisterListener custRegListener)
//	{
//
//
//		JSONObject object=new JSONObject();
//		try {
//			//  String email=eemail.getText().toString();
//           /* Long number=Long.valueOf(email);
//            String usaNumber;
//            if(number==(long)number){
//                usaNumber=FBUtils.getPhoneDash(number);
//                object.put("username",usaNumber);
//            }else {
//                object.put("username",eemail.getText().toString());
//            }
//            */
//			object.put("username", FBConstant.guest_username);
//			object.put("password", FBConstant.guest_password);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
//			public void onLoginMemberCallback(JSONObject response, Exception error){
//
//				if(response!=null) {
//					try {
//						if(response.has("accessToken")) {
//						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
//
//						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
//
//						if (count < 2)
//						{  // test: boolean test within (..)
//							count=count + 1;
//
//
//							String secratekey=response.getString("accessToken");
//							FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
//							fbsdk.saveCustomer(customer, custRegListener);
//
//						}else
//						{
//							count=0;
//						}
//						}else
//						{
//
//							if (count < 2)
//							{
//								count=count + 1;
//								fbsdk.saveCustomer(customer, custRegListener);
//							}
//							else
//							{
//								count=0;
//							}
//						}
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}else {
//					//   FBUtils.showErrorAlert(SignIn.this,error);
//				}
//			}
//
//		});
//	}
//
//    public void getTokenClpAnalytic() {
//        JSONObject object = new JSONObject();
//        try {
//            //  String email=eemail.getText().toString();
//           /* Long number=Long.valueOf(email);
//            String usaNumber;
//            if(number==(long)number){
//                usaNumber=FBUtils.getPhoneDash(number);
//                object.put("username",usaNumber);
//            }else {
//                object.put("username",eemail.getText().toString());
//            }
//            */
//            object.put("username", FBConstant.guest_username);
//            object.put("password", FBConstant.guest_password);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
//			public void onLoginMemberCallback(JSONObject response, Exception error){
//
//				if(response!=null) {
//					try {
//						if(response.has("accessToken")) {
//						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
//
//						if (count < 2) {  // test: boolean test within (..)
//							count = count + 1;
//							String secratekey = response.getString("accessToken");
//							FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
//							//	fbSdk.saveCustomer(customer, custRegListener);
//							//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
//							FBAnalytic.sharedInstance().onTimer();
//						} else {
//							count = 0;
//						}
//					}else
//					{
//
//						if (count < 2)
//						{
//							count=count + 1;
//							FBAnalytic.sharedInstance().onTimer();
//						}
//						else
//						{
//							count=0;
//						}
//					}}
//						catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}else {
//					//   FBUtils.showErrorAlert(SignIn.this,error);
//				}
//			}
//
//		});
//
//
//	}
//
//
//	public  void getTokenUserClypOffer(final JSONObject offer, final String customId, final FBOfferService.FBOfferCallback callback)
//	{
//		JSONObject object=new JSONObject();
//		try {
//			//  String email=eemail.getText().toString();
//           /* Long number=Long.valueOf(email);
//            String usaNumber;
//            if(number==(long)number){
//                usaNumber=FBUtils.getPhoneDash(number);
//                object.put("username",usaNumber);
//            }else {
//                object.put("username",eemail.getText().toString());
//            }
//            */
//			object.put("username", FBConstant.guest_username);
//			object.put("password", FBConstant.guest_password);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
//			public void onLoginMemberCallback(JSONObject response, Exception error){
//
//				if(response!=null) {
//					try {
//						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
//
//						if(response.has("accessToken")) {
//							if (count < 2) {  // test: boolean test within (..)
//								count = count + 1;
//								String secratekey = response.getString("accessToken");
//								FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
//								FBOfferService.sharedInstance().getFBOffer(offer, customId, callback);
//
//
//							} else {
//								count = 0;
//							}
//						}else
//							{
//
//								if (count < 2)
//								{
//									count=count + 1;
//									FBOfferService.sharedInstance().getFBOffer(offer, customId, callback);
//								}
//								else
//								{
//									count=0;
//								}
//							}
//
//
//						//	fbSdk.saveCustomer(customer, custRegListener);
//						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}else {
//					//   FBUtils.showErrorAlert(SignIn.this,error);
//				}
//			}
//
//		});
//
//
//	}
//
//
//
//	public  void getTokenUserClypOfferByOfferId(final JSONObject offerpromo, final String itemId, final FBOfferService.FBOfferPushCallback callback)
//	{
//		JSONObject object=new JSONObject();
//		try {
//			//  String email=eemail.getText().toString();
//           /* Long number=Long.valueOf(email);
//            String usaNumber;
//            if(number==(long)number){
//                usaNumber=FBUtils.getPhoneDash(number);
//                object.put("username",usaNumber);
//            }else {
//                object.put("username",eemail.getText().toString());
//            }
//            */
//			object.put("username", FBConstant.guest_username);
//			object.put("password", FBConstant.guest_password);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
//			public void onLoginMemberCallback(JSONObject response, Exception error){
//
//				if(response!=null) {
//					try {
//						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
//
//
//						//	fbSdk.saveCustomer(customer, custRegListener);
//						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
//
//
//						if(response.has("accessToken")) {
//
//							if (count < 2) {  // test: boolean test within (..)
//								count = count + 1;
//								String secratekey = response.getString("accessToken");
//								FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
//								FBOfferService.sharedInstance().getFBOfferByOfferId(offerpromo, itemId, callback);
//
//							} else {
//								count = 0;
//							}
//						}else
//							{
//
//								if (count < 2)
//								{
//									count=count + 1;
//									FBOfferService.sharedInstance().getFBOfferByOfferId(offerpromo,itemId,callback);
//								}
//								else
//								{
//									count=0;
//								}
//							}
//
//
//							//	fbSdk.saveCustomer(customer, custRegListener);
//							//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}else {
//					//   FBUtils.showErrorAlert(SignIn.this,error);
//				}
//			}
//
//		});
//
//
//	}
//
////its now deprecated from production server
//
////
////	public  void getTokenUserClypOfferPromo(final JSONObject offerpromo, final String itemId, final Boolean isPMIntegrated, final FBOfferService.FBOfferPromoCallback callback)
////	{
////		JSONObject object=new JSONObject();
////		try {
////			//  String email=eemail.getText().toString();
////           /* Long number=Long.valueOf(email);
////            String usaNumber;
////            if(number==(long)number){
////                usaNumber=FBUtils.getPhoneDash(number);
////                object.put("username",usaNumber);
////            }else {
////                object.put("username",eemail.getText().toString());
////            }
////            */
////			object.put("username", FBConstant.guest_username);
////			object.put("password", FBConstant.guest_password);
////		}catch (Exception e){
////			e.printStackTrace();
////		}
////
////		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
////			public void onLoginMemberCallback(JSONObject response, Exception error){
////
////				if(response!=null) {
////					try {
////						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
////
////						if(response.has("accessToken")) {
////							if (count < 2) {  // test: boolean test within (..)
////								count = count + 1;
////
////								String secratekey = response.getString("accessToken");
////								FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
////								FBOfferService.sharedInstance().getFBOfferPromo(offerpromo, itemId, isPMIntegrated, callback);
////
////							} else {
////								count = 0;
////							}
////						}else
////							{
////
////								if (count < 2)
////								{
////									count=count + 1;
////									FBOfferService.sharedInstance().getFBOfferPromo(offerpromo, itemId, isPMIntegrated, callback);
////								}
////								else
////								{
////									count=0;
////								}
////							}
////
////
////						//	fbSdk.saveCustomer(customer, custRegListener);
////						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
////					} catch (Exception e) {
////						e.printStackTrace();
////					}
////
////				}else {
////					//   FBUtils.showErrorAlert(SignIn.this,error);
////				}
////			}
////
////		});
////
////
////	}
//
//
//	public  void getTokenUserClypOfferPass(final JSONObject offerpromo, final String offerId, final boolean isPMIntegrated, final FBOfferService.FBOfferPassCallback callback)
//	{
//		JSONObject object=new JSONObject();
//		try {
//			//  String email=eemail.getText().toString();
//           /* Long number=Long.valueOf(email);
//            String usaNumber;
//            if(number==(long)number){
//                usaNumber=FBUtils.getPhoneDash(number);
//                object.put("username",usaNumber);
//            }else {
//                object.put("username",eemail.getText().toString());
//            }
//            */
//			object.put("username", FBConstant.guest_username);
//			object.put("password", FBConstant.guest_password);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
//			public void onLoginMemberCallback(JSONObject response, Exception error){
//
//				if(response!=null) {
//					try {
//						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
//
//						if(response.has("accessToken")) {
//							if (count < 2) {  // test: boolean test within (..)
//								count = count + 1;
//
//								String secratekey = response.getString("accessToken");
//								FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
//								FBOfferService.sharedInstance().getFBOfferPass(offerpromo, offerId, isPMIntegrated, callback);
//
//							} else {
//								count = 0;
//							}
//						}else
//							{
//
//								if (count < 2)
//								{
//									count=count + 1;
//									FBOfferService.sharedInstance().getFBOfferPass(offerpromo, offerId, isPMIntegrated, callback);
//								}
//								else
//								{
//									count=0;
//								}
//							}
//
//
//
//						//	fbSdk.saveCustomer(customer, custRegListener);
//						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}else {
//					//   FBUtils.showErrorAlert(SignIn.this,error);
//				}
//			}
//
//		});
//
//
//	}
//
//
//	public  void getTokenClypRedeemedOffer(final JSONObject offer,final String itemId,final FBOfferService.FBRedeemedCallback callback)
//	{
//		JSONObject object=new JSONObject();
//		try {
//
//			object.put("username", FBConstant.guest_username);
//			object.put("password", FBConstant.guest_password);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
//			public void onLoginMemberCallback(JSONObject response, Exception error){
//
//				if(response!=null) {
//					try {
//						if(response.has("accessToken")) {
//						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
//
//
//						if (count < 2)
//						{  // test: boolean test within (..)
//							count=count + 1;
//							String secratekey=response.getString("accessToken");
//							FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
//							FBOfferService.sharedInstance().getFBRedeemedOffer(offer,itemId,callback);
//
//
//						}else
//						{
//							count=0;
//						}
//
//						}else
//						{
//
//							if (count < 2)
//							{
//								count=count + 1;
//								FBOfferService.sharedInstance().getFBRedeemedOffer(offer,itemId,callback);
//							}
//							else
//							{
//								count=0;
//							}
//						}
//
//
//
//						//	fbSdk.saveCustomer(customer, custRegListener);
//						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
//
//
//						//	fbSdk.saveCustomer(customer, custRegListener);
//						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}else {
//					//   FBUtils.showErrorAlert(SignIn.this,error);
//				}
//			}
//
//		});
//
//
//	}
//
//
//
//	public  void getTokendeviceUpdate(final FBCustomerItem customer)
//	{
//		JSONObject object=new JSONObject();
//		try {
//			//  String email=eemail.getText().toString();
//           /* Long number=Long.valueOf(email);
//            String usaNumber;
//            if(number==(long)number){
//                usaNumber=FBUtils.getPhoneDash(number);
//                object.put("username",usaNumber);
//            }else {
//                object.put("username",eemail.getText().toString());
//            }
//            */
//			object.put("username", FBConstant.guest_username);
//			object.put("password", FBConstant.guest_password);
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//
//		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
//			public void onLoginMemberCallback(JSONObject response, Exception error){
//
//				if(response!=null) {
//					try {
//						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
//						if(response.has("accessToken")) {
//
//						if (count < 2)
//						{  // test: boolean test within (..)
//							count=count + 1;
//
//							String secratekey=response.getString("accessToken");
//							FBPreferences.sharedInstance(fbsdk.context).setAccessToken(secratekey);
//							FBCustomerService.sharedInstance().deviceUpdate(customer);
//
//						}else
//						{
//							count=0;
//						}
//
//						}else
//						{
//
//							if (count < 2)
//							{
//								count=count + 1;
//								FBCustomerService.sharedInstance().deviceUpdate(customer);
//							}
//							else
//							{
//								count=0;
//							}
//						}
//
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//				}else {
//					//   FBUtils.showErrorAlert(SignIn.this,error);
//				}
//			}
//
//		});
//
//
//	}

    }

