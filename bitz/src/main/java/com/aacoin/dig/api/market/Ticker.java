package com.aacoin.dig.api.market;

import com.aacoin.dig.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.util.TreeMap;


//获取最新的50条交易记录
public class Ticker {
    public static void main(String[] args) throws IOException {
        final String requestUrl = "https://api.bit-z.pro/api_v1/ticker";
        TreeMap<String, String> queryParam = new TreeMap<String, String>();
//        queryParam.put("language", "en");
        queryParam.put("coin", "mzc_btc");
        try {
            String responseStr = HttpUtils.get(requestUrl, null, queryParam);
            JSONObject jsonObject = JSON.parseObject(responseStr);
            System.out.println("ticker 当前交易记录情况 ： " + jsonObject);
//            return jsonObject;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
