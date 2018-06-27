package com.aacoin.dig.api.entity;

import java.util.List;

/**
 * Created by Administrator on 2018-06-22.
 */
public class AccountDetail {

    private String status;

    private List<AccountsData> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<AccountsData> getData() {
        return data;
    }

    public void setData(List<AccountsData> data) {
        this.data = data;
    }
}
