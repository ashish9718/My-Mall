package com.ashish.mymall;

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

    private int productImage, freeCoupans, productQuantity, offersApplied, coupansApplied;
    private String productTitle, productPrice, cuttedPrice;

    public CartItemModel(int type,int productImage, int freeCoupans, int productQuantity, int offersApplied, int coupansApplied, String productTitle, String productPrice, String cuttedPrice) {
        this.type=type;
        this.productImage = productImage;
        this.freeCoupans = freeCoupans;
        this.productQuantity = productQuantity;
        this.offersApplied = offersApplied;
        this.coupansApplied = coupansApplied;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }

    public int getFreeCoupans() {
        return freeCoupans;
    }

    public void setFreeCoupans(int freeCoupans) {
        this.freeCoupans = freeCoupans;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getOffersApplied() {
        return offersApplied;
    }

    public void setOffersApplied(int offersApplied) {
        this.offersApplied = offersApplied;
    }

    public int getCoupansApplied() {
        return coupansApplied;
    }

    public void setCoupansApplied(int coupansApplied) {
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
    private String totalItems;
    private String totalItemPrice, deliveryPrice, savedAmount,totalAmount;

    public CartItemModel(int type, String totalItems, String totalItemPrice, String deliveryPrice, String savedAmount,String totalAmount) {
        this.type = type;
        this.totalItems = totalItems;
        this.totalItemPrice = totalItemPrice;
        this.deliveryPrice = deliveryPrice;
        this.savedAmount = savedAmount;
        this.totalAmount=totalAmount;
    }

    public String getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(String totalItems) {
        this.totalItems = totalItems;
    }

    public String getTotalItemPrice() {
        return totalItemPrice;
    }

    public void setTotalItemPrice(String totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(String savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }
    /////////cart total


}
