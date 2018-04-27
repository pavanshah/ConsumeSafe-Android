package com.example.pavanshah.consumesafe.model;

/**
 * Created by pavan on 3/4/2018.
 */


public class ReceiptDetails implements java.io.Serializable {

    String upcApi;
    String productName;
    String image;
    String productCategory;

    public ReceiptDetails() {

    }

    public ReceiptDetails(String upcApi, String productName, String image, String productCategory) {

        this.upcApi = upcApi;
        this.productName = productName;
        this.image = image;
        this.productCategory = productCategory;
    }

    public String getUpcApi() {
        return upcApi;
    }

    public void setUpcApi(String upcApi) {
        this.upcApi = upcApi;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }
}
