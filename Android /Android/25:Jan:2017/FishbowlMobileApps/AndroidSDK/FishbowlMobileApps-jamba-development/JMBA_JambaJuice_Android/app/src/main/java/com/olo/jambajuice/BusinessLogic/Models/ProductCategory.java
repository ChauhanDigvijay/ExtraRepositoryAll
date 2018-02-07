package com.olo.jambajuice.BusinessLogic.Models;

import com.parse.ParseObject;

/**
 * Created by Ihsanulhaq on 5/15/2015.
 */
public class ProductCategory extends ProductFamily {

    private String tagLine;
    private String family;
    private String desc;
    private String categoryId;

    public ProductCategory(ParseObject parseObject) {
        super(parseObject);
        this.tagLine = parseObject.getString("tagLine");
        this.family = parseObject.getParseObject("family").getObjectId();
        this.desc = parseObject.getString("desc");
        this.categoryId= parseObject.getString("categoryId");
    }

    public String getFamily() {
        return family;
    }

    public String getTagLine() {
        return tagLine;
    }

    public int getType(){
        return TYPE_ITEM;
    }

    public String getDesc()
    {
        return desc;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }
}
