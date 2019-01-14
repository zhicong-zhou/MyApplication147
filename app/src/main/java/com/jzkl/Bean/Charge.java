package com.jzkl.Bean;

public class Charge {
    private String chargeMoney;
    private String chargeId;
    private String chargeTitle;

    public String getChargeMoney() {
        return chargeMoney;
    }

    public void setChargeMoney(String chargeMoney) {
        this.chargeMoney = chargeMoney;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeTitle() {
        return chargeTitle;
    }

    public void setChargeTitle(String chargeTitle) {
        this.chargeTitle = chargeTitle;
    }

    @Override
    public String toString() {
        return "Charge{" +
                "chargeMoney='" + chargeMoney + '\'' +
                ", chargeId='" + chargeId + '\'' +
                ", chargeTitle='" + chargeTitle + '\'' +
                '}';
    }
}
