package com.android.societysolutionregistration.Utils;

/**
 * Created by schaudhary_ic on 10-Nov-16.
 */

public class MessageListData {
    String message = null;
    String name = null;
    boolean selected = false;
    String date_time = null;

   /* public MessageListData(String message, boolean selected) {
        super();
        this.message = message;
        this.name = name;
        this.selected = selected;
    }
*/
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDate_time(){return date_time;}
public  void setDate_time(String date_time){this.date_time = date_time;}

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}