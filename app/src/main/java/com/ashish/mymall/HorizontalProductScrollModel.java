package com.ashish.mymall;

public class HorizontalProductScrollModel {

    private int productImage;
    private String productTitle,productDesc,productPrice;

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
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

    public HorizontalProductScrollModel(int productImage, String productTitle, String productDesc, String productPrice) {
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
    }

}
