package com.jzkl.Bean;

public class Address {
    private String addressName;
    private String addressTel;
    private String addressAear;
    private String addressAddress;
    private String addressId;
    private String addressisDefault;

    public String getAddressAddress() {
        return addressAddress;
    }

    public void setAddressAddress(String addressAddress) {
        this.addressAddress = addressAddress;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getAddressisDefault() {
        return addressisDefault;
    }

    public void setAddressisDefault(String addressisDefault) {
        this.addressisDefault = addressisDefault;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getAddressTel() {
        return addressTel;
    }

    public void setAddressTel(String addressTel) {
        this.addressTel = addressTel;
    }

    public String getAddressAear() {
        return addressAear;
    }

    public void setAddressAear(String addressAear) {
        this.addressAear = addressAear;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressName='" + addressName + '\'' +
                ", addressTel='" + addressTel + '\'' +
                ", addressAear='" + addressAear + '\'' +
                ", addressAddress='" + addressAddress + '\'' +
                ", addressId='" + addressId + '\'' +
                ", addressisDefault='" + addressisDefault + '\'' +
                '}';
    }
}
