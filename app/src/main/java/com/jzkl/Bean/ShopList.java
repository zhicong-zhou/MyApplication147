package com.jzkl.Bean;

public class ShopList {
    private String shopTitle;
    private int shopId;

    public String getShopTitle() {
        return shopTitle;
    }

    public void setShopTitle(String shopName) {
        this.shopTitle = shopName;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }


    @Override
    public String toString() {
        return "ShopList{" +
                "shopName='" + shopTitle + '\'' +
                ", shopId=" + shopId +
                '}';
    }


}
