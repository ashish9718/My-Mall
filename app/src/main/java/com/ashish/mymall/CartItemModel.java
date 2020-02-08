package com.ashish.mymall;

import java.util.List;

public class CartItemModel {

    public static final int CART_ITEM=0,TOTAL_AMOUNT=1;

    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /////////cart item

    private Long freeCoupans, productQuantity, offersApplied, coupansApplied;
    private String productID,productImage,productTitle, productPrice, cuttedPrice;

    public CartItemModel(int type,String productID,String productImage, Long freeCoupans, Long productQuantity, Long offersApplied, Long coupansApplied, String productTitle, String productPrice, String cuttedPrice) {
        this.type=type;
        this.productID=productID;
        this.productImage = productImage;
        this.freeCoupans = freeCoupans;
        this.productQuantity = productQuantity;
        this.offersApplied = offersApplied;
        this.coupansApplied = coupansApplied;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
    }

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

    public Long getFreeCoupans() {
        return freeCoupans;
    }

    public void setFreeCoupans(Long freeCoupans) {
        this.freeCoupans = freeCoupans;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Long getOffersApplied() {
        return offersApplied;
    }

    public void setOffersApplied(Long offersApplied) {
        this.offersApplied = offersApplied;
    }

    public Long getCoupansApplied() {
        return coupansApplied;
    }

    public void setCoupansApplied(Long coupansApplied) {
        this.coupansApplied = coupansApplied;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getCuttedPrice() {
        return cuttedPrice;
    }

    public void setCuttedPrice(String cuttedPrice) {
        this.cuttedPrice = cuttedPrice;
    }

    /////////cart item

    /////////cart total

    public CartItemModel(int type) {
        this.type = type;
    }


    /////////cart total


}
