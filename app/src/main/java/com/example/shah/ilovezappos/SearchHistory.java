package com.example.shah.ilovezappos;

/**
 * Created by shah on 2/8/2017.
 */

public class SearchHistory {
    private String img;
    private String productName;
    private String productId;

    public SearchHistory() {
    }

    public SearchHistory(String productName, String img, String productId) {
        this.productName = productName;
        this.img = img;
        this.productId = productId;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}
