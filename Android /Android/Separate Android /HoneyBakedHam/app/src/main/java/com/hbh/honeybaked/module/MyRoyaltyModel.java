package com.hbh.honeybaked.module;

public class MyRoyaltyModel {
    String royalty_enable = null;
    String royalty_text = null;

    public MyRoyaltyModel(String royalty_text, String royalty_enable) {
        this.royalty_text = royalty_text;
        this.royalty_enable = royalty_enable;
    }

    public String getRoyalty_text() {
        return this.royalty_text;
    }

    public void setRoyalty_text(String royalty_text) {
        this.royalty_text = royalty_text;
    }

    public String getRoyalty_enable() {
        return this.royalty_enable;
    }

    public void setRoyalty_enable(String royalty_enable) {
        this.royalty_enable = royalty_enable;
    }
}
