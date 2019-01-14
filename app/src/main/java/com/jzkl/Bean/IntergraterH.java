package com.jzkl.Bean;

public class IntergraterH {
    private String InterHId;
    private String InterHName;
    private String InterHNum;
    private String InterHPrice;
    private String InterHImg;

    public String getInterHId() {
        return InterHId;
    }

    public void setInterHId(String interHId) {
        InterHId = interHId;
    }

    public String getInterHName() {
        return InterHName;
    }

    public void setInterHName(String interHName) {
        InterHName = interHName;
    }

    public String getInterHNum() {
        return InterHNum;
    }

    public void setInterHNum(String interHNum) {
        InterHNum = interHNum;
    }

    public String getInterHPrice() {
        return InterHPrice;
    }

    public void setInterHPrice(String interHPrice) {
        InterHPrice = interHPrice;
    }

    public String getInterHImg() {
        return InterHImg;
    }

    public void setInterHImg(String interHImg) {
        InterHImg = interHImg;
    }

    @Override
    public String toString() {
        return "IntergraterH{" +
                "InterHId='" + InterHId + '\'' +
                ", InterHName='" + InterHName + '\'' +
                ", InterHNum='" + InterHNum + '\'' +
                ", InterHPrice='" + InterHPrice + '\'' +
                ", InterHImg='" + InterHImg + '\'' +
                '}';
    }
}
