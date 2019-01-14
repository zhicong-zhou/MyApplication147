package com.jzkl.Bean;

public class BankList {
    String bankId;
    String bankName;
    int bankImg;
    String bankUserImg;
    String bankClass;
    String bankNo;
    String bankCode;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankUserImg() {
        return bankUserImg;
    }

    public void setBankUserImg(String bankUserImg) {
        this.bankUserImg = bankUserImg;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public int getBankImg() {
        return bankImg;
    }

    public void setBankImg(int bankImg) {
        this.bankImg = bankImg;
    }

    public String getBankClass() {
        return bankClass;
    }

    public void setBankClass(String bankClass) {
        this.bankClass = bankClass;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    @Override
    public String toString() {
        return "BankList{" +
                "bankId='" + bankId + '\'' +
                ", bankName='" + bankName + '\'' +
                ", bankImg=" + bankImg +
                ", bankUserImg=" + bankUserImg +
                ", bankClass='" + bankClass + '\'' +
                ", bankNo='" + bankNo + '\'' +
                ", bankCode='" + bankCode + '\'' +
                '}';
    }
}
