package com.android.jcenter_projectlibrary.Models;

/**
 * Created by digvijaychauhan on 28/07/17.
 */

public class FBSessionItem
{


    private String message;
    private String accessToken;
    private boolean successFlag;
    private int expires = 0;
    private String loyaltyNo;
    private long memberId;

    public String getLoyaltyNo() {
        return loyaltyNo;
    }

    public void setLoyaltyNo(String loyaltyNo) {
        this.loyaltyNo = loyaltyNo;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public FBSessionItem()
    {
        clearSession();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public boolean isSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.successFlag = successFlag;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }



    public void clearSession()
    {
        message = null;
        accessToken = null;
    }


}
