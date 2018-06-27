package com.aacoin.dig.api.trade;

import com.aacoin.dig.api.entity.AccountDetail;
import com.aacoin.dig.api.entity.Accounts;
import com.aacoin.dig.api.entity.AccountsData;
import com.aacoin.dig.api.utils.Utils;
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


@Service("accounts")
public class RestAccountsDemo {
    public static void main(String[] args) throws IOException {

        String accountDetails = null;
        final String url = "https://api.aacoin.com/v1/account/accounts";

        final String secretKey = "*";
        final String accessKey = "*";

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
            System.out.println(accountDetails);

            Gson gson = new Gson();
//            System.out.println("gson accountDetails ========================" + gson.toJson(accountDetails));
//            JSONObject accountJson = JSONObject.parseObject(accountDetails);
//            System.out.println("accountJson ========================" + accountJson);
//
//            String status = (String) accountJson.get("status");
//            System.out.println("返回状态：：：" + status);
//            System.out.println("将Json转换成实体类 ： Start");

            AccountDetail accountDetail = gson.fromJson(accountDetails, AccountDetail.class);
            System.out.println("返回状态 ========================" + accountDetail.getStatus());
            System.out.println("================================================");
            List<AccountsData> accountsDataList = accountDetail.getData();
            for (AccountsData accountsData : accountsDataList) {
                String currencyCode = accountsData.getCurrencyCode();
                List<Accounts> accountses = accountsData.getAccounts();
                for (Accounts accounts : accountses) {
                    if (accounts.getType().equals("trade")) {
                        if (!accounts.getBalance().equals("0")) {
                            //获取交易账户中余额大于0 的显示
                            System.out.println("币种 ========================" + currencyCode);
                            System.out.println("账户ID ========================" + accounts.getAccountId());
                            System.out.println("余额 ========================" + accounts.getBalance());
                        }
                    }
                }
            }

//            String data = accountJson.toJSONString(accountJson.get("data"));
//            System.out.println("data ：" + data);
            //去除data中的[]
//            String dataJson = data.substring(1, data.length() - 1);

//            System.out.println("dataJson ：" + dataJson);
//            AccountDetail accountDetail = JSON.parseObject(dataJson, AccountDetail.class);

//            JSON data = accountJson.get("data");
//            AccountDetail accountDetail = JSON.parseObject(accountDetails, AccountDetail.class);
//            System.out.println("返回状态 ：：：" + accountDetail.getStatus());
//            List<AccountsData> accountsDataList = JSON.parseArray(accountJson, AccountsData.class);
//            for (AccountsData accountsData : accountsDataList) {
//                System.out.println("账户中的币种：：：" + accountsData.getCurrencyCode());
//                System.out.println("账户中币种详细记录：：：" + JSONObject.toJSONString(accountsData));
//            }

        } else {
            System.err.println(response.getStatusLine().getStatusCode());
            System.err.println(EntityUtils.toString(response.getEntity()));
        }
        httpPost.releaseConnection();
    }

    /**
     * <p>Description: 获取用户含有的币种记录</p>
     * <p>param  </p>
     * <p>return List<Accounts></p>
     */
    public List<AccountsData> getAccountCoins(String secretKey, String accessKey) throws IOException {
        List<AccountsData> accountsDataList = null;
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
            System.out.println(accountDetails);

            Gson gson = new Gson();

            AccountDetail accountDetail = gson.fromJson(accountDetails, AccountDetail.class);
            System.out.println("返回状态 ========================" + accountDetail.getStatus());
            System.out.println("================================================");
            accountsDataList = accountDetail.getData();

            for (AccountsData accountsData : accountsDataList) {
                String currencyCode = accountsData.getCurrencyCode();

                List<Accounts> accountses = accountsData.getAccounts();
                for (Accounts accounts : accountses) {
                    if (accounts.getType().equals("trade")) {
                        if (!accounts.getBalance().equals("0")) {
                            //获取交易账户中余额大于0 的显示
                            System.out.println("币种 ========================" + currencyCode);
                            System.out.println("账户ID ========================" + accounts.getAccountId());
                            System.out.println("余额 ========================" + accounts.getBalance());
//                            accountsRsult.add(accounts);
                        } else {
                            accountsDataList.remove(accountsData);
                        }
                    }
                }

            }
        } else {
            System.err.println(response.getStatusLine().getStatusCode());
            System.err.println(EntityUtils.toString(response.getEntity()));
        }
        httpPost.releaseConnection();
        return accountsDataList;
    }
}
