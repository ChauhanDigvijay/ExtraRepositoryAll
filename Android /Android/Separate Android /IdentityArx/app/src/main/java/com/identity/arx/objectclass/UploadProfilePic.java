package com.identity.arx.objectclass;

public class UploadProfilePic {
    private Integer id;
    private byte[] image;
    private Integer instId;
    private Integer loginStatus;
    private String msg;
    private String password;
    private Integer role;
    private String userName;

    public Integer getInstId() {
        return this.instId;
    }

    public void setInstId(Integer instId) {
        this.instId = instId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return this.role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getLoginStatus() {
        return this.loginStatus;
    }

    public void setLoginStatus(Integer loginStatus) {
        this.loginStatus = loginStatus;
    }

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
