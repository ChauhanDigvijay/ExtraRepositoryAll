package com.identity.arx.objectclass;

public class LoginRequestObject {
    private String deviceId;
    private String instId;
    private String loginStatus;
    private String notificationId;
    private String pasword;
    private String userId;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPasword() {
        return this.pasword;
    }

    public void setPasword(String pasword) {
        this.pasword = pasword;
    }

    public String getLoginStatus() {
        return this.loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    public String getInstId() {
        return this.instId;
    }

    public void setInstId(String instId) {
        this.instId = instId;
    }

    public String getNotificationId() {
        return this.notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
