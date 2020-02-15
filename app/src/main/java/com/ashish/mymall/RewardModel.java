package com.ashish.mymall;


import java.util.Date;

public class RewardModel {
    private String type,upperLimit,lowerLimit,discORamt,coupanBody,coupanId;
    private Date timestamp;
    private boolean alreadyUsed;

    public RewardModel(String coupanId,String type, String upperLimit, String lowerLimit, String discORamt, String coupanBody, Date timestamp,boolean alreadyUsed) {
        this.coupanId=coupanId;
        this.type = type;
        this.upperLimit = upperLimit;
        this.lowerLimit = lowerLimit;
        this.discORamt = discORamt;
        this.coupanBody = coupanBody;
        this.timestamp = timestamp;
        this.alreadyUsed=alreadyUsed;
    }

    public String getCoupanId() {
        return coupanId;
    }

    public void setCoupanId(String coupanId) {
        this.coupanId = coupanId;
    }

    public boolean isAlreadyUsed() {
        return alreadyUsed;
    }

    public void setAlreadyUsed(boolean alreadyUsed) {
        this.alreadyUsed = alreadyUsed;
    }

    public String getDiscORamt() {
        return discORamt;
    }

    public void setDiscORamt(String discORamt) {
        this.discORamt = discORamt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(String upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(String lowerLimit) {
        this.lowerLimit = lowerLimit;
    }


    public String getCoupanBody() {
        return coupanBody;
    }

    public void setCoupanBody(String coupanBody) {
        this.coupanBody = coupanBody;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

