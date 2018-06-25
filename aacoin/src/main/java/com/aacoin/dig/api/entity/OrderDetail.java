package com.aacoin.dig.api.entity;

/**
 * Created by Administrator on 2018-06-22.
 */
public class OrderDetail {

    private String status;

    private OrderData data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderData getData() {
        return data;
    }

    public void setData(OrderData data) {
        this.data = data;
    }
}
