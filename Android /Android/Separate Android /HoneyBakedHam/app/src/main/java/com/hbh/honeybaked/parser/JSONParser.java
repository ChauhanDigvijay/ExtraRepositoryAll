package com.hbh.honeybaked.parser;

import android.util.Log;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    public String getStringFromPostUrl(String url, JSONObject jsonObject, HashMap<String, String> header_map) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            List<String> key_list = new ArrayList();
            for (String key : header_map.keySet()) {
                key_list.add(key);
            }
            for (int hm = 0; hm < header_map.size(); hm++) {
                httppost.setHeader((String) key_list.get(hm), (String) header_map.get(key_list.get(hm)));
            }
            StringEntity se = new StringEntity(jsonObject.toString(), "UTF-8");
          //  se.setContentType(new BasicHeader(HttpRequest, "application/json"));
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

    public String getStringFromPutUrl(String url, JSONObject jsonObject, HashMap<String, String> header_map) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPut httpPut = new HttpPut(url);
            List<String> key_list = new ArrayList();
            for (String key : header_map.keySet()) {
                key_list.add(key);
            }
            for (int hm = 0; hm < header_map.size(); hm++) {
                httpPut.setHeader((String) key_list.get(hm), (String) header_map.get(key_list.get(hm)));
            }
            StringEntity se = new StringEntity(jsonObject.toString(), "UTF-8");
           // se.setContentType(new BasicHeader(HttpRequest.HEADER_CONTENT_TYPE, "application/json"));
            httpPut.setEntity(se);
            is = httpClient.execute(httpPut).getEntity().getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return getStringFromInputStream(is);
    }

    public String getStringFromGetUrl(String url, HashMap<String, String> header_map) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            List<String> key_list = new ArrayList();
            for (String key : header_map.keySet()) {
                key_list.add(key);
            }
            for (int hm = 0; hm < header_map.size(); hm++) {
                httpGet.setHeader((String) key_list.get(hm), (String) header_map.get(key_list.get(hm)));
            }
            is = httpClient.execute(httpGet).getEntity().getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return getStringFromInputStream(is);
    }

    public String getStringFromGetUrl(String url) {
        try {
            is = new DefaultHttpClient().execute(new HttpGet(url)).getEntity().getContent();
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


}
