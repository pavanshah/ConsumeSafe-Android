package com.example.pavanshah.consumesafe.model;

import android.net.Uri;

/**
 * Created by pavan on 3/4/2018.
 */

public class UserDetails {

    private String DisplayName;
    private String Email;
    private String FirstName;
    private String LastName;
    private String PhotoUrl;
    private String ProviderId;
    private String[] SubscribedCategories;
    //private String[] PurchasedItems;
    private String created_at;
    private String updated_at;

    public UserDetails() {

    }

    public UserDetails(String DisplayName, String Email, String FirstName, String LastName, String PhotoUrl,
                       String ProviderId, String[] SubscribedCategories, String created_at,
                       String updated_at) {
        this.DisplayName = DisplayName;
        this.Email = Email;
        this.FirstName = FirstName;
        this.LastName = LastName;
        this.PhotoUrl = PhotoUrl;
        this.ProviderId = ProviderId;
        this.SubscribedCategories = SubscribedCategories;
        //this.PurchasedItems = PurchasedItems;
        this.created_at = created_at;
        this.updated_at = updated_at;
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

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public String getProviderId() {
        return ProviderId;
    }

    public void setProviderId(String providerId) {
        ProviderId = providerId;
    }

    public String[] getSubscribedCategories() {
        return SubscribedCategories;
    }

    public void setSubscribedCategories(String[] subscribedCategories) {
        SubscribedCategories = subscribedCategories;
    }

    /*public String[] getPurchasedItems() {
        return PurchasedItems;
    }

    public void setPurchasedItems(String[] purchasedItems) {
        PurchasedItems = purchasedItems;
    }*/

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
