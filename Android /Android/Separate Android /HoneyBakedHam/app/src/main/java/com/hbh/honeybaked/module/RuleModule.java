package com.hbh.honeybaked.module;

import java.io.Serializable;

public class RuleModule implements Serializable {
    String criteriaName = "";
    int criteriaValue = 0;
    String description = "";
    int id = 0;
    boolean pointRule = false;
    boolean rewardRule = false;

    public RuleModule(String criteriaName, int criteriaValue, String description, boolean pointRule, int id, boolean rewardRule) {
        this.criteriaName = criteriaName;
        this.criteriaValue = criteriaValue;
        this.description = description;
        this.pointRule = pointRule;
        this.id = id;
        this.rewardRule = rewardRule;
    }

    public String getCriteriaName() {
        return this.criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCriteriaValue() {
        return this.criteriaValue;
    }

    public void setCriteriaValue(int criteriaValue) {
        this.criteriaValue = criteriaValue;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPointRule() {
        return this.pointRule;
    }

    public void setPointRule(boolean pointRule) {
        this.pointRule = pointRule;
    }

    public boolean isRewardRule() {
        return this.rewardRule;
    }

    public void setRewardRule(boolean rewardRule) {
        this.rewardRule = rewardRule;
    }
}
