package com.aacoin.dig.api.service;

import com.aacoin.dig.api.entity.AccountsData;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2018-06-23.
 */
public interface IAacoinService {

    List<AccountsData> getAccountCoins(String secretKey, String accessKey) throws IOException;

    Boolean makeOrder(String secretKey, String accessKey, String symbol, String type, String quantity, String price) throws IOException;

    String currentOrders(String secretKey, String accessKey, String symbol, String type, String page, String size) throws IOException;

    Boolean orderCancel(String secretKey, String accessKey, String orderId) throws IOException;

    String getCurrentPrice(String symbol) throws IOException;


}
