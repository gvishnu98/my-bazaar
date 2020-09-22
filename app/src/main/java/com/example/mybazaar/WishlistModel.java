package com.example.mybazaar;

public class WishlistModel {

    private String productID;
    private String productImage;
    private String productTitle;
    private String productPrice;
    private String cutPrice;
    private Boolean COD;

    public WishlistModel(String productID,String productImage, String productTitle, String productPrice, String cutPrice, Boolean COD) {
        this.productID=productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.cutPrice = cutPrice;
        this.COD = COD;
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

    public String getCutPrice() {
        return cutPrice;
    }

    public void setCutPrice(String cutPrice) {
        this.cutPrice = cutPrice;
    }

    public Boolean isCOD() {
        return COD;
    }

    public void setCOD(Boolean COD) {
        this.COD = COD;
    }
}
