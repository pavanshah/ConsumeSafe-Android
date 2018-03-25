package com.example.pavanshah.consumesafe.model;

import android.net.Uri;

/**
 * Created by pavan on 3/4/2018.
 */

public class SubscriptionDetails {

    private String Catagory;
    private Boolean Subscribed;

    public SubscriptionDetails() {

    }

    public SubscriptionDetails(String catagory, Boolean subscribed) {
        Catagory = catagory;
        Subscribed = subscribed;
    }

    public String getCatagory() {
        return Catagory;
    }

    public void setCatagory(String catagory) {
        Catagory = catagory;
    }

    public Boolean getSubscribed() {
        return Subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        Subscribed = subscribed;
    }
}
