package com.aacoin.dig.api.entity;

/**
 * Created by Administrator on 2018-06-22.
 */
public class MarketDetail {

    private String status;

    private Market data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Market getData() {
        return data;
    }

    public void setData(Market data) {
        this.data = data;
    }
}
