package com.ashish.mymall;

import java.util.ArrayList;
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

    private Long freeCoupans, productQuantity, offersApplied, coupansApplied,maxQuantity,stockQuantity;
    private String productID,productImage,productTitle, productPrice,discountedPrice, cuttedPrice,selectedCoupanId;
    private boolean inStock,qtyError,COD;
    private List<String> qtyIDs;

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public CartItemModel(int type, String productID, String productImage, Long freeCoupans, Long productQuantity, Long offersApplied, Long coupansApplied, String productTitle, String productPrice, String cuttedPrice, boolean inStock, Long maxQuantity, Long stockQuantity,boolean COD) {
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
        this.inStock=inStock;
        this.maxQuantity=maxQuantity;
        this.stockQuantity=stockQuantity;
        qtyIDs=new ArrayList<>();
        qtyError=false;
        this.COD=COD;

    }

    public boolean isCOD() {
        return COD;
    }

    public void setCOD(boolean COD) {
        this.COD = COD;
    }

    public String getSelectedCoupanId() {
        return selectedCoupanId;
    }

    public void setSelectedCoupanId(String selectedCoupanId) {
        this.selectedCoupanId = selectedCoupanId;
    }

    public boolean isQtyError() {
        return qtyError;
    }

    public void setQtyError(boolean qtyError) {
        this.qtyError = qtyError;
    }

    public Long getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Long stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<String> getQtyIDs() {
        return qtyIDs;
    }

    public void setQtyIDs(List<String> qtyIDs) {
        this.qtyIDs = qtyIDs;
    }

    public Long getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(Long maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
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
    private int totalItems,totalItemsPrice,totalAmount,savedAmount;
    private String deliveryPrice;

    public CartItemModel(int type) {
        this.type = type;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalItemsPrice() {
        return totalItemsPrice;
    }

    public void setTotalItemsPrice(int totalItemsPrice) {
        this.totalItemsPrice = totalItemsPrice;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(int savedAmount) {
        this.savedAmount = savedAmount;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }
/////////cart total


}
