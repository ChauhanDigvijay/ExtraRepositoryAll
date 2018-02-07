package com.fishbowl.LoyaltyTabletApp.BusinessLogic.Models;

import org.json.JSONObject;

/**
 * Created by digvijaychauhan on 13/02/17.
 */

public class LoyaltyCardModel {


    public String frontHeaderText;
    public String frontHeaderFontSize;
    public String frontHeaderColor;
    public String frontHeaderFontBold;
    public String frontHeaderFontItalic;
    public String frontHeaderFontUnderline;


    public String frontBodyText1;
    public String frontBodyText1FontSize;
    public String frontBodyText1Color;
    public String frontBodyText1FontBold;
    public String frontBodyText1FontItalic;
    public String frontBodyText1FontUnderline;


    public String frontBodyText2;
    public String frontBodyText2FontSize;
    public String frontBodyText2Color;
    public String frontBodyText2FontBold;
    public String frontBodyText2FontItalic;
    public String frontBodyText2FontUnderline;


    public String frontBodyText3;
    public String frontBodyText3FontSize;
    public String frontBodyText3Color;
    public String frontBodyText3FontBold;
    public String frontBodyText3FontItalic;
    public String frontBodyText3FontUnderline;


    public String frontBodyText4;
    public String frontBodyText4FontSize;
    public String frontBodyText4Color;
    public String frontBodyText4FontBold;
    public String frontBodyText4FontItalic;
    public String frontBodyText4FontUnderline;


    public String frontLabelColor;
    public String frontBackgroundColor;
    public String frontTextColor;
    public String customStripURL;
    public String customLogoURL;

    public String frontCouponTitle;
    public String barcodeType;
    public String frontValue2;
    public String frontValue1;
    public String frontValue4;
    public String frontValue3;


    public String barcodeValue;


