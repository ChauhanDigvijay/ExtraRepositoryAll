
package com.clp.Analytic;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;



@SuppressWarnings("serial")
public class CLPEventModel implements Serializable {

    public int id;
    public boolean successFlag;
    public int eventID;
    public  String eventTypeName;
    public int eventChannelId;
    public int eventCount;

    public void initFromJson(JSONObject jsonObj){
    	try {
			id=jsonObj.getInt("id");
			successFlag=jsonObj.getBoolean("successFlag");
			eventTypeName=jsonObj.getString("eventTypeName");
			eventID=jsonObj.getInt("eventID");
			eventChannelId=jsonObj.getInt("eventChannelId");
			eventCount=jsonObj.getInt("eventCount");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	



    }


}