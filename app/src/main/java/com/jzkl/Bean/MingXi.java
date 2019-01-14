package com.jzkl.Bean;

public class MingXi {
    private String mingxiMonth;

    public String getMingxiMonth() {
        return mingxiMonth;
    }

    public void setMingxiMonth(String mingxiMonth) {
        this.mingxiMonth = mingxiMonth;
    }

    @Override
    public String toString() {
        return "MingXi{" +
                "mingxiMonth='" + mingxiMonth + '\'' +
                '}';
    }
}
