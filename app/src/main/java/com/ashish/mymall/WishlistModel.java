package com.ashish.mymall;

import android.widget.ImageView;

public class WishlistModel {

    private long freeCoupans,totalRatings;
    private String productImage,productId;
    private String productTitle,rating,productPrice,cuttedPrice;
    private Boolean COD;

    public WishlistModel(String productId,String productImage, String productTitle,long freeCoupans, String rating, long totalRatings, String productPrice, String cuttedPrice, Boolean COD) {
        this.productId=productId;
        this.freeCoupans = freeCoupans;
        this.totalRatings = totalRatings;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.rating = rating;
        this.productPrice = productPrice;
        this.cuttedPrice = cuttedPrice;
        this.COD = COD;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public long getFreeCoupans() {
        return freeCoupans;
    }

    public void setFreeCoupans(long freeCoupans) {
        this.freeCoupans = freeCoupans;
    }

    public long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(long totalRatings) {
        this.totalRatings = totalRatings;
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

    public Boolean getCOD() {
        return COD;
    }

    public void setCOD(Boolean COD) {
        this.COD = COD;
    }
}
