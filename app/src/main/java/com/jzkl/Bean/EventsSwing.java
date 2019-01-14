package com.jzkl.Bean;

/**
 * Created by admin on 2018/5/7.
 */

public class EventsSwing {
    private final String card ;
    private final String cardId ;

    public EventsSwing(String card,String cardId) {
        this.card = card;
        this.cardId = cardId;
    }

    public String getCarId(){
        return cardId;
    }
    public String getCard(){
        return card;
    }
}
