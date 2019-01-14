package com.jzkl.Bean;

public class First {
    String mFirstName;
    String mFirstImg;
    String mFirstId;

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmFirstImg() {
        return mFirstImg;
    }

    public void setmFirstImg(String mFirstImg) {
        this.mFirstImg = mFirstImg;
    }

    public String getmFirstId() {
        return mFirstId;
    }

    public void setmFirstId(String mFirstId) {
        this.mFirstId = mFirstId;
    }

    @Override
    public String toString() {
        return "First{" +
                "mFirstName='" + mFirstName + '\'' +
                ", mFirstImg='" + mFirstImg + '\'' +
                ", mFirstId='" + mFirstId + '\'' +
                '}';
    }
}
