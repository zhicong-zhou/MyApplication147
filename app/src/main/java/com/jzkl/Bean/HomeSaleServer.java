package com.jzkl.Bean;

public class HomeSaleServer {
    private int homeSaleImg;
    private String homeSaleReason;
    private String homeSaleContent;

    public int getHomeSaleImg() {
        return homeSaleImg;
    }

    public void setHomeSaleImg(int homeSaleImg) {
        this.homeSaleImg = homeSaleImg;
    }

    public String getHomeSaleReason() {
        return homeSaleReason;
    }

    public void setHomeSaleReason(String homeSaleReason) {
        this.homeSaleReason = homeSaleReason;
    }

    public String getHomeSaleContent() {
        return homeSaleContent;
    }

    public void setHomeSaleContent(String homeSaleContent) {
        this.homeSaleContent = homeSaleContent;
    }

    @Override
    public String toString() {
        return "HomeSaleServer{" +
                "homeSaleImg=" + homeSaleImg +
                ", homeSaleReason='" + homeSaleReason + '\'' +
                ", homeSaleContent='" + homeSaleContent + '\'' +
                '}';
    }
}
