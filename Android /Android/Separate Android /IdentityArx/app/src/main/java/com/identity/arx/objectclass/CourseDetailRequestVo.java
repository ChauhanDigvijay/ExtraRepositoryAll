package com.identity.arx.objectclass;

public class CourseDetailRequestVo {
    private int id;
    private int loginStatus;
    private String userType;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getLoginStatus() {
        return this.loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }
}
