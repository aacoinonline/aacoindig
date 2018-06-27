package com.aacoin.dig.api.utils;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Utils {
    /**
     * 使用HmacSHA256消息摘要算法计算消息摘要
     *
     * @param paramString 做消息摘要的数据
     * @param secretKey   密钥
     * @return 消息摘要（长度为16的字节数组）
     */
    public static String encodeHmacSHA256(String paramString, String secretKey) {
        Key secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secret_key);
            byte[] signData = mac.doFinal(paramString.getBytes("UTF-8"));
            //进行加密 并转换成16进制
            return Hex.encodeHexString(signData);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
