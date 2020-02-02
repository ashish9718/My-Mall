package com.ashish.mymall;

public class RewardModel {
    private String title,expiryDate,coupanBody;

    public RewardModel(String title, String expiryDate, String coupanBody) {
        this.title = title;
        this.expiryDate = expiryDate;
        this.coupanBody = coupanBody;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCoupanBody() {
        return coupanBody;
    }

    public void setCoupanBody(String coupanBody) {
        this.coupanBody = coupanBody;
    }
}
