package com.aacoin.dig.api.service;

import com.aacoin.dig.api.entity.AccountDetail;
import com.aacoin.dig.api.entity.Accounts;
import com.aacoin.dig.api.entity.AccountsData;
import com.aacoin.dig.api.entity.MarketDetail;
import com.aacoin.dig.api.utils.Utils;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
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
 * Created by Administrator on 2018-06-23.
 */
@Service("aacoinService")
public class AacoinService implements IAacoinService {

    //每次交易最小的Eth数量
    private static final Double MINI_AMOUNT_ETH = 0.0025;

    /**
     * <p>Description: 获取用户含有的币种记录</p>
     * <p>param  </p>
     * <p>return List<Accounts></p>
     */
    public List<AccountsData> getAccountCoins(String secretKey, String accessKey) throws IOException {
        List<AccountsData> accountsDataList = null;
        List<AccountsData> accountsDataListResult = new ArrayList<>();

        String accountDetails = null;
        final String url = "https://api.aacoin.com/v1/account/accounts";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("accessKey", accessKey));

        //对参数进行排序
        params.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        List<String> paramStringList = params.stream().map(e -> e.getName() + "=" + e.getValue()).collect(Collectors.toList());
        String paramString = StringUtils.join(paramStringList, "&");
        //进行签名
        String actualSignature = Utils.encodeHmacSHA256(paramString, secretKey);
        params.add(new BasicNameValuePair("sign", actualSignature));

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回
            accountDetails = EntityUtils.toString(response.getEntity());
//            System.out.println(accountDetails);

            Gson gson = new Gson();
            AccountDetail accountDetail = gson.fromJson(accountDetails, AccountDetail.class);
            accountsDataList = accountDetail.getData();

            String currencyCode = null;
            List<Accounts> accountses = null;
            for (AccountsData accountsData : accountsDataList) {
                AccountsData accountsData1 = new AccountsData();

                currencyCode = accountsData.getCurrencyCode();
                accountses = accountsData.getAccounts();
                for (Accounts accounts : accountses) {
                    if (accounts.getType().equals("trade")) {
                        if (!accounts.getBalance().equals("0")) {
                            //获取交易账户中余额大于0 的显示

                            accountsData1.setCurrencyCode(currencyCode);

                            List<Accounts> accountses1 = new ArrayList<>();
                            Accounts accounts1 = new Accounts();
                            accounts1.setBalance(accounts.getBalance());
                            accounts1.setAccountId(accounts.getAccountId());
                            accountses1.add(accounts1);

                            accountsData1.setAccounts(accountses1);
                            accountsDataListResult.add(accountsData1);
                        }
                    }
                }
            }
        } else {
            System.err.println(response.getStatusLine().getStatusCode());
            System.err.println(EntityUtils.toString(response.getEntity()));
        }
        httpPost.releaseConnection();
        return accountsDataListResult;
    }


    /**
     * <p>Description: 下单操作</p>
     * <p>param  </p>
     * <p>return List<Accounts></p>
     */

    public Boolean makeOrder(String secretKey, String accessKey, String symbol, String type, String quantity, String price) throws IOException {

        Boolean result = false;
        final String url = "https://api.aacoin.com/v1/order/place";

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
            String responseResult = EntityUtils.toString(response.getEntity());
            JSONObject accountJson = JSONObject.parseObject(responseResult);
            String status = (String) accountJson.get("status");
            if (!status.equals("1000")) {
                System.out.println("下单异常返回" + responseResult);
                result = false;
            } else {
                System.out.println("下单正常返回" + responseResult);
                result = true;
            }
        } else {
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.err.println(response.getStatusLine().getStatusCode());

            System.out.println("下单异常返回" + EntityUtils.toString(response.getEntity()) + "   Code :::  " + response.getStatusLine().getStatusCode());
            result = false;
        }
        httpPost.releaseConnection();
        return result;
    }


    /**
     * <p>Description: 获取当前委托订单</p>
     * <p>param  </p>
     * <p>return List<Accounts></p>
     */
    public String currentOrders(String secretKey, String accessKey, String symbol, String type, String page, String size) throws IOException {
        String result = null;
        final String url = "https://api.aacoin.com/v1/order/currentOrders";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("accessKey", accessKey));
        params.add(new BasicNameValuePair("symbol", symbol));
        params.add(new BasicNameValuePair("type", type));
        params.add(new BasicNameValuePair("page", page));
        params.add(new BasicNameValuePair("size", size));

        //对参数进行排序
        params.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        List<String> paramStringList = params.stream().map(e -> e.getName() + "=" + e.getValue()).collect(Collectors.toList());
        String paramString = StringUtils.join(paramStringList, "&");
        String actualSignature = Utils.encodeHmacSHA256(paramString, secretKey);
        params.add(new BasicNameValuePair("sign", actualSignature));

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回
            result = EntityUtils.toString(response.getEntity());
//            System.out.println(result);
            JSONObject accountJson = JSONObject.parseObject(result);
            String status = (String) accountJson.get("status");
            if (!status.equals("1000")) {
                System.out.println("异常返回" + result);
            } else {
//                System.out.println("正常返回" + result);
            }
//            result = EntityUtils.toString(response.getEntity());
        } else {
            result = EntityUtils.toString(response.getEntity());
            System.out.println(result);
            System.err.println(response.getStatusLine().getStatusCode());
//            result = EntityUtils.toString(response.getEntity());
            System.out.println("异常返回" + result);
        }
        httpPost.releaseConnection();
        return result;
    }

    /**
     * <p>Description: 取消订单</p>
     * <p>param  </p>
     * <p>return List<Accounts></p>
     */
    public Boolean orderCancel(String secretKey, String accessKey, String orderId) throws IOException {
        Boolean result = false;
        final String url = "https://api.aacoin.com/v1/order/cancel";

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("accessKey", accessKey));
        params.add(new BasicNameValuePair("orderId", orderId));

        //对参数进行排序
        params.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));
        List<String> paramStringList = params.stream().map(e -> e.getName() + "=" + e.getValue()).collect(Collectors.toList());
        String paramString = StringUtils.join(paramStringList, "&");
        String actualSignature = Utils.encodeHmacSHA256(paramString, secretKey);
        params.add(new BasicNameValuePair("sign", actualSignature));

        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 正常返回
//            System.out.println("撤销订单的详细信息   " + EntityUtils.toString(response.getEntity()));
            result = true;

        } else {
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.err.println(response.getStatusLine().getStatusCode());
//            System.out.println("撤销订单  失败  " + EntityUtils.toString(response.getEntity()));
            result = false;
        }
        httpPost.releaseConnection();
        return result;
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
//            System.out.println(symbol + "当前交易价格信息" + marketDetails);
            Gson gson = new Gson();
            MarketDetail detail = gson.fromJson(marketDetails, MarketDetail.class);
            if (detail.getStatus().equals("1000")) {
                currentPrice = detail.getData().getCurrent();
//                System.out.println("current price : " + detail.getData().getCurrent());
            }
        } else {
            System.err.println(response.getStatusLine().getStatusCode());
        }
        httpPost.releaseConnection();
        return currentPrice;
    }
}
