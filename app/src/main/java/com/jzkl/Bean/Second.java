package com.jzkl.Bean;

public class Second {
    String mSecondName;
    String mSecondImg;
    String mSecondId;
    String mSecondQQ;
    String mSecondDetail;

    public String getmSecondQQ() {
        return mSecondQQ;
    }

    public void setmSecondQQ(String mSecondQQ) {
        this.mSecondQQ = mSecondQQ;
    }

    public String getmSecondDetail() {
        return mSecondDetail;
    }

    public void setmSecondDetail(String mSecondDetail) {
        this.mSecondDetail = mSecondDetail;
    }

    public String getmSecondName() {
        return mSecondName;
    }

    public void setmSecondName(String mSecondName) {
        this.mSecondName = mSecondName;
    }

    public String getmSecondImg() {
        return mSecondImg;
    }

    public void setmSecondImg(String mSecondImg) {
        this.mSecondImg = mSecondImg;
    }

    public String getmSecondId() {
        return mSecondId;
    }

    public void setmSecondId(String mSecondId) {
        this.mSecondId = mSecondId;
    }

    @Override
    public String toString() {
        return "Second{" +
                "mSecondName='" + mSecondName + '\'' +
                ", mSecondImg='" + mSecondImg + '\'' +
                ", mSecondId='" + mSecondId + '\'' +
                ", mSecondQQ='" + mSecondQQ + '\'' +
                ", mSecondDetail='" + mSecondDetail + '\'' +
                '}';
    }
}
