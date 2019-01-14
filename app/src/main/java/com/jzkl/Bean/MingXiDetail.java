package com.jzkl.Bean;

public class MingXiDetail {
    private String detailTitle;
    private String detailTime;
    private String detailPrice;

    public String getDetailTitle() {
        return detailTitle;
    }

    public void setDetailTitle(String detailTitle) {
        this.detailTitle = detailTitle;
    }

    public String getDetailTime() {
        return detailTime;
    }

    public void setDetailTime(String detailTime) {
        this.detailTime = detailTime;
    }

    public String getDetailPrice() {
        return detailPrice;
    }

    public void setDetailPrice(String detailPrice) {
        this.detailPrice = detailPrice;
    }

    @Override
    public String toString() {
        return "MingXiDetail{" +
                "detailTitle='" + detailTitle + '\'' +
                ", detailTime='" + detailTime + '\'' +
                ", detailPrice='" + detailPrice + '\'' +
                '}';
    }
}
