package com.BasicApp.BusinessLogic.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by schaudhary_ic on 13-Dec-16.
 */

public class Bonus implements Serializable {
    String criteriaName;
    int criteriaValue;
    String description;
    Boolean pointRule;
    int id;
    Boolean rewardRule;

    public  Bonus(JSONObject jsonObj){
        try{
            criteriaName = jsonObj.has("criteriaName")?jsonObj.getString("criteriaName"):null;
            criteriaValue = jsonObj.has("criteriaValue") ? jsonObj.getInt("criteriaValue"):0;
            description = jsonObj.has("description") ? jsonObj.getString("description"):null;
            pointRule = jsonObj.has("pointRule")? jsonObj.getBoolean("pointRule"):false;
            id = jsonObj.has("id")? jsonObj.getInt("id"):0;
            rewardRule = jsonObj.has("rewardRule")?jsonObj.getBoolean("rewardRule"):false;

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void setCriteriaName(String criteriaName){ this.criteriaName=criteriaName;}
    public String getCriteriaName(){return criteriaName; }

    public void setCriteriaValue(int criteriaValue){this.criteriaValue = criteriaValue;}
    public int getCriteriaValue(){return criteriaValue;}

    public void setDescription(String description){ this.description=description;}
    public String getDescription(){return description; }

    public void setPointRule(Boolean pointRule){ this.pointRule = pointRule;}
    public Boolean getPointRule(){return pointRule;}

    public void setId(int id){this.id = id;}
    public int getId(){return id;}

    public void setRewardRule(Boolean rewardRule){ this.rewardRule = rewardRule;}
    public Boolean getRewardRule(){return rewardRule;}


}
