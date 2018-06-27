package com.aacoin.dig.api.entity;

import java.util.List;

/**
 * Created by Administrator on 2018-06-22.
 */
public class AccountsData {

    private String currencyCode;

    private List<Accounts> accounts;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public List<Accounts> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Accounts> accounts) {
        this.accounts = accounts;
    }
}
