package com.ashish.mymall;

public class HorizontalProductScrollModel {

    private String productImage,productID;
    private String productTitle,productDesc,productPrice;

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public HorizontalProductScrollModel(String productID,String productImage, String productTitle, String productDesc, String productPrice) {
        this.productID=productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
    }

}
