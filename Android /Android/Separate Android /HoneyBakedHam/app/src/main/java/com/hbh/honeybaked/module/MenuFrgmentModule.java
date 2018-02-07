package com.hbh.honeybaked.module;

public class MenuFrgmentModule {
    String pc_des = "";
    int pc_id = 0;
    int pc_img_url = 0;
    String pc_nm = "";

    public MenuFrgmentModule(int pc_id, String pc_nm, String pc_des, int pc_img_url) {
        this.pc_id = pc_id;
        this.pc_nm = pc_nm;
        this.pc_des = pc_des;
        this.pc_img_url = pc_img_url;
    }

    public String getPc_nm() {
        return this.pc_nm;
    }

    public void setPc_nm(String pc_nm) {
        this.pc_nm = pc_nm;
    }

    public String getPc_des() {
        return this.pc_des;
    }

    public void setPc_des(String pc_des) {
        this.pc_des = pc_des;
    }

    public int getPc_img_url() {
        return this.pc_img_url;
    }

    public void setPc_img_url(int pc_img_url) {
        this.pc_img_url = pc_img_url;
    }

    public int getPc_id() {
        return this.pc_id;
    }

    public void setPc_id(int pc_id) {
        this.pc_id = pc_id;
    }
}
