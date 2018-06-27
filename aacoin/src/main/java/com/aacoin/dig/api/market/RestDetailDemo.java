package com.aacoin.dig.api.market;

import com.aacoin.dig.api.entity.MarketDetail;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service("restDetailDemo")
public class RestDetailDemo {

    //    /market/detail 获取市场详情
//    {"status":"1000","data":{"symbol":"BCH_ETH","highest":"1.66775","lowest":"1.63502","current":"1.648300","totalTradeAmount":"5600.87","price24HourBefore":1.66156,"time":"2018-06-22 11:28:13"}}
    public static void main(String[] args) throws IOException {
        //获取详细信息
        final String url = "https://api.aacoin.com/market/detail";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("symbol", "BCH_ETH"));

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回

            String marketDetails = EntityUtils.toString(response.getEntity());
            System.out.println("币" + "BCH_ETH" + "当前交易价格信息" + marketDetails);

            Gson gson = new Gson();
            MarketDetail detail = gson.fromJson(marketDetails, MarketDetail.class);
            if (detail.getStatus().equals("1000")) {
                System.out.println("current price : " + detail.getData().getCurrent());
            }
        } else {
            System.err.println(response.getStatusLine().getStatusCode());
        }
        httpPost.releaseConnection();
    }

    /**
     * <p>Description: 获取币种的当前交易价格</p>
     * <p>param  </p>
     * <p>author zhouhe</p>
     * <p>date 2018/03/08 10:00 </p>
     * <p>return BatchList</p>
     */
    public String getCurrentPrice(String symbol) throws IOException {
        String currentPrice = null;
        //获取详细信息
        final String url = "https://api.aacoin.com/market/detail";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("symbol", symbol));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回
            String marketDetails = EntityUtils.toString(response.getEntity());
            System.out.println(symbol + "当前交易价格信息" + marketDetails);
            Gson gson = new Gson();
            MarketDetail detail = gson.fromJson(marketDetails, MarketDetail.class);
            if (detail.getStatus().equals("1000")) {
                currentPrice = detail.getData().getCurrent();
                System.out.println("current price : " + detail.getData().getCurrent());
            }
        } else {
            System.err.println(response.getStatusLine().getStatusCode());
        }
        httpPost.releaseConnection();
        return currentPrice;
    }
}
