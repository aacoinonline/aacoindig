package com.aacoin.dig.api.trade;

import com.aacoin.dig.Constant;
import com.aacoin.dig.HttpUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by Administrator on 2018-06-26.
 */
public class BitBalances {

    public static void main(String[] args) throws Exception {
        final String requestUrl = "https://api.bit-z.pro/api_v1/balances";
        String secret_key = "FFwiWjAk45IHLJSRgN5rKk9RZuFoloo6kWWIUGP6WQ8E9UvkdGPgT8xZS2D4VsGG";
        TreeMap<String, String> queryParam = new TreeMap<>();
        queryParam.put("api_key", "10160b75e5a0cc76c23d8eb6bc447638");
        queryParam.put("timestamp", String.valueOf(System.currentTimeMillis()));
        queryParam.put("nonce", getNonce());
        if (null != queryParam && queryParam.size() > 0) {
            StringBuffer sbParam = new StringBuffer();
            for (String key : queryParam.keySet()) {
                String value = queryParam.get(key);
                if (null == value) {
                    continue;
                }
                sbParam.append(key + " = " + URLEncoder.encode(value, Constant.ENCODING) + " & ");
            }

            String sbPath = sbParam.toString().substring(0, sbParam.length() - 2);
            System.out.println(sbParam.toString());
            sbPath = sbPath + secret_key;
            System.out.println(sbPath);
            queryParam.put("sign", encryption(sbPath));
            String responseStr = HttpUtils.postParam(requestUrl, null, queryParam);
            JSONObject jsonObject = JSON.parseObject(responseStr);
            System.out.println("账户资产信息 ：" + jsonObject);
//            return jsonObject;
        }
    }

    public static String getNonce() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += random.nextInt(10);
        }
        System.out.println(result);
        return result;
    }

    /**
     * @param plainText 明文
     * @return 32位密文
     */
    public static String encryption(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            re_md5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }
}
