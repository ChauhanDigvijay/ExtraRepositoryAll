package com.wearehathway.apps.olo.Models;

/**
 * Created by Nauman Afzaal on 23/04/15.
 */
public class OloRestaurantCustomField
{
    private int id;
    private String label;
    private boolean required;
    private String validationregex;
    private String qualificationcriteria;

    public int getId()
    {
        return id;
    }

    public String getLabel()
    {
        return label;
    }

    public boolean isRequired()
    {
        return required;
    }

    public String getValidationregex()
    {
        return validationregex;
    }

    public String getQualificationcriteria()
    {
        return qualificationcriteria;
    }
}
