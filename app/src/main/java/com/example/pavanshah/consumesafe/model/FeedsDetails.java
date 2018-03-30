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
    //private String RemedyInformation;
    //private String NoOfUnits;

    public FeedsDetails() {

    }

    public FeedsDetails(String ImageURL, String ProductName, String NewsTitle, String NewsBody, String NewsURL, String RecallDate) {
        ImageURL = ImageURL;
        ProductName = ProductName;
        NewsTitle = NewsTitle;
        NewsBody = NewsBody;
        NewsURL = NewsURL;
        RecallDate = RecallDate;
        //RemedyInformation = RemedyInformation;
        //NoOfUnits = NoOfUnits;
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

    /*public String getRemedyInformation() {
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
    }*/

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
