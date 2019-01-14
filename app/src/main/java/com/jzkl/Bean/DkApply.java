package com.jzkl.Bean;

public class DkApply {
    private String dkId;
    private String dkTitle;
    private String dkMoney;
    private String dkYj;
    private int dkImg;

    public String getDkId() {
        return dkId;
    }

    public void setDkId(String dkId) {
        this.dkId = dkId;
    }

    public String getDkTitle() {
        return dkTitle;
    }

    public void setDkTitle(String dkTitle) {
        this.dkTitle = dkTitle;
    }

    public String getDkMoney() {
        return dkMoney;
    }

    public void setDkMoney(String dkMoney) {
        this.dkMoney = dkMoney;
    }

    public String getDkYj() {
        return dkYj;
    }

    public void setDkYj(String dkYj) {
        this.dkYj = dkYj;
    }

    public int getDkImg() {
        return dkImg;
    }

    public void setDkImg(int dkImg) {
        this.dkImg = dkImg;
    }

    @Override
    public String toString() {
        return "DkApply{" +
                "dkId='" + dkId + '\'' +
                ", dkTitle='" + dkTitle + '\'' +
                ", dkMoney='" + dkMoney + '\'' +
                ", dkYj='" + dkYj + '\'' +
                ", dkImg='" + dkImg + '\'' +
                '}';
    }
}
