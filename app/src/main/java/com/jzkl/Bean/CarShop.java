package com.jzkl.Bean;

public class CarShop {
    private String carShopId;
    private String carItemId;
    private String carShopSizeId;
    private String carShopName;
    private double carShopPrice;
    private String carShopCredit;
    private String carShopColor;
    private String carShopSize;
    private int carShopNum;
    private String carShopImg;
    private String carShopType;
    public boolean isChoosed;

    public String getCarItemId() {
        return carItemId;
    }

    public void setCarItemId(String carItemId) {
        this.carItemId = carItemId;
    }

    public String getCarShopSizeId() {
        return carShopSizeId;
    }

    public void setCarShopSizeId(String carShopSizeId) {
        this.carShopSizeId = carShopSizeId;
    }

    public String getCarShopId() {
        return carShopId;
    }

    public void setCarShopId(String carShopId) {
        this.carShopId = carShopId;
    }

    public String getCarShopType() {
        return carShopType;
    }

    public void setCarShopType(String carShopType) {
        this.carShopType = carShopType;
    }

    public String getCarShopCredit() {
        return carShopCredit;
    }

    public void setCarShopCredit(String carShopCredit) {
        this.carShopCredit = carShopCredit;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getCarShopSize() {
        return carShopSize;
    }

    public void setCarShopSize(String carShopSize) {
        this.carShopSize = carShopSize;
    }

    public String getCarShopName() {
        return carShopName;
    }

    public void setCarShopName(String carShopName) {
        this.carShopName = carShopName;
    }

    public double getCarShopPrice() {
        return carShopPrice;
    }

    public void setCarShopPrice(double carShopPrice) {
        this.carShopPrice = carShopPrice;
    }


    public int getCarShopNum() {
        return carShopNum;
    }

    public void setCarShopNum(int carShopNum) {
        this.carShopNum = carShopNum;
    }

    public String getCarShopImg() {
        return carShopImg;
    }

    public void setCarShopImg(String carShopImg) {
        this.carShopImg = carShopImg;
    }

    public String getCarShopColor() {
        return carShopColor;
    }

    public void setCarShopColor(String carShopColor) {
        this.carShopColor = carShopColor;
    }

    @Override
    public String toString() {
        return "CarShop{" +
                "carShopId='" + carShopId + '\'' +
                ", carItemId='" + carItemId + '\'' +
                ", carShopSizeId='" + carShopSizeId + '\'' +
                ", carShopName='" + carShopName + '\'' +
                ", carShopPrice=" + carShopPrice +
                ", carShopCredit='" + carShopCredit + '\'' +
                ", carShopColor='" + carShopColor + '\'' +
                ", carShopSize='" + carShopSize + '\'' +
                ", carShopNum=" + carShopNum +
                ", carShopImg='" + carShopImg + '\'' +
                ", carShopType='" + carShopType + '\'' +
                ", isChoosed=" + isChoosed +
                '}';
    }
}
