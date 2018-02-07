package com.fishbowl.basicmodule.Services;

import android.content.Context;

import com.fishbowl.basicmodule.Analytics.FBAnalytic;
import com.fishbowl.basicmodule.Controllers.FBSdk;
import com.fishbowl.basicmodule.Interfaces.FBLoginMemberCallback;
import com.fishbowl.basicmodule.Models.FBConfig;
import com.fishbowl.basicmodule.Models.FBCustomerItem;
import com.fishbowl.basicmodule.Preferences.FBPreferences;
import com.fishbowl.basicmodule.Utils.FBConstant;

import org.json.JSONObject;
/**
 * Created by digvijay(dj)
 */
public class FBTokenService {

	FBSdk clpsdk=null;
	public static FBTokenService instance;
	public static int count = 0;
	int getAllStoresCounter;



	public static FBTokenService sharedInstance(FBSdk _clpsdk) {
		if(instance==null){
			instance=new FBTokenService(_clpsdk);

		}
		return instance;
	}

	public FBTokenService(FBSdk _clpsdk) {
		this.clpsdk=_clpsdk;
	}


	public void getToken(final Context context, final String sdkPointingUrl, final FBSdk.OnFBSdkRegisterListener sdkListener, final FBConfig FBConfig, final FBCustomerItem customer){



		JSONObject object=new JSONObject();
		try {
			//  String email=eemail.getText().toString();
           /* Long number=Long.valueOf(email);
            String usaNumber;
            if(number==(long)number){
                usaNumber=FBUtils.getPhoneDash(number);
                object.put("username",usaNumber);
            }else {
                object.put("username",eemail.getText().toString());
            }
            */
			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {
					try {



						if (count < 2)
						{  // test: boolean test within (..)
							count=count + 1;

							String secratekey=response.getString("accessToken");
							FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
							clpsdk.initialation(context,sdkPointingUrl, sdkListener, FBConfig, customer);
						}else
						{
							count=0;
						}

						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");


					} catch (Exception e) {
						e.printStackTrace();
					}

				}else {
					//   FBUtils.showErrorAlert(SignIn.this,error);
				}
			}

		});
	}



	public void getTokenAllStore(){

		JSONObject object=new JSONObject();
		try {
			//  String email=eemail.getText().toString();
           /* Long number=Long.valueOf(email);
            String usaNumber;
            if(number==(long)number){
                usaNumber=FBUtils.getPhoneDash(number);
                object.put("username",usaNumber);
            }else {
                object.put("username",eemail.getText().toString());
            }
            */
			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {
					try {
						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");

						if(response.has("accessToken")) {
							if (count < 2) {  // test: boolean test within (..)
								count = count + 1;


								String secratekey = response.getString("accessToken");
								FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
								clpsdk.getAllStores();
							} else {
								count = 0;
							}
						}
						else
						{

							if (count < 2)
							{
								count=count + 1;
								clpsdk.getAllStores();
							}
							else
							{
								count=0;
							}
						}
						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}else {
					//   FBUtils.showErrorAlert(SignIn.this,error);
				}
			}

		});
	}


	public  void getTokenMobileSetting()
	{



		JSONObject object=new JSONObject();
		try {
			//  String email=eemail.getText().toString();
           /* Long number=Long.valueOf(email);
            String usaNumber;
            if(number==(long)number){
                usaNumber=FBUtils.getPhoneDash(number);
                object.put("username",usaNumber);
            }else {
                object.put("username",eemail.getText().toString());
            }
            */
			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {
					try {
						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");

						if(response.has("accessToken")) {
							if (count < 2) {  // test: boolean test within (..)
								count = count + 1;


								String secratekey = response.getString("accessToken");
								FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
								clpsdk.mobileSettings();
							} else {
								count = 0;
							}
						}	else
							{

								if (count < 2)
								{
									count=count + 1;
									clpsdk.mobileSettings();
								}
								else
								{
									count=0;
								}
							}
						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}else {
					//   FBUtils.showErrorAlert(SignIn.this,error);
				}
			}

		});


	}

	public  void getTokenRegisterCustomer(final FBCustomerItem customer,
										  final FBSdk.OnFBSdkRegisterListener sdkRegListener)

	{




		JSONObject object=new JSONObject();
		try {
			//  String email=eemail.getText().toString();
           /* Long number=Long.valueOf(email);
            String usaNumber;
            if(number==(long)number){
                usaNumber=FBUtils.getPhoneDash(number);
                object.put("username",usaNumber);
            }else {
                object.put("username",eemail.getText().toString());
            }
            */
			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {

				
					try {
						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");

						if(response.has("accessToken")) {
							if (count < 2) {  // test: boolean test within (..)
								count = count + 1;
								String secratekey = response.getString("accessToken");
								FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
								clpsdk.registerCustomer(customer, sdkRegListener);


							} else {
								count = 0;
							}
						}else
						{

							if (count < 2)
							{
								count=count + 1;
								clpsdk.registerCustomer(customer, sdkRegListener);
							}
							else
							{
								count=0;
							}
						}

						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}else {
					//   FBUtils.showErrorAlert(SignIn.this,error);
				}
			}

		});
	}


	public  void getTokenSaveCustomer(final FBCustomerItem customer,
									  final FBSdk.OnFBCustomerRegisterListener custRegListener)
	{


		JSONObject object=new JSONObject();
		try {
			//  String email=eemail.getText().toString();
           /* Long number=Long.valueOf(email);
            String usaNumber;
            if(number==(long)number){
                usaNumber=FBUtils.getPhoneDash(number);
                object.put("username",usaNumber);
            }else {
                object.put("username",eemail.getText().toString());
            }
            */
			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {
					try {
						if(response.has("accessToken")) {
						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");

						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);

						if (count < 2)
						{  // test: boolean test within (..)
							count=count + 1;


							String secratekey=response.getString("accessToken");
							FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
							clpsdk.saveCustomer(customer, custRegListener);

						}else
						{
							count=0;
						}
						}else
						{

							if (count < 2)
							{
								count=count + 1;
								clpsdk.saveCustomer(customer, custRegListener);
							}
							else
							{
								count=0;
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}else {
					//   FBUtils.showErrorAlert(SignIn.this,error);
				}
			}

		});
	}

	public  void getTokenClpAnalytic()
	{
		JSONObject object=new JSONObject();
		try {
			//  String email=eemail.getText().toString();
           /* Long number=Long.valueOf(email);
            String usaNumber;
            if(number==(long)number){
                usaNumber=FBUtils.getPhoneDash(number);
                object.put("username",usaNumber);
            }else {
                object.put("username",eemail.getText().toString());
            }
            */
			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {
					try {
						if(response.has("accessToken")) {
						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");

						if (count < 2) {  // test: boolean test within (..)
							count = count + 1;
							String secratekey = response.getString("accessToken");
							FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
							//	fbSdk.saveCustomer(customer, custRegListener);
							//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
							FBAnalytic.sharedInstance().onTimer();
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


	public  void getTokenUserClypOffer(final JSONObject offer, final String customId, final FBOfferService.FBOfferCallback callback)
	{
		JSONObject object=new JSONObject();
		try {
			//  String email=eemail.getText().toString();
           /* Long number=Long.valueOf(email);
            String usaNumber;
            if(number==(long)number){
                usaNumber=FBUtils.getPhoneDash(number);
                object.put("username",usaNumber);
            }else {
                object.put("username",eemail.getText().toString());
            }
            */
			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {
					try {
						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");

						if(response.has("accessToken")) {
							if (count < 2) {  // test: boolean test within (..)
								count = count + 1;
								String secratekey = response.getString("accessToken");
								FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
								FBOfferService.sharedInstance().getFBOffer(offer, customId, callback);


							} else {
								count = 0;
							}
						}else
							{

								if (count < 2)
								{
									count=count + 1;
									FBOfferService.sharedInstance().getFBOffer(offer, customId, callback);
								}
								else
								{
									count=0;
								}
							}


						//	fbSdk.saveCustomer(customer, custRegListener);
						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}else {
					//   FBUtils.showErrorAlert(SignIn.this,error);
				}
			}

		});


	}



	public  void getTokenUserClypOfferByOfferId(final JSONObject offerpromo, final String itemId, final FBOfferService.ClypOfferPushCallback callback)
	{
		JSONObject object=new JSONObject();
		try {
			//  String email=eemail.getText().toString();
           /* Long number=Long.valueOf(email);
            String usaNumber;
            if(number==(long)number){
                usaNumber=FBUtils.getPhoneDash(number);
                object.put("username",usaNumber);
            }else {
                object.put("username",eemail.getText().toString());
            }
            */
			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {
					try {
						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");


						//	fbSdk.saveCustomer(customer, custRegListener);
						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);


						if(response.has("accessToken")) {

							if (count < 2) {  // test: boolean test within (..)
								count = count + 1;
								String secratekey = response.getString("accessToken");
								FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
								FBOfferService.sharedInstance().getFBOfferByOfferId(offerpromo, itemId, callback);

							} else {
								count = 0;
							}
						}else
							{

								if (count < 2)
								{
									count=count + 1;
									FBOfferService.sharedInstance().getFBOfferByOfferId(offerpromo,itemId,callback);
								}
								else
								{
									count=0;
								}
							}


							//	fbSdk.saveCustomer(customer, custRegListener);
							//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);

					} catch (Exception e) {
						e.printStackTrace();
					}

				}else {
					//   FBUtils.showErrorAlert(SignIn.this,error);
				}
			}

		});


	}




	public  void getTokenUserClypOfferPromo(final JSONObject offerpromo, final String itemId, final Boolean isPMIntegrated, final FBOfferService.FBOfferPromoCallback callback)
	{
		JSONObject object=new JSONObject();
		try {
			//  String email=eemail.getText().toString();
           /* Long number=Long.valueOf(email);
            String usaNumber;
            if(number==(long)number){
                usaNumber=FBUtils.getPhoneDash(number);
                object.put("username",usaNumber);
            }else {
                object.put("username",eemail.getText().toString());
            }
            */
			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {
					try {
						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");

						if(response.has("accessToken")) {
							if (count < 2) {  // test: boolean test within (..)
								count = count + 1;

								String secratekey = response.getString("accessToken");
								FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
								FBOfferService.sharedInstance().getFBOfferPromo(offerpromo, itemId, isPMIntegrated, callback);

							} else {
								count = 0;
							}
						}else
							{

								if (count < 2)
								{
									count=count + 1;
									FBOfferService.sharedInstance().getFBOfferPromo(offerpromo, itemId, isPMIntegrated, callback);
								}
								else
								{
									count=0;
								}
							}


						//	fbSdk.saveCustomer(customer, custRegListener);
						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}else {
					//   FBUtils.showErrorAlert(SignIn.this,error);
				}
			}

		});


	}


	public  void getTokenUserClypOfferPass(final JSONObject offerpromo, final String offerId, final boolean isPMIntegrated, final FBOfferService.FBOfferPassCallback callback)
	{
		JSONObject object=new JSONObject();
		try {
			//  String email=eemail.getText().toString();
           /* Long number=Long.valueOf(email);
            String usaNumber;
            if(number==(long)number){
                usaNumber=FBUtils.getPhoneDash(number);
                object.put("username",usaNumber);
            }else {
                object.put("username",eemail.getText().toString());
            }
            */
			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {
					try {
						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");

						if(response.has("accessToken")) {
							if (count < 2) {  // test: boolean test within (..)
								count = count + 1;

								String secratekey = response.getString("accessToken");
								FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
								FBOfferService.sharedInstance().getFBOfferPass(offerpromo, offerId, isPMIntegrated, callback);

							} else {
								count = 0;
							}
						}else
							{

								if (count < 2)
								{
									count=count + 1;
									FBOfferService.sharedInstance().getFBOfferPass(offerpromo, offerId, isPMIntegrated, callback);
								}
								else
								{
									count=0;
								}
							}



						//	fbSdk.saveCustomer(customer, custRegListener);
						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}else {
					//   FBUtils.showErrorAlert(SignIn.this,error);
				}
			}

		});


	}


	public  void getTokenClypRedeemedOffer(final JSONObject offer,final String itemId,final FBOfferService.FBRedeemedCallback callback)
	{
		JSONObject object=new JSONObject();
		try {

			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {
					try {
						if(response.has("accessToken")) {
						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");


						if (count < 2)
						{  // test: boolean test within (..)
							count=count + 1;
							String secratekey=response.getString("accessToken");
							FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
							FBOfferService.sharedInstance().getFBRedeemedOffer(offer,itemId,callback);


						}else
						{
							count=0;
						}

						}else
						{

							if (count < 2)
							{
								count=count + 1;
								FBOfferService.sharedInstance().getFBRedeemedOffer(offer,itemId,callback);
							}
							else
							{
								count=0;
							}
						}



						//	fbSdk.saveCustomer(customer, custRegListener);
						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);


						//	fbSdk.saveCustomer(customer, custRegListener);
						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}else {
					//   FBUtils.showErrorAlert(SignIn.this,error);
				}
			}

		});


	}



	public  void getTokendeviceUpdate(final FBCustomerItem customer)
	{
		JSONObject object=new JSONObject();
		try {
			//  String email=eemail.getText().toString();
           /* Long number=Long.valueOf(email);
            String usaNumber;
            if(number==(long)number){
                usaNumber=FBUtils.getPhoneDash(number);
                object.put("username",usaNumber);
            }else {
                object.put("username",eemail.getText().toString());
            }
            */
			object.put("username", FBConstant.guest_username);
			object.put("password", FBConstant.guest_password);
		}catch (Exception e){
			e.printStackTrace();
		}

		FBService.getInstance().loginMember(object, new FBLoginMemberCallback(){
			public void onLoginMemberCallback(JSONObject response, Exception error){

				if(response!=null) {
					try {
						//    FBUserManager.sharedInstance().access_token = response.getString("accessToken");
						if(response.has("accessToken")) {

						if (count < 2)
						{  // test: boolean test within (..)
							count=count + 1;

							String secratekey=response.getString("accessToken");
							FBPreferences.sharedInstance(clpsdk.context).setAccessToken(secratekey);
							FBCustomerService.sharedInstance().deviceUpdate(customer);

						}else
						{
							count=0;
						}

						}else
						{

							if (count < 2)
							{
								count=count + 1;
								FBCustomerService.sharedInstance().deviceUpdate(customer);
							}
							else
							{
								count=0;
							}
						}


						//	fbSdk.saveCustomer(customer, custRegListener);
						//instance.initialation(context,sdkPointingUrl, sdkListener, clpConfig, customer);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}else {
					//   FBUtils.showErrorAlert(SignIn.this,error);
				}
			}

		});


	}

}
