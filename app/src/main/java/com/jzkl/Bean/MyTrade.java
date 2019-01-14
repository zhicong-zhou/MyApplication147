package com.jzkl.Bean;

public class MyTrade {
    private String tradeCard;
    private String tradeMoney;
    private String tradeHua;
    private String tradeTime;

    public String getTradeCard() {
        return tradeCard;
    }

    public void setTradeCard(String tradeCard) {
        this.tradeCard = tradeCard;
    }

    public String getTradeMoney() {
        return tradeMoney;
    }

    public void setTradeMoney(String tradeMoney) {
        this.tradeMoney = tradeMoney;
    }

    public String getTradeHua() {
        return tradeHua;
    }

    public void setTradeHua(String tradeHua) {
        this.tradeHua = tradeHua;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    @Override
    public String toString() {
        return "MyTrade{" +
                "tradeCard='" + tradeCard + '\'' +
                ", tradeMoney='" + tradeMoney + '\'' +
                ", tradeHua='" + tradeHua + '\'' +
                ", tradeTime='" + tradeTime + '\'' +
                '}';
    }
}
