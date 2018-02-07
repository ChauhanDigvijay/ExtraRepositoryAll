//<?xml version="1.0" encoding="utf-8"?>
//<manifest xmlns:android="http://schemas.android.com/apk/res/android"
//        package="com.fishbowl.BasicApp">
//
//<application
//android:name=".AuthApplication"
//        android:allowBackup="true"
//        android:icon="@mipmap/ic_launcher"
//        android:label="@string/app_name"
//        android:supportsRtl="true"
//        android:theme="@style/AppTheme">
//<permission
//android:name="com.fishbowl.mybistro.MAPS_RECEIVE"
//        android:protectionLevel="signature" />
//
//<uses-permission android:name="${applicationId}.permission.UA_DATA" />
//
//<!-- REQUIRED PERMISSIONS for GCM -->
//<uses-permission android:name="android.permission.GET_ACCOUNTS" />
//<!-- GCM requires a Google account. -->
//<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
//
//<!-- This app has permission to register with GCM and receive message -->
//<permission
//android:name="${applicationId}.permission.C2D_MESSAGE"
//        android:protectionLevel="signature" />
//
//<uses-permission android:name="${applicationId}.permission.C2D_MESSAGE" />
//<uses-permission android:name="android.permission.INTERNET" />
//<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//<uses-permission android:name="com.fishbowl.mybistro.MAPS_RECEIVE" />
//<uses-permission android:name="android.permission.INTERNET" />
//<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//<uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES" />
//<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
//<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
//<uses-permission android:name="android.permission.WAKE_LOCK" />
//<uses-permission android:name="com.fishbowl.mybistro.permission.C2D_MESSAGE" />
//<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
//<uses-permission android:name="android.permission.BLUETOOTH" />
//<uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
//<uses-permission android:name="android.permission.INTERNET" />
//<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
//<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
//<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
//<uses-permission android:name="com.clp.sdk.MAPS_RECEIVE" />
//<uses-permission android:name="android.permission.INTERNET" />
//<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
//<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
//<uses-permission android:name="com.google.android.providers.gsf.permission.READ_GSERVICES" />
//<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
//<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
//<uses-permission android:name="android.permission.WAKE_LOCK" />
//<uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
//
//<meta-data
//        android:name="com.google.android.gms.version"
//        android:value="@integer/google_play_services_version" />
//
//<uses-feature
//        android:glEsVersion="0x00020000"
//        android:required="true" />
//
//<!-- ++++++++FB SDK GCM ++++++++ -->
//<activity
//android:name="com.fishbowl.basicmodule.Controllers.FBActionActivity"
//        android:exported="true" />
//
//<meta-data
//        android:name="com.google.android.maps.v2.API_KEY"
//        android:value="AIzaSyDIUTOnzSqyPPsm_F6huZ_zhRpSAsyx2h0" />
//<meta-data
//        android:name="com.google.android.gms.version"
//        android:value="@integer/google_play_services_version" />
//<meta-data
//        android:name="com.facebook.sdk.ApplicationId"
//        android:value="@string/facebook_app_id" />
//<meta-data
//        android:name="io.fabric.ApiKey"
//        android:value="a14d6b01c9ac40ae615172475daf875def5d235e" />
//
//<receiver
//android:name="com.google.android.gms.gcm.GcmReceiver"
//        android:exported="true"
//        android:permission="com.google.android.c2dm.permission.SEND">
//<intent-filter>
//<action android:name="com.google.android.c2dm.intent.RECEIVE" />
//<action android:name="com.google.android.c2dm.intent.REGISTRATION" />
//
//<category android:name="${applicationId}" />
//</intent-filter>
//</receiver>
//
//<service
//android:name="com.fishbowl.basicmodule.Gcm.FBGCMListenerService"
//        android:exported="false">
//<intent-filter>
//<action android:name="com.google.android.c2dm.intent.RECEIVE" />
//</intent-filter>
//</service>
//<service
//android:name="com.fishbowl.basicmodule.Gcm.FBInstanceIDListenerService"
//        android:exported="false">
//<intent-filter>
//<action android:name="com.google.android.gms.iid.InstanceID" />
//</intent-filter>
//</service>
//<service
//android:name="com.fishbowl.basicmodule.Gcm.FBRegistrationIntentService"
//        android:exported="false" />
//<!-- ++++++++FB SDK GCM ++++++++ -->
//
//<activity
//android:name=".BasicMainActivity"
//        android:configChanges="orientation|keyboardHidden"
//        android:screenOrientation="portrait">
//<intent-filter>
//<action android:name="android.intent.action.MAIN" />
//
//<category android:name="android.intent.category.LAUNCHER" />
//</intent-filter>
//</activity>
//<activity
//android:name="com.BasicApp.activity.SignInActivity"
//        android:configChanges="orientation|keyboardHidden"
//        android:launchMode="singleTop"
//        android:screenOrientation="portrait"></activity>
//<activity
//android:name="com.BasicApp.activity.DashboardActivity"
//        android:configChanges="orientation|keyboardHidden"
//        android:label="@string/app_name"
//        android:screenOrientation="portrait">
//<intent-filter>
//<action android:name="android.intent.action.MAIN" />
//
//<category android:name="android.intent.category.DEFAULT" />
//</intent-filter>
//</activity>
//
//</application>
//
//</manifest>