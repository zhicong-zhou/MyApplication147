package com.jzkl.Bean;

public class Wallet {
    private String WalletName;
    private String WalletClass;
    private String WalletStatus;
    private String WalletPirce;
    private String WalletNum;
    private String WalletId;
    private String WalletNo;

    public String getWalletNo() {
        return WalletNo;
    }

    public void setWalletNo(String walletNo) {
        WalletNo = walletNo;
    }

    public String getWalletStatus() {
        return WalletStatus;
    }

    public void setWalletStatus(String walletStatus) {
        WalletStatus = walletStatus;
    }

    public String getWalletId() {
        return WalletId;
    }

    public void setWalletId(String walletId) {
        WalletId = walletId;
    }

    public String getWalletNum() {
        return WalletNum;
    }

    public void setWalletNum(String walletNum) {
        WalletNum = walletNum;
    }

    public String getWalletPirce() {
        return WalletPirce;
    }

    public void setWalletPirce(String walletPirce) {
        WalletPirce = walletPirce;
    }

    public String getWalletName() {
        return WalletName;
    }

    public void setWalletName(String walletName) {
        WalletName = walletName;
    }

    public String getWalletClass() {
        return WalletClass;
    }

    public void setWalletClass(String walletClass) {
        WalletClass = walletClass;
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "WalletName='" + WalletName + '\'' +
                ", WalletClass='" + WalletClass + '\'' +
                ", WalletStatus='" + WalletStatus + '\'' +
                ", WalletPirce='" + WalletPirce + '\'' +
                ", WalletNum='" + WalletNum + '\'' +
                ", WalletId='" + WalletId + '\'' +
                ", WalletNo='" + WalletNo + '\'' +
                '}';
    }
}
