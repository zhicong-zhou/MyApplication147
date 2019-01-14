package com.jzkl.Bean;

public class ShopSize {
    private String shopSizeColor;
    private String shopSizeNum;
    private String shopSizeId;
    private String shopSizePrice;
    private String shopSizeCredit;

    public String getShopSizeCredit() {
        return shopSizeCredit;
    }

    public void setShopSizeCredit(String shopSizeCredit) {
        this.shopSizeCredit = shopSizeCredit;
    }

    public String getShopSizePrice() {
        return shopSizePrice;
    }

    public void setShopSizePrice(String shopSizePrice) {
        this.shopSizePrice = shopSizePrice;
    }

    public String getShopSizeColor() {
        return shopSizeColor;
    }

    public void setShopSizeColor(String shopSizeColor) {
        this.shopSizeColor = shopSizeColor;
    }

    public String getShopSizeNum() {
        return shopSizeNum;
    }

    public void setShopSizeNum(String shopSizeNum) {
        this.shopSizeNum = shopSizeNum;
    }

    public String getShopSizeId() {
        return shopSizeId;
    }

    public void setShopSizeId(String shopSizeId) {
        this.shopSizeId = shopSizeId;
    }

    @Override
    public String toString() {
        return "ShopSize{" +
                "shopSizeColor='" + shopSizeColor + '\'' +
                ", shopSizeNum='" + shopSizeNum + '\'' +
                ", shopSizeId='" + shopSizeId + '\'' +
                ", shopSizePrice='" + shopSizePrice + '\'' +
                ", shopSizeCredit='" + shopSizeCredit + '\'' +
                '}';
    }
}
