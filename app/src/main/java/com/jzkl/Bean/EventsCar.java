package com.jzkl.Bean;

/**
 * Created by admin on 2018/5/7.
 */

public class EventsCar {
    private final String carStatus ;

    public EventsCar(String carStatus) {
        this.carStatus = carStatus;
    }
    public String getCarStatus(){
        return carStatus;
    }
}
