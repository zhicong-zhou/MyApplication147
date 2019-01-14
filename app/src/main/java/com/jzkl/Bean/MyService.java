package com.jzkl.Bean;

public class MyService {
    private String myServiceName;
    private int myServiceImg;

    public String getMyServiceName() {
        return myServiceName;
    }

    public void setMyServiceName(String myServiceName) {
        this.myServiceName = myServiceName;
    }

    public int getMyServiceImg() {
        return myServiceImg;
    }

    public void setMyServiceImg(int myServiceImg) {
        this.myServiceImg = myServiceImg;
    }

    @Override
    public String toString() {
        return "MyService{" +
                "myServiceName='" + myServiceName + '\'' +
                ", myServiceImg=" + myServiceImg +
                '}';
    }
}
