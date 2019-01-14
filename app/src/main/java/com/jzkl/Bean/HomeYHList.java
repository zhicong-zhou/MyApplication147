package com.jzkl.Bean;

public class HomeYHList {
    private String homeYhImg;
    private String homeYhId;
    private String homeYhTitle;
    private String homeYhDesc;

    public String getHomeYhId() {
        return homeYhId;
    }

    public void setHomeYhId(String homeYhId) {
        this.homeYhId = homeYhId;
    }

    public String getHomeYhTitle() {
        return homeYhTitle;
    }

    public void setHomeYhTitle(String homeYhTitle) {
        this.homeYhTitle = homeYhTitle;
    }

    public String getHomeYhDesc() {
        return homeYhDesc;
    }

    public void setHomeYhDesc(String homeYhDesc) {
        this.homeYhDesc = homeYhDesc;
    }

    public String getHomeYhImg() {
        return homeYhImg;
    }

    public void setHomeYhImg(String homeYhImg) {
        this.homeYhImg = homeYhImg;
    }

    @Override
    public String toString() {
        return "HomeYHList{" +
                "homeYhImg='" + homeYhImg + '\'' +
                ", homeYhId='" + homeYhId + '\'' +
                ", homeYhTitle='" + homeYhTitle + '\'' +
                ", homeYhDesc='" + homeYhDesc + '\'' +
                '}';
    }
}
