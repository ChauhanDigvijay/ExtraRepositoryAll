
package com.fishbowl.basicmodule.Analytics;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;


/**
 * Created by digvijay(dj)
 */
@SuppressWarnings("serial")
public class FBEventModel implements Serializable {

    public int id;
    public boolean successFlag;
    public int eventID;
    public String eventTypeName;
    public int eventChannelId;
    public int eventCount;

    public void initFromJson(JSONObject jsonObj){
    	try {
			if(jsonObj.has("id") && !jsonObj.isNull("id") && jsonObj.get("id") instanceof Integer) {
				id = jsonObj.getInt("id");
			}
			if(jsonObj.has("successFlag") && !jsonObj.isNull("successFlag") && jsonObj.get("successFlag") instanceof Boolean) {
				successFlag = jsonObj.getBoolean("successFlag");
			}
			if(jsonObj.has("eventTypeName") && !jsonObj.isNull("eventTypeName") && jsonObj.get("eventTypeName") instanceof String) {
				eventTypeName = jsonObj.getString("eventTypeName");
			}
			if(jsonObj.has("eventID") && !jsonObj.isNull("eventID") && jsonObj.get("eventID") instanceof Integer) {
				eventID = jsonObj.getInt("eventID");
			}
			if(jsonObj.has("eventChannelId") && !jsonObj.isNull("eventChannelId") && jsonObj.get("eventChannelId") instanceof Integer) {
				eventChannelId = jsonObj.getInt("eventChannelId");
			}
			if(jsonObj.has("eventCount") && !jsonObj.isNull("eventCount") && jsonObj.get("eventCount") instanceof Integer) {
				eventCount = jsonObj.getInt("eventCount");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	



    }


}