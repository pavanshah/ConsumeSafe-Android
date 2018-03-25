package com.example.pavanshah.consumesafe.model;

import android.net.Uri;

/**
 * Created by pavan on 3/4/2018.
 */

public class UserDetails {

    private String DisplayName;
    private String Email;
    private  Uri PhotoUrl;
    private String PhoneNumber;
    private String[] Subscriptions;

    public UserDetails() {

    }

    public UserDetails(String displayName, String email, Uri photoUrl, String phoneNumber, String[] subscriptions) {
        DisplayName = displayName;
        Email = email;
        PhotoUrl = photoUrl;
        PhoneNumber = phoneNumber;
        Subscriptions = subscriptions;
    }



    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Uri getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String[] getSubscriptions() {
        return Subscriptions;
    }

    public void setSubscriptions(String[] subscriptions) {
        Subscriptions = subscriptions;
    }

    @Override
    public String toString() {
        return DisplayName + ", "+ Email + ", " + PhoneNumber + ", " + PhotoUrl;
    }
}
