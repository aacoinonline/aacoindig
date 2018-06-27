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

///market/depth 获取 Market Depth 数据
//    {
//        "bids": 买盘,[price(成交价), amount(成交量)],
//        "asks": 卖盘,[price(成交价), amount(成交量)]
//    }
public class RestDepthDemo {
    public static void main(String[] args) throws IOException {
        final String url = "https://api.aacoin.com/market/depth";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("symbol", "BCH_ETH"));

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回
            System.out.println(EntityUtils.toString(response.getEntity()));
        } else {
            System.err.println(response.getStatusLine().getStatusCode());
        }
        httpPost.releaseConnection();
    }
}
