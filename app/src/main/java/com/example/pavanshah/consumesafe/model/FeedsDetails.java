package com.example.pavanshah.consumesafe.model;

import org.json.JSONArray;

import java.net.URL;
import java.util.Date;

/**
 * Created by pavan on 3/4/2018.
 */


public class FeedsDetails {

    private String RecallID;
    private ImageURL[] ImageURL;
    private String ProductName;
    private String NewsTitle;
    private String NewsBody;
    private String NewsURL;
    private String RecallDate;
    private String Hazard;
    private String Remedy;
    private String Retailer;
    private String Manufacturer;
    private String ManufacturerCountries;
    private String Injuries;
    private String ConsumerContact;

    public FeedsDetails() {

    }

    public FeedsDetails(String RecallID, ImageURL[] ImageURL, String ProductName, String NewsTitle,
                        String NewsBody, String NewsURL, String RecallDate, String Remedy, String Retailer, String Manufacturer,
                        String ManufacturerCountries, String Injuries, String ConsumerContact) {
        RecallID = RecallID;
        ImageURL = ImageURL;
        ProductName = ProductName;
        NewsTitle = NewsTitle;
        NewsBody = NewsBody;
        NewsURL = NewsURL;
        RecallDate = RecallDate;
        Remedy = Remedy;
        Retailer = Retailer;
        Manufacturer = Manufacturer;
        ManufacturerCountries = ManufacturerCountries;
        Injuries = Injuries;
        ConsumerContact = ConsumerContact;
    }

    public String getRecallID() {
        return RecallID;
    }

    public void setRecallID(String recallID) {
        RecallID = recallID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getNewsTitle() {
        return NewsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        NewsTitle = newsTitle;
    }

    public String getNewsBody() {
        return NewsBody;
    }

    public void setNewsBody(String newsBody) {
        NewsBody = newsBody;
    }

    public String getNewsURL() {
        return NewsURL;
    }

    public void setNewsURL(String newsURL) {
        NewsURL = newsURL;
    }

    public String getRecallDate() {
        return RecallDate;
    }

    public void setRecallDate(String recallDate) {
        RecallDate = recallDate;
    }

    public com.example.pavanshah.consumesafe.model.ImageURL[] getImageURL() {
        return ImageURL;
    }

    public void setImageURL(com.example.pavanshah.consumesafe.model.ImageURL[] imageURL) {
        ImageURL = imageURL;
    }

    public String getHazard() {
        return Hazard;
    }

    public void setHazard(String hazard) {
        Hazard = hazard;
    }

    public String getRemedy() {
        return Remedy;
    }

    public void setRemedy(String remedy) {
        Remedy = remedy;
    }

    public String getRetailer() {
        return Retailer;
    }

    public void setRetailer(String retailer) {
        Retailer = retailer;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getManufacturerCountries() {
        return ManufacturerCountries;
    }

    public void setManufacturerCountries(String manufacturerCountries) {
        ManufacturerCountries = manufacturerCountries;
    }

    public String getInjuries() {
        return Injuries;
    }

    public void setInjuries(String injuries) {
        Injuries = injuries;
    }

    public String getConsumerContact() {
        return ConsumerContact;
    }

    public void setConsumerContact(String consumerContact) {
        ConsumerContact = consumerContact;
    }
}
