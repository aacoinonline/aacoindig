package com.aacoin.dig.api.entity;

/**
 * Created by Administrator on 2018-06-22.
 */
public class Market {

    private String symbol;

    private String highest;

    private String lowest;

    private String current;

    private String totalTradeAmount;

    private String price24HourBefore;

    private String time;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getHighest() {
        return highest;
    }

    public void setHighest(String highest) {
        this.highest = highest;
    }

    public String getLowest() {
        return lowest;
    }

    public void setLowest(String lowest) {
        this.lowest = lowest;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getTotalTradeAmount() {
        return totalTradeAmount;
    }

    public void setTotalTradeAmount(String totalTradeAmount) {
        this.totalTradeAmount = totalTradeAmount;
    }

    public String getPrice24HourBefore() {
        return price24HourBefore;
    }

    public void setPrice24HourBefore(String price24HourBefore) {
        this.price24HourBefore = price24HourBefore;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
