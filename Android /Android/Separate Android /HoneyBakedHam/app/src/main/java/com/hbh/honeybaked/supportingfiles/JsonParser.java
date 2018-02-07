package com.hbh.honeybaked.supportingfiles;

import android.util.Log;

import com.fishbowl.basicmodule.Utils.FBConstant;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class JsonParser {
    static InputStream is = null;
    static JSONArray jArr = null;
    static JSONObject jObj = null;
    static String json = "";

    public String getStringFromUrl(String url, String deviceId, String acc_token) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(url);
          //  httppost.setHeader(HttpRequest.HEADER_CONTENT_TYPE, "application/json");
            httppost.setHeader("Application", "mobilesdk");
            httppost.setHeader("client_id", FBConstant.client_id);
            httppost.setHeader("access_token", acc_token);
            httppost.setHeader("tenantid", FBConstant.client_tenantid);
            httppost.setHeader("deviceId", deviceId);
            is = httpClient.execute(httppost).getEntity().getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return getStringFromInputStream(is);
    }

    public String getStringFromInputStream(InputStream inputStream) {
        String sResponse = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line + "\n");
            }
            sResponse = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        return sResponse;
    }

    public String getMobileEventsFromUrl(String url, String deviceId, String acc_token, JSONObject jsonObject) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
           // httppost.setHeader(HttpRequest.HEADER_CONTENT_TYPE, "application/json");
            httppost.setHeader("Application", "mobilesdk");
            httppost.setHeader("client_id", FBConstant.client_id);
            httppost.setHeader("ClientSecret", FBConstant.client_secret);
            httppost.setHeader("access_token", acc_token);
            httppost.setHeader("tenantid", FBConstant.client_tenantid);
            httppost.setHeader("deviceId", deviceId);
            httppost.setEntity(new StringEntity(jsonObject.toString()));
            is = httpClient.execute(httppost).getEntity().getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return getStringFromInputStream(is);
    }

    public String deleteAPICall(String url, String cookie) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpDelete httppost = new HttpDelete(url);
            httppost.setHeader("Cookie", cookie);
            is = httpClient.execute(httppost).getEntity().getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return getStringFromInputStream(is);
    }

    public String putAPICall(String url, JSONObject jsonObject, String cookie) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPut httppost = new HttpPut(url);
            httppost.setHeader("Cookie", cookie);
            StringEntity se = new StringEntity(jsonObject.toString());
            //se.setContentType(new BasicHeader(HttpRequest.HEADER_CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);
            is = httpClient.execute(httppost).getEntity().getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return getStringFromInputStream(is);
    }

    public String editAPICall(String url, JSONObject jsonObject, String cookie) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPut httppost = new HttpPut(url);
            httppost.setHeader("Cookie", cookie);
            StringEntity se = new StringEntity(jsonObject.toString());
           // se.setContentType(new BasicHeader(HttpRequest.HEADER_CONTENT_TYPE, "application/json"));
            httppost.setEntity(se);
            is = httpClient.execute(httppost).getEntity().getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return getStringFromInputStream(is);
    }
}
