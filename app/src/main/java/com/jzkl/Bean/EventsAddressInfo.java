package com.jzkl.Bean;

/**
 * Created by admin on 2018/5/7.
 */

public class EventsAddressInfo {
    private final String addressId;
    private final String addressName;
    private final String addressTel;
    private final String addressAear;
    private final String addressDetail;

    public EventsAddressInfo(String addressId, String addressName, String addressTel, String addressAear, String addressDetail) {
        this.addressId = addressId;
        this.addressName = addressName;
        this.addressTel = addressTel;
        this.addressAear = addressAear;
        this.addressDetail = addressDetail;
    }

    public String getaddressId() {
        return addressId;
    }

    public String getaddressName() {
        return addressName;
    }

    public String getaddressTel() {
        return addressTel;
    }

    public String getAddressAear() {
        return addressAear;
    }

    public String getaddressDetail() {
        return addressDetail;
    }

}
