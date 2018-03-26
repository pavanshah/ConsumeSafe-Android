package com.example.pavanshah.consumesafe.model;

import java.net.URL;
import java.util.Date;

/**
 * Created by pavan on 3/4/2018.
 */

public class FeedsDetails {

    private String ImageURL;
    private String ProductName;
    private String NewsTitle;
    private String NewsBody;
    private String NewsURL;
    private String RecallDate;
    private String RemedyInformation;
    private String NoOfUnits;
    private String MobileNumber;
    private String EmailAddress;
    private String Website;

    public FeedsDetails() {

    }

    public FeedsDetails(String imageURL, String productName, String newsTitle, String newsBody, String newsURL, String recallDate, String remedyInformation, String noOfUnits, String mobileNumber, String emailAddress, String website) {
        ImageURL = imageURL;
        ProductName = productName;
        NewsTitle = newsTitle;
        NewsBody = newsBody;
        NewsURL = newsURL;
        RecallDate = recallDate;
        RemedyInformation = remedyInformation;
        NoOfUnits = noOfUnits;
        MobileNumber = mobileNumber;
        EmailAddress = emailAddress;
        Website = website;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
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

    public String getRemedyInformation() {
        return RemedyInformation;
    }

    public void setRemedyInformation(String remedyInformation) {
        RemedyInformation = remedyInformation;
    }

    public String getNoOfUnits() {
        return NoOfUnits;
    }

    public void setNoOfUnits(String noOfUnits) {
        NoOfUnits = noOfUnits;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getWebsite() {
        return Website;
    }

    public void setWebsite(String website) {
        Website = website;
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
}
