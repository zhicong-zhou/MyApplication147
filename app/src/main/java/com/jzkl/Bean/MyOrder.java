package com.jzkl.Bean;

public class MyOrder {
    private String myOrderName;
    private int myOrderImg;

    public String getMyOrderName() {
        return myOrderName;
    }

    public void setMyOrderName(String myOrderName) {
        this.myOrderName = myOrderName;
    }

    public int getMyOrderImg() {
        return myOrderImg;
    }

    public void setMyOrderImg(int myOrderImg) {
        this.myOrderImg = myOrderImg;
    }

    @Override
    public String toString() {
        return "MyOrder{" +
                "myOrderName='" + myOrderName + '\'' +
                ", myOrderImg=" + myOrderImg +
                '}';
    }
}
