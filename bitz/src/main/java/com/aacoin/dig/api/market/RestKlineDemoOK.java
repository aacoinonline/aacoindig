package com.aacoin.dig.api.market;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//获取K线的记录
public class RestKlineDemoOK {
    public static void main(String[] args) throws IOException {
        final String url = "https://api.aacoin.com/market/kline";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("symbol", "BCH_ETH"));
        params.add(new BasicNameValuePair("period", "5m"));
        params.add(new BasicNameValuePair("size", "50"));

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);

        System.out.println("============================================");
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回
            System.out.println(EntityUtils.toString(response.getEntity()));
        } else {
            System.err.println(response.getStatusLine().getStatusCode());
        }
        httpPost.releaseConnection();
    }
}
