package com.aacoin.dig.api.trade;

import com.aacoin.dig.api.utils.Utils;
import org.apache.commons.lang.StringUtils;
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
import java.util.stream.Collectors;

/**
 * 交易 /order/place 下单
 */
@Service("restPlaceDemo")
public class RestPlaceDemo {
    public static void main(String[] args) throws IOException {

        final String url = "https://api.aacoin.com/v1/order/place";

        final String secretKey = "xx";
        final String accessKey = "xx";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("accessKey", accessKey));
        params.add(new BasicNameValuePair("symbol", "xx"));       //交易市场（例：BCC_ETH）
        params.add(new BasicNameValuePair("type", "xx"));         //交易类型（buy-limit：限价买入；buy-market：市价买入；sell-limit：限价卖出；sell-market：市价卖出）
        params.add(new BasicNameValuePair("quantity", "xx"));     //数量
        params.add(new BasicNameValuePair("price", "xx"));        //价格

        //对参数进行排序
        params.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));


        List<String> paramStringList = params.stream().map(e -> e.getName() + "=" + e.getValue()).collect(Collectors.toList());
        String paramString = StringUtils.join(paramStringList, "&");
        String actualSignature = Utils.encodeHmacSHA256(paramString, secretKey);
        params.add(new BasicNameValuePair("sign", actualSignature));

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回
            System.out.println(EntityUtils.toString(response.getEntity()));
        } else {
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.err.println(response.getStatusLine().getStatusCode());
        }
        httpPost.releaseConnection();
    }



    //下单操作
    public Boolean makeOrder(String secretKey, String accessKey, String symbol, String type, String quantity, String price) throws IOException {

        Boolean result = false;
        final String url = "https://api.aacoin.com/v1/order/place";

//        final String secretKey = "xx";
//        final String accessKey = "xx";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("accessKey", accessKey));
        params.add(new BasicNameValuePair("symbol", symbol));       //交易市场（例：BCC_ETH）
        params.add(new BasicNameValuePair("type", type));         //交易类型（buy-limit：限价买入；buy-market：市价买入；sell-limit：限价卖出；sell-market：市价卖出）
        params.add(new BasicNameValuePair("quantity", quantity));     //数量
        params.add(new BasicNameValuePair("price", price));        //价格

        //对参数进行排序
        params.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        List<String> paramStringList = params.stream().map(e -> e.getName() + "=" + e.getValue()).collect(Collectors.toList());
        String paramString = StringUtils.join(paramStringList, "&");
        String actualSignature = Utils.encodeHmacSHA256(paramString, secretKey);
        params.add(new BasicNameValuePair("sign", actualSignature));

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回
            System.out.println("正常返回" + EntityUtils.toString(response.getEntity()));
            result = true;
        } else {
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.err.println(response.getStatusLine().getStatusCode());

            System.out.println("异常返回" + EntityUtils.toString(response.getEntity()) + "   Code :::  " + response.getStatusLine().getStatusCode());
            result = false;
        }
        httpPost.releaseConnection();
        return result;
    }
}
