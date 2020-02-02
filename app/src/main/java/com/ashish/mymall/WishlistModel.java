package com.ashish.mymall;

public class WishlistModel {

    private int productImage,freeCoupans,totalRatings;
    private String productTitle,rating,productPrice,cuttedPrice,paymentMethod;

    public WishlistModel(int productImage, int freeCoupans, int totalRatings, String productTitle, String rating, String productPrice, String cuttedPrice, String paymentMethod) {
        this.productImage = productImage;
        this.freeCoupans = freeCoupans;
        this.totalRatings = totalRatings;
        this.productTitle = productTitle;
        this.rating = rating;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.paymentMethod = paymentMethod;
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

    public int getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(int totalRatings) {
        this.totalRatings = totalRatings;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
