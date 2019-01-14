package com.jzkl.Bean;

public class MyJuan {
    private String myJuanName;
    private int myJuanImg;

    public String getMyJuanName() {
        return myJuanName;
    }

    public void setMyJuanName(String myJuanName) {
        this.myJuanName = myJuanName;
    }

    public int getMyJuanImg() {
        return myJuanImg;
    }

    public void setMyJuanImg(int myJuanImg) {
        this.myJuanImg = myJuanImg;
    }

    @Override
    public String toString() {
        return "MyJuan{" +
                "myJuanName='" + myJuanName + '\'' +
                ", myJuanImg=" + myJuanImg +
                '}';
    }
}
