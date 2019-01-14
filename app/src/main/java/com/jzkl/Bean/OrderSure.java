package com.jzkl.Bean;

public class OrderSure {

    private String sureShopId;
    private String sureShopSizeId;
    private String sureImg;
    private String sureName;
    private String surePrice;
    private String sureColor;
    private String sureNum;
    private String sureKPrice;
    private String sureKStatus;
    private String sureFeedback;
    private String sureType;

    public String getSureShopSizeId() {
        return sureShopSizeId;
    }

    public void setSureShopSizeId(String sureShopSizeId) {
        this.sureShopSizeId = sureShopSizeId;
    }

    public String getSureShopId() {
        return sureShopId;
    }

    public void setSureShopId(String sureShopId) {
        this.sureShopId = sureShopId;
    }

    public String getSureImg() {
        return sureImg;
    }

    public void setSureImg(String sureImg) {
        this.sureImg = sureImg;
    }

    public String getSureName() {
        return sureName;
    }

    public void setSureName(String sureName) {
        this.sureName = sureName;
    }

    public String getSurePrice() {
        return surePrice;
    }

    public void setSurePrice(String surePrice) {
        this.surePrice = surePrice;
    }

    public String getSureColor() {
        return sureColor;
    }

    public void setSureColor(String sureColor) {
        this.sureColor = sureColor;
    }

    public String getSureNum() {
        return sureNum;
    }

    public void setSureNum(String sureNum) {
        this.sureNum = sureNum;
    }

    public String getSureKPrice() {
        return sureKPrice;
    }

    public void setSureKPrice(String sureKPrice) {
        this.sureKPrice = sureKPrice;
    }

    public String getSureKStatus() {
        return sureKStatus;
    }

    public void setSureKStatus(String sureKStatus) {
        this.sureKStatus = sureKStatus;
    }

    public String getSureFeedback() {
        return sureFeedback;
    }

    public void setSureFeedback(String sureFeedback) {
        this.sureFeedback = sureFeedback;
    }

    public String getSureType() {
        return sureType;
    }

    public void setSureType(String sureType) {
        this.sureType = sureType;
    }

    @Override
    public String toString() {
        return "OrderSure{" +
                "sureShopId='" + sureShopId + '\'' +
                ", sureShopSizeId='" + sureShopSizeId + '\'' +
                ", sureImg='" + sureImg + '\'' +
                ", sureName='" + sureName + '\'' +
                ", surePrice='" + surePrice + '\'' +
                ", sureColor='" + sureColor + '\'' +
                ", sureNum='" + sureNum + '\'' +
                ", sureKPrice='" + sureKPrice + '\'' +
                ", sureKStatus='" + sureKStatus + '\'' +
                ", sureFeedback='" + sureFeedback + '\'' +
                ", sureType='" + sureType + '\'' +
                '}';
    }
}
