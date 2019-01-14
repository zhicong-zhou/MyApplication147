package com.jzkl.Bean;

public class PayWay {
    private String payWayTitle;
    private int payWayImg;

    public String getPayWayTitle() {
        return payWayTitle;
    }

    public void setPayWayTitle(String payWayTitle) {
        this.payWayTitle = payWayTitle;
    }

    public int getPayWayImg() {
        return payWayImg;
    }

    public void setPayWayImg(int payWayImg) {
        this.payWayImg = payWayImg;
    }

    @Override
    public String toString() {
        return "PayWay{" +
                "payWayTitle='" + payWayTitle + '\'' +
                ", payWayImg='" + payWayImg + '\'' +
                '}';
    }
}
