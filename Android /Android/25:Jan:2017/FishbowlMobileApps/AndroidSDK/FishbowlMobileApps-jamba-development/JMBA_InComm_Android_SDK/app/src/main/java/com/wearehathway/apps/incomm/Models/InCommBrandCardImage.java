package com.wearehathway.apps.incomm.Models;

import java.util.Date;

/**
 * Created by Nauman Afzaal on 13/08/15.
 */
public class InCommBrandCardImage
{
    private double CustomerCharge;
    private String ImageCode;
    private String ImageFileName;
    private String ImageType;
    private String ImageUrl;
    private boolean IsCustomUpload;
    private Date LastModifiedDate;
    private int SortOrder;
    private String ThumbnailImageUrl;

    public String getImageType()
    {
        return ImageType;
    }

    public double getCustomerCharge()
    {
        return CustomerCharge;
    }

    public String getImageCode()
    {
        return ImageCode;
    }

    public String getImageFileName()
    {
        return ImageFileName;
    }

    public String getImageUrl()
    {
        return ImageUrl;
    }

    public boolean isCustomUpload()
    {
        return IsCustomUpload;
    }

    public Date getLastModifiedDate()
    {
        return LastModifiedDate;
    }

    public int getSortOrder()
    {
        return SortOrder;
    }

    public String getThumbnailImageUrl()
    {
        return ThumbnailImageUrl;
    }
}
