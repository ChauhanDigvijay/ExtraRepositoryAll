package com.identity.arx.objectclass;

public class StudentTimeTableRequestVo {
    private int loginStatus;
    private int rollNumber;

    public int getRollNumber() {
        return this.rollNumber;
    }

    public void setRollNumber(int rollNumber) {
        this.rollNumber = rollNumber;
    }

    public int getLoginStatus() {
        return this.loginStatus;
    }

    public void setLoginStatus(int loginStatus) {
        this.loginStatus = loginStatus;
    }
}
