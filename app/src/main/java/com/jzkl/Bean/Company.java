package com.jzkl.Bean;

public class Company {
    private String mCompanyName;
    private String mCompanyTel;
    private String mCompanyAear;
    private int mCompanyDecs;
    private int mCompanyImg;

    public String getmCompanyName() {
        return mCompanyName;
    }

    public void setmCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }

    public String getmCompanyTel() {
        return mCompanyTel;
    }

    public void setmCompanyTel(String mCompanyTel) {
        this.mCompanyTel = mCompanyTel;
    }

    public String getmCompanyAear() {
        return mCompanyAear;
    }

    public void setmCompanyAear(String mCompanyAear) {
        this.mCompanyAear = mCompanyAear;
    }

    public int getmCompanyDecs() {
        return mCompanyDecs;
    }

    public void setmCompanyDecs(int mCompanyDefault) {
        this.mCompanyDecs = mCompanyDefault;
    }

    public int getmCompanyImg() {
        return mCompanyImg;
    }

    public void setmCompanyImg(int mCompanyImg) {
        this.mCompanyImg = mCompanyImg;
    }

    @Override
    public String toString() {
        return "Company{" +
                "mCompanyName='" + mCompanyName + '\'' +
                ", mCompanyTel='" + mCompanyTel + '\'' +
                ", mCompanyAear='" + mCompanyAear + '\'' +
                ", mCompanyDecs='" + mCompanyDecs + '\'' +
                ", mCompanyImg=" + mCompanyImg +
                '}';
    }
}
