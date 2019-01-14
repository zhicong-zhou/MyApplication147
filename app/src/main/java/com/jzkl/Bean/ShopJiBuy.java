package com.jzkl.Bean;

public class ShopJiBuy {
    private String JIBuyName;
    private String JIBuyContent;
    private String JIBuyPrice;
    private String JIBuyPriced;
    private String JIBuyNum;
    private int JIBuyImg;

    public String getJIBuyName() {
        return JIBuyName;
    }

    public void setJIBuyName(String JIBuyName) {
        this.JIBuyName = JIBuyName;
    }

    public String getJIBuyContent() {
        return JIBuyContent;
    }

    public void setJIBuyContent(String JIBuyContent) {
        this.JIBuyContent = JIBuyContent;
    }

    public String getJIBuyPrice() {
        return JIBuyPrice;
    }

    public void setJIBuyPrice(String JIBuyPrice) {
        this.JIBuyPrice = JIBuyPrice;
    }

    public String getJIBuyPriced() {
        return JIBuyPriced;
    }

    public void setJIBuyPriced(String JIBuyPriced) {
        this.JIBuyPriced = JIBuyPriced;
    }

    public String getJIBuyNum() {
        return JIBuyNum;
    }

    public void setJIBuyNum(String JIBuyNum) {
        this.JIBuyNum = JIBuyNum;
    }

    public int getJIBuyImg() {
        return JIBuyImg;
    }

    public void setJIBuyImg(int JIBuyImg) {
        this.JIBuyImg = JIBuyImg;
    }

    @Override
    public String toString() {
        return "ShopJiBuy{" +
                "JIBuyName='" + JIBuyName + '\'' +
                ", JIBuyContent='" + JIBuyContent + '\'' +
                ", JIBuyPrice='" + JIBuyPrice + '\'' +
                ", JIBuyPriced='" + JIBuyPriced + '\'' +
                ", JIBuyNum='" + JIBuyNum + '\'' +
                ", JIBuyImg=" + JIBuyImg +
                '}';
    }
}