    public LoyaltyCardModel(JSONObject obj) {

        try {
            // id = jsonObj.has("id") ? jsonObj.getInt("id") : 0;


            //  frontLabelColor = obj.has("frontLabelColor") ? obj.getInt("frontLabelColor") : 0;
            // companyID = obj.has("tenantId") ? obj.getInt("tenantId") : 0;
            frontLabelColor = obj.has("frontLabelColor") ? obj.getString("frontLabelColor") : null;
            frontBackgroundColor = obj.has("frontBackgroundColor") ? obj.getString("frontBackgroundColor") : null;
            frontTextColor = obj.has("frontTextColor") ? obj.getString("frontTextColor") : null;
            customStripURL = obj.has("customStripURL") ? obj.getString("customStripURL") : null;
            customLogoURL = obj.has("customLogoURL") ? obj.getString("customLogoURL") : null;
            frontHeaderText = obj.has("frontHeaderText") ? obj.getString("frontHeaderText") : null;
            frontCouponTitle = obj.has("frontCouponTitle") ? obj.getString("frontCouponTitle") : null;
            barcodeType = obj.has("barcodeType") ? obj.getString("barcodeType") : null;
            frontValue2 = obj.has("frontValue2") ? obj.getString("frontValue2") : null;
            frontValue1 = obj.has("frontValue1") ? obj.getString("frontValue1") : null;
            frontValue4 = obj.has("frontValue4") ? obj.getString("frontValue4") : null;
            frontValue3 = obj.has("frontValue3") ? obj.getString("frontValue3") : null;
            frontBodyText4 = obj.has("frontBodyText4") ? obj.getString("frontBodyText4") : null;
            frontBodyText3 = obj.has("frontBodyText3") ? obj.getString("frontBodyText3") : null;
            frontBodyText2 = obj.has("frontBodyText2") ? obj.getString("frontBodyText2") : null;
            frontBodyText1 = obj.has("frontBodyText1") ? obj.getString("frontBodyText1") : null;
            barcodeValue = obj.has("barcodeValue") ? obj.getString("barcodeValue") : null;
            frontHeaderFontUnderline = obj.has("frontHeaderFontUnderline") ? obj.getString("frontHeaderFontUnderline") : null;
            frontBodyText2FontUnderline = obj.has("frontBodyText2FontUnderline") ? obj.getString("frontBodyText2FontUnderline") : null;
            frontBodyText3FontUnderline = obj.has("frontBodyText3FontUnderline") ? obj.getString("frontBodyText3FontUnderline") : null;
            frontBodyText4FontUnderline = obj.has("frontBodyText4FontUnderline") ? obj.getString("frontBodyText4FontUnderline") : null;

            frontHeaderFontSize = obj.has("frontHeaderFontSize") ? obj.getString("frontHeaderFontSize") : null;
            frontHeaderColor = obj.has("frontHeaderColor") ? obj.getString("frontHeaderColor") : null;
            frontHeaderFontBold = obj.has("frontHeaderFontBold") ? obj.getString("frontHeaderFontBold") : null;
            frontHeaderFontItalic = obj.has("frontHeaderFontItalic") ? obj.getString("frontHeaderFontItalic") : null;


            frontBodyText1FontSize = obj.has("frontBodyText1FontSize") ? obj.getString("frontBodyText1FontSize") : null;
            frontBodyText1Color = obj.has("frontBodyText1Color") ? obj.getString("frontBodyText1Color") : null;
            frontBodyText1FontBold = obj.has("frontBodyText1FontBold") ? obj.getString("frontBodyText1FontBold") : null;
            frontBodyText1FontItalic = obj.has("frontBodyText1FontItalic") ? obj.getString("frontBodyText1FontItalic") : null;
            frontBodyText1FontUnderline = obj.has("frontBodyText1FontUnderline") ? obj.getString("frontBodyText1FontUnderline") : null;

            frontBodyText2FontSize = obj.has("frontBodyText2FontSize") ? obj.getString("frontBodyText2FontSize") : null;
            frontBodyText2Color = obj.has("frontBodyText2Color") ? obj.getString("frontBodyText2Color") : null;
            frontBodyText2FontBold = obj.has("frontBodyText2FontBold") ? obj.getString("frontBodyText2FontBold") : null;
            frontBodyText2FontItalic = obj.has("frontBodyText2FontItalic") ? obj.getString("frontBodyText2FontItalic") : null;


            frontBodyText3FontSize = obj.has("frontBodyText3FontSize") ? obj.getString("frontBodyText3FontSize") : null;
            frontBodyText3Color = obj.has("frontBodyText3Color") ? obj.getString("frontBodyText3Color") : null;
            frontBodyText3FontBold = obj.has("frontBodyText3FontBold") ? obj.getString("frontBodyText3FontBold") : null;
            frontBodyText3FontItalic = obj.has("frontBodyText3FontItalic") ? obj.getString("frontBodyText3FontItalic") : null;


            frontBodyText4FontSize = obj.has("frontBodyText4FontSize") ? obj.getString("frontBodyText4FontSize") : null;
            frontBodyText4Color = obj.has("frontBodyText4Color") ? obj.getString("frontBodyText4Color") : null;
            frontBodyText4FontBold = obj.has("frontBodyText4FontBold") ? obj.getString("frontBodyText4FontBold") : null;
            frontBodyText4FontItalic = obj.has("frontBodyText4FontItalic") ? obj.getString("frontBodyText4FontItalic") : null;


        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public String getFrontLabelColor() {
        return frontLabelColor;
    }

    public void setFrontLabelColor(String frontLabelColor) {
        this.frontLabelColor = frontLabelColor;
    }

    public String getFrontBackgroundColor() {
        return frontBackgroundColor;
    }

    public void setFrontBackgroundColor(String frontBackgroundColor) {
        this.frontBackgroundColor = frontBackgroundColor;
    }

    public String getFrontTextColor() {
        return frontTextColor;
    }

    public void setFrontTextColor(String frontTextColor) {
        this.frontTextColor = frontTextColor;
    }

    public String getCustomStripURL() {
        return customStripURL;
    }

    public void setCustomStripURL(String customStripURL) {
        this.customStripURL = customStripURL;
    }

    public String getCustomLogoURL() {
        return customLogoURL;
    }

    public void setCustomLogoURL(String customLogoURL) {
        this.customLogoURL = customLogoURL;
    }

    public String getFrontHeaderText() {
        return frontHeaderText;
    }

    public void setFrontHeaderText(String frontHeaderText) {
        this.frontHeaderText = frontHeaderText;
    }

    public String getFrontCouponTitle() {
        return frontCouponTitle;
    }

    public void setFrontCouponTitle(String frontCouponTitle) {
        this.frontCouponTitle = frontCouponTitle;
    }

    public String getBarcodeType() {
        return barcodeType;
    }

    public void setBarcodeType(String barcodeType) {
        this.barcodeType = barcodeType;
    }

    public String getFrontValue2() {
        return frontValue2;
    }

    public void setFrontValue2(String frontValue2) {
        this.frontValue2 = frontValue2;
    }

    public String getFrontValue1() {
        return frontValue1;
    }

    public void setFrontValue1(String frontValue1) {
        this.frontValue1 = frontValue1;
    }

    public String getFrontValue4() {
        return frontValue4;
    }

    public void setFrontValue4(String frontValue4) {
        this.frontValue4 = frontValue4;
    }

    public String getFrontValue3() {
        return frontValue3;
    }

    public void setFrontValue3(String frontValue3) {
        this.frontValue3 = frontValue3;
    }

    public String getFrontBodyText4() {
        return frontBodyText4;
    }

    public void setFrontBodyText4(String frontBodyText4) {
        this.frontBodyText4 = frontBodyText4;
    }

    public String getFrontBodyText3() {
        return frontBodyText3;
    }

    public void setFrontBodyText3(String frontBodyText3) {
        this.frontBodyText3 = frontBodyText3;
    }

    public String getFrontBodyText2() {
        return frontBodyText2;
    }

    public void setFrontBodyText2(String frontBodyText2) {
        this.frontBodyText2 = frontBodyText2;
    }

    public String getFrontBodyText1() {
        return frontBodyText1;
    }

    public void setFrontBodyText1(String frontBodyText1) {
        this.frontBodyText1 = frontBodyText1;
    }

    public String getBarcodeValue() {
        return barcodeValue;
    }

    public void setBarcodeValue(String barcodeValue) {
        this.barcodeValue = barcodeValue;
    }


    public String getFrontHeaderFontSize() {
        return frontHeaderFontSize;
    }

    public void setFrontHeaderFontSize(String frontHeaderFontSize) {
        this.frontHeaderFontSize = frontHeaderFontSize;
    }

    public String getFrontHeaderColor() {
        return frontHeaderColor;
    }

    public void setFrontHeaderColor(String frontHeaderColor) {
        this.frontHeaderColor = frontHeaderColor;
    }

    public String getFrontHeaderFontBold() {
        return frontHeaderFontBold;
    }

    public void setFrontHeaderFontBold(String frontHeaderFontBold) {
        this.frontHeaderFontBold = frontHeaderFontBold;
    }

    public String getFrontHeaderFontItalic() {
        return frontHeaderFontItalic;
    }

    public void setFrontHeaderFontItalic(String frontHeaderFontItalic) {
        this.frontHeaderFontItalic = frontHeaderFontItalic;
    }

    public String getFrontBodyText1FontSize() {
        return frontBodyText1FontSize;
    }

    public void setFrontBodyText1FontSize(String frontBodyText1FontSize) {
        this.frontBodyText1FontSize = frontBodyText1FontSize;
    }

    public String getFrontBodyText1Color() {
        return frontBodyText1Color;
    }

    public void setFrontBodyText1Color(String frontBodyText1Color) {
        this.frontBodyText1Color = frontBodyText1Color;
    }

    public String getFrontBodyText1FontBold() {
        return frontBodyText1FontBold;
    }

    public void setFrontBodyText1FontBold(String frontBodyText1FontBold) {
        this.frontBodyText1FontBold = frontBodyText1FontBold;
    }

    public String getFrontBodyText1FontItalic() {
        return frontBodyText1FontItalic;
    }

    public void setFrontBodyText1FontItalic(String frontBodyText1FontItalic) {
        this.frontBodyText1FontItalic = frontBodyText1FontItalic;
    }

    public String getFrontBodyText2FontSize() {
        return frontBodyText2FontSize;
    }

    public void setFrontBodyText2FontSize(String frontBodyText2FontSize) {
        this.frontBodyText2FontSize = frontBodyText2FontSize;
    }

    public String getFrontBodyText2Color() {
        return frontBodyText2Color;
    }

    public void setFrontBodyText2Color(String frontBodyText2Color) {
        this.frontBodyText2Color = frontBodyText2Color;
    }

    public String getFrontBodyText2FontBold() {
        return frontBodyText2FontBold;
    }

    public void setFrontBodyText2FontBold(String frontBodyText2FontBold) {
        this.frontBodyText2FontBold = frontBodyText2FontBold;
    }

    public String getFrontBodyText2FontItalic() {
        return frontBodyText2FontItalic;
    }

    public void setFrontBodyText2FontItalic(String frontBodyText2FontItalic) {
        this.frontBodyText2FontItalic = frontBodyText2FontItalic;
    }

    public String getFrontBodyText3FontSize() {
        return frontBodyText3FontSize;
    }

    public void setFrontBodyText3FontSize(String frontBodyText3FontSize) {
        this.frontBodyText3FontSize = frontBodyText3FontSize;
    }

    public String getFrontBodyText3Color() {
        return frontBodyText3Color;
    }

    public void setFrontBodyText3Color(String frontBodyText3Color) {
        this.frontBodyText3Color = frontBodyText3Color;
    }

    public String getFrontBodyText3FontBold() {
        return frontBodyText3FontBold;
    }

    public void setFrontBodyText3FontBold(String frontBodyText3FontBold) {
        this.frontBodyText3FontBold = frontBodyText3FontBold;
    }

    public String getFrontBodyText3FontItalic() {
        return frontBodyText3FontItalic;
    }

    public void setFrontBodyText3FontItalic(String frontBodyText3FontItalic) {
        this.frontBodyText3FontItalic = frontBodyText3FontItalic;
    }

    public String getFrontBodyText4FontSize() {
        return frontBodyText4FontSize;
    }

    public void setFrontBodyText4FontSize(String frontBodyText4FontSize) {
        this.frontBodyText4FontSize = frontBodyText4FontSize;
    }

    public String getFrontBodyText4Color() {
        return frontBodyText4Color;
    }

    public void setFrontBodyText4Color(String frontBodyText4Color) {
        this.frontBodyText4Color = frontBodyText4Color;
    }

    public String getFrontBodyText4FontBold() {
        return frontBodyText4FontBold;
    }

    public void setFrontBodyText4FontBold(String frontBodyText4FontBold) {
        this.frontBodyText4FontBold = frontBodyText4FontBold;
    }

    public String getFrontBodyText4FontItalic() {
        return frontBodyText4FontItalic;
    }

    public void setFrontBodyText4FontItalic(String frontBodyText4FontItalic) {
        this.frontBodyText4FontItalic = frontBodyText4FontItalic;
    }


}
