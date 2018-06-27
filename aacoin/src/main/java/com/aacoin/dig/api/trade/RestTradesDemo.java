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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 */
public class RestTradesDemo {
    public static void main(String[] args) throws IOException {

        final String url = "https://api.aacoin.com/v1/order/trades";

        final String secretKey = "xx";
        final String accessKey = "xx";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("accessKey", accessKey));
        params.add(new BasicNameValuePair("symbol", "xx"));

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
}
