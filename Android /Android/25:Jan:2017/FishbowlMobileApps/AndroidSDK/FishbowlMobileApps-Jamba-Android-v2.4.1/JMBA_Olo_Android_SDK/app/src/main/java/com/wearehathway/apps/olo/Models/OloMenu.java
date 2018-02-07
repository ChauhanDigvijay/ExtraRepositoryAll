package com.wearehathway.apps.olo.Models;

/**
 * Created by Nauman Afzaal on 24/04/15.
 */
public class OloMenu
{
    private String imagepath;
    private OloCategory[] categories;

    public OloMenu(String imagepath, OloCategory[] categories)
    {
        this.imagepath = imagepath;
        this.categories = categories;
    }

    public OloCategory[] getCategories()
    {
        return categories;
    }

    public String getImagepath()
    {
        return imagepath;
    }
}
