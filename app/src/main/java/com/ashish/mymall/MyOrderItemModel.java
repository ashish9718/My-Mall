package com.ashish.mymall;

import java.util.Date;


public class MyOrderItemModel {
    private String productId,productTitle,productImage,orderStatus;
    private String address,coupanId,productPrice,cuttedPrice,discountedPrice;
    private Date orderedDate,packedDate,shippedDate,delveredDate,cancelleddate;
    private Long freeCoupans,productQuantity;
    private String fullName,orderId,paymentMethod,pincode,userId;
    private int rating=0;
    private String deliveryPrice;
    private boolean cancellationrequested;


    public MyOrderItemModel(String productId, String orderStatus, String address, String coupanId, String productPrice, String cuttedPrice, String discountedPrice, Date orderedDate, Date packedDate, Date shippedDate, Date delveredDate, Date cancelleddate, Long freeCoupans, Long productQuantity, String fullName, String orderId, String paymentMethod, String pincode, String userId,String productTitle,String productImage,String deliveryPrice,boolean cancellationrequested) {
        this.productId = productId;
        this.orderStatus = orderStatus;
        this.address = address;
        this.coupanId = coupanId;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.discountedPrice = discountedPrice;
        this.orderedDate = orderedDate;
        this.packedDate = packedDate;
        this.shippedDate = shippedDate;
        this.delveredDate = delveredDate;
        this.cancelleddate = cancelleddate;
        this.freeCoupans = freeCoupans;
        this.productQuantity = productQuantity;
        this.fullName = fullName;
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.pincode = pincode;
        this.userId = userId;
        this.productTitle=productTitle;
        this.productImage=productImage;
        this.deliveryPrice=deliveryPrice;
        this.cancellationrequested=cancellationrequested;
    }

    public boolean isCancellationrequested() {
        return cancellationrequested;
    }

    public void setCancellationrequested(boolean cancellationrequested) {
        this.cancellationrequested = cancellationrequested;
    }

    public String getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(String deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoupanId() {
        return coupanId;
    }

    public void setCoupanId(String coupanId) {
        this.coupanId = coupanId;
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

    public String getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(String discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public Date getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(Date orderedDate) {
        this.orderedDate = orderedDate;
    }

    public Date getPackedDate() {
        return packedDate;
    }

    public void setPackedDate(Date packedDate) {
        this.packedDate = packedDate;
    }

    public Date getShippedDate() {
        return shippedDate;
    }

    public void setShippedDate(Date shippedDate) {
        this.shippedDate = shippedDate;
    }

    public Date getDelveredDate() {
        return delveredDate;
    }

    public void setDelveredDate(Date delveredDate) {
        this.delveredDate = delveredDate;
    }

    public Date getCancelleddate() {
        return cancelleddate;
    }

    public void setCancelleddate(Date cancelleddate) {
        this.cancelleddate = cancelleddate;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
