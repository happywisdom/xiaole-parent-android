package com.aibasis.parent.domain;

/**
 * Created by sniper on 2015/12/16.
 */
public class Baby {
    private String babyName;
    private String babyState;

    public Baby(String Name) {
        this.babyName = Name;
    }

    public String getBabyName() {
        return babyName;
    }

    public void setBabyName(String babyName) {
        this.babyName = babyName;
    }

    public String getBabyState() {
        return babyState;
    }

    public void setBabyState(String babyState) {
        this.babyState = babyState;
    }
}
