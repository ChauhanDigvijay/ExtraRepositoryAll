package com.wearehathway.apps.olo.Models;

import java.util.ArrayList;

/**
 * Created by Nauman Afzaal on 27/04/15.
 */
public class OloModifier
{
    private int id;
    private String description;
    private boolean mandatory;
    private ArrayList<OloOption> options;
    private String parentchoiceid;
    private String minselects;
    private String maxselects;
    private String minchoicequantity;
    private String maxchoicequantity;
    private String maxaggregatequantity;
    private boolean supportschoicequantities;

    public int getId()
    {
        return id;
    }

    public String getDescription()
    {
        return description;
    }

    public boolean isMandatory()
    {
        return mandatory;
    }

    public ArrayList<OloOption> getOptions()
    {
        return options;
    }

    public String getParentchoiceid()
    {
        return parentchoiceid;
    }

    public String getMinselects()
    {
        return minselects;
    }

    public String getMaxselects()
    {
        return maxselects;
    }

    public String getMaxchoicequantity() {
        return maxchoicequantity;
    }

    public String getMinchoicequantity() {
        return minchoicequantity;
    }

    public String getMaxaggregatequantity() {
        return maxaggregatequantity;
    }

    public boolean isSupportschoicequantities() {
        return supportschoicequantities;
    }
}
