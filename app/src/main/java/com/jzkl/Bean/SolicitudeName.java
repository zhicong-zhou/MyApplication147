package com.jzkl.Bean;

public class SolicitudeName {
    private String sName;
    private String sImg;
    private String sId;
    private String sTel;
    public boolean isChoosed;

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setChoosed(boolean choosed) {
        isChoosed = choosed;
    }

    public String getsTel() {
        return sTel;
    }

    public void setsTel(String sTel) {
        this.sTel = sTel;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsImg() {
        return sImg;
    }

    public void setsImg(String sImg) {
        this.sImg = sImg;
    }

    @Override
    public String toString() {
        return "SolicitudeName{" +
                "sName='" + sName + '\'' +
                ", sImg='" + sImg + '\'' +
                ", sId='" + sId + '\'' +
                ", sTel='" + sTel + '\'' +
                ", isChoosed=" + isChoosed +
                '}';
    }
}
