package com.jzkl.Bean;

public class OrderList {
    private String orderId;
    private String orderName;
    private String orderImg;
    private String orderBh;
    private String orderStatus;
    private String orderPrice;
    private String orderNum;
    private String orderTotail;
    private String orderType;
    private String orderCredit;
    private String orderTotailCredit;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderCredit() {
        return orderCredit;
    }

    public void setOrderCredit(String orderCredit) {
        this.orderCredit = orderCredit;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderImg() {
        return orderImg;
    }

    public void setOrderImg(String orderImg) {
        this.orderImg = orderImg;
    }

    public String getOrderBh() {
        return orderBh;
    }

    public void setOrderBh(String orderBh) {
        this.orderBh = orderBh;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderTotail() {
        return orderTotail;
    }

    public void setOrderTotail(String orderTotail) {
        this.orderTotail = orderTotail;
    }

    public String getOrderTotailCredit() {
        return orderTotailCredit;
    }

    public void setOrderTotailCredit(String orderTotailCredit) {
        this.orderTotailCredit = orderTotailCredit;
    }

    @Override
    public String toString() {
        return "OrderList{" +
                "orderId='" + orderId + '\'' +
                ", orderName='" + orderName + '\'' +
                ", orderImg='" + orderImg + '\'' +
                ", orderBh='" + orderBh + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderPrice='" + orderPrice + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", orderTotail='" + orderTotail + '\'' +
                ", orderType='" + orderType + '\'' +
                ", orderCredit='" + orderCredit + '\'' +
                ", orderTotailCredit='" + orderTotailCredit + '\'' +
                '}';
    }
}
