package com.jzkl.Bean;

public class SolicituList {
    private String mSHead;
    private String mSName;
    private String mSRenz;
    private String mSRenzJb;
    private String mSTel;
    public boolean isChoosed;

    public SolicituList(String mSName, String mSTel) {
        this.mSName = mSName;
        this.mSTel = mSTel;
    }
    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getmSTel() {
        return mSTel;
    }

    public void setmSTel(String mSTel) {
        this.mSTel = mSTel;
    }

    public String getmSHead() {
        return mSHead;
    }

    public void setmSHead(String mSHead) {
        this.mSHead = mSHead;
    }

    public String getmSName() {
        return mSName;
    }

    public void setmSName(String mSName) {
        this.mSName = mSName;
    }

    public String getmSRenz() {
        return mSRenz;
    }

    public void setmSRenz(String mSRenz) {
        this.mSRenz = mSRenz;
    }

    public String getmSRenzJb() {
        return mSRenzJb;
    }

    public void setmSRenzJb(String mSRenzJb) {
        this.mSRenzJb = mSRenzJb;
    }

    @Override
    public String toString() {
        return "SolicituList{" +
                "mSHead='" + mSHead + '\'' +
                ", mSName='" + mSName + '\'' +
                ", mSRenz='" + mSRenz + '\'' +
                ", mSRenzJb='" + mSRenzJb + '\'' +
                ", mSTel='" + mSTel + '\'' +
                ", isChoosed=" + isChoosed +
                '}';
    }
}
