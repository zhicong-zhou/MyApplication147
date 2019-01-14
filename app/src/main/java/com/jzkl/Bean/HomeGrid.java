package com.jzkl.Bean;

public class HomeGrid {
    private String myOrderName;
    private String myOrderImg;

    public String getMyOrderName() {
        return myOrderName;
    }

    public void setMyOrderName(String myOrderName) {
        this.myOrderName = myOrderName;
    }

    public String getMyOrderImg() {
        return myOrderImg;
    }

    public void setMyOrderImg(String myOrderImg) {
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
