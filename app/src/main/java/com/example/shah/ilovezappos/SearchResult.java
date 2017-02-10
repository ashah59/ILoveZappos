package com.example.shah.ilovezappos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shah on 2/6/2017.
 */

public class SearchResult {
    private String originalTerm, currentResultCount, totalResultCount, term, statusCode;
    @SerializedName("results")
    private List<Product> products;

    public String getOriginalTerm() {
        return originalTerm;
    }

    public void setOriginalTerm(String originalTerm) {
        this.originalTerm = originalTerm;
    }

    public String getCurrentResultCount() {
        return currentResultCount;
    }

    public void setCurrentResultCount(String currentResultCount) {
        this.currentResultCount = currentResultCount;
    }

    public String getTotalResultCount() {
        return totalResultCount;
    }

    public void setTotalResultCount(String totalResultCount) {
        this.totalResultCount = totalResultCount;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "originalTerm='" + originalTerm + '\'' +
                ", currentResultCount='" + currentResultCount + '\'' +
                ", totalResultCount='" + totalResultCount + '\'' +
                ", term='" + term + '\'' +
                ", statusCode='" + statusCode + '\'' +
                ", products=" + products.toString() +
                '}';
    }
}
