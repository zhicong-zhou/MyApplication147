package com.jzkl.Bean;

public class ShopListShop {
    private String shopId;
    private String shopName;
    private String shopImage;
    private int shopImage2;
    private String shopPrice;
    private String shopCredit;
    private String shopType;
    private String shopKuCun;

    public String getShopKuCun() {
        return shopKuCun;
    }

    public void setShopKuCun(String shopKuCun) {
        this.shopKuCun = shopKuCun;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public int getShopImage2() {
        return shopImage2;
    }

    public void setShopImage2(int shopImage2) {
        this.shopImage2 = shopImage2;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getShopCredit() {
        return shopCredit;
    }

    public void setShopCredit(String shopCredit) {
        this.shopCredit = shopCredit;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(String shopPrice) {
        this.shopPrice = shopPrice;
    }

    @Override
    public String toString() {
        return "ShopListShop{" +
                "shopId='" + shopId + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopImage='" + shopImage + '\'' +
                ", shopImage2=" + shopImage2 +
                ", shopPrice='" + shopPrice + '\'' +
                ", shopCredit='" + shopCredit + '\'' +
                ", shopType='" + shopType + '\'' +
                ", shopKuCun='" + shopKuCun + '\'' +
                '}';
    }
}
