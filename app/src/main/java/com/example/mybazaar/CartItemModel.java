package com.example.mybazaar;

public class CartItemModel {
    public static final int CART_ITEM=0;
    public static final int TOTAL_AMOUNT=1;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    ///// cart item
    private String productID;
    private String productImage;
    private String productTitle;
    private String productPrice;
    private String cutPrice;
    private Long productQuantity;
    private Long offersApplied;

    public CartItemModel(int type, String productID,String productImage, String productTitle, String productPrice, String cutPrice, Long productQuantity, Long offersApplied) {
        this.type = type;
        this.productID=productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.productPrice = productPrice;
        this.cutPrice = cutPrice;
        this.productQuantity = productQuantity;
        this.offersApplied = offersApplied;
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
    ///// cart item

    ///// cart total

    public CartItemModel(int type) {
        this.type = type;
    }
}
