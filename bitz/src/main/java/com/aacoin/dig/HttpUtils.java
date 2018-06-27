package com.aacoin.dig;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.TreeMap;

/**
 * http请求工具
 * Created by zhangjiming on 2017/2/10.
 */
public class HttpUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * put请求
     *
     * @param url
     * @param headerMap
     * @param entity
     * @return
     */
    public static String put(String url, Map<String, String> headerMap, Object entity) throws IOException {
        String responseStr = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(url);
            httpPut.addHeader("Content-type", "application/json; charset=utf-8");
            if (null != headerMap && 0 < headerMap.size()) {
                for (Map.Entry<String, String> map : headerMap.entrySet()) {
                    httpPut.addHeader(map.getKey(), map.getValue());
                }
            }
            StringEntity reqEntity = new StringEntity(JSON.toJSONString(entity), Consts.UTF_8);

            httpPut.setEntity(reqEntity);
            CloseableHttpResponse response = httpClient.execute(httpPut);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    responseStr = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                    // 打印响应长度
                    log.debug("Response content length: " + resEntity.getContentLength());
                    // 打印响应内容
                    log.debug("Response content: " + responseStr);
                }
                EntityUtils.consume(resEntity);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseStr;
    }

    /**
     * post请求
     *
     * @param url
     * @param headerMap
     * @param entity
     * @return
     */
    public static String post(String url, Map<String, String> headerMap, Object entity) throws IOException {
        String responseStr = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-type", "application/json; charset=utf-8");
            if (null != headerMap && 0 < headerMap.size()) {
                for (Map.Entry<String, String> map : headerMap.entrySet()) {
                    httpPost.addHeader(map.getKey(), map.getValue());
                }
            }
            StringEntity reqEntity = new StringEntity(JSON.toJSONString(entity), Consts.UTF_8);

            httpPost.setEntity(reqEntity);
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    responseStr = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                    // 打印响应长度
                    log.debug("Response content length: " + resEntity.getContentLength());
                    // 打印响应内容
                    log.debug("Response content: " + responseStr);
                }
                EntityUtils.consume(resEntity);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseStr;
    }


    /**
     * get请求
     *
     * @param url
     * @param headerMap
     * @param queryParam
     * @return
     */
    public static String postParam(String url, Map<String, String> headerMap, TreeMap<String, String> queryParam) throws IOException {
        String responseStr = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();

            if (null != queryParam && queryParam.size() > 0) {
                StringBuffer sbParam = new StringBuffer();
                for (String key : queryParam.keySet()) {
                    String value = queryParam.get(key);
                    if (null == value) {
                        continue;
                    }
                    sbParam.append(key + "=" + URLEncoder.encode(value, Constant.ENCODING) + "&");
//					sbParam.append(key+"="+value+"&");
                }
                String sbPath = sbParam.toString();
                if (StringUtils.isNotBlank(sbPath)) {
                    String pathParam = sbPath.substring(0, sbPath.length() - 1);

                    url += "?" + pathParam;
                }
            }

            HttpPost httpGet = new HttpPost(url);
            httpGet.addHeader("Content-type", "application/json; charset=utf-8");
            if (null != headerMap && 0 < headerMap.size()) {
                for (Map.Entry<String, String> map : headerMap.entrySet()) {
                    httpGet.addHeader(map.getKey(), map.getValue());
                }
            }

            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    responseStr = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                    // 打印响应长度
                    log.debug("Response content length: " + resEntity.getContentLength());
                    // 打印响应内容
                    log.debug("Response content: " + responseStr);
                }
                EntityUtils.consume(resEntity);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseStr;
    }

    /**
     * get请求
     *
     * @param url
     * @param headerMap
     * @param queryParam
     * @return
     */
    public static String get(String url, Map<String, String> headerMap, TreeMap<String, String> queryParam) throws IOException {
        String responseStr = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();

            if (null != queryParam && queryParam.size() > 0) {
                StringBuffer sbParam = new StringBuffer();
                for (String key : queryParam.keySet()) {
                    String value = queryParam.get(key);
                    if (null == value) {
                        continue;
                    }
                    sbParam.append(key + "=" + URLEncoder.encode(value, Constant.ENCODING) + "&");
//					sbParam.append(key+"="+value+"&");
                }
                String sbPath = sbParam.toString();
                if (StringUtils.isNotBlank(sbPath)) {
                    String pathParam = sbPath.substring(0, sbPath.length() - 1);

                    url += "?" + pathParam;
                }
            }
            log.info("Request url: " + url);
            HttpGet httpGet = new HttpGet(url);
//            httpGet.addHeader("Content-type", "application/json; charset=utf-8");
            if (null != headerMap && 0 < headerMap.size()) {
                for (Map.Entry<String, String> map : headerMap.entrySet()) {
                    httpGet.addHeader(map.getKey(), map.getValue());
                }
            }

            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    responseStr = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                    // 打印响应长度
                    log.debug("Response content length: " + resEntity.getContentLength());
                    // 打印响应内容
                    log.debug("Response content: " + responseStr);
                }
                EntityUtils.consume(resEntity);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseStr;
    }


    /**
     * get请求
     *
     * @param url
     * @param headerMap
     * @param queryParam
     * @return
     */
    public static String getObejct(String url, Map<String, String> headerMap, TreeMap<String, Object> queryParam) throws IOException {
        String responseStr = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();

            if (null != queryParam && queryParam.size() > 0) {
                StringBuffer sbParam = new StringBuffer();
                for (String key : queryParam.keySet()) {
                    String value = queryParam.get(key).toString();
                    if (null == value) {
                        continue;
                    }
                    sbParam.append(key + "=" + URLEncoder.encode(value, Constant.ENCODING) + "&");
//					sbParam.append(key+"="+value+"&");
                }
                String sbPath = sbParam.toString();
                if (StringUtils.isNotBlank(sbPath)) {
                    String pathParam = sbPath.substring(0, sbPath.length() - 1);

                    url += "?" + pathParam;
                }
            }

            HttpGet httpGet = new HttpGet(url);
            httpGet.addHeader("Content-type", "application/json; charset=utf-8");
            if (null != headerMap && 0 < headerMap.size()) {
                for (Map.Entry<String, String> map : headerMap.entrySet()) {
                    httpGet.addHeader(map.getKey(), map.getValue());
                }
            }

            CloseableHttpResponse response = httpClient.execute(httpGet);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    responseStr = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                    // 打印响应长度
                    log.debug("Response content length: " + resEntity.getContentLength());
                    // 打印响应内容
                    log.debug("Response content: " + responseStr);
                }
                EntityUtils.consume(resEntity);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseStr;
    }

    /**
     * delete请求
     *
     * @param url
     * @param headerMap
     * @param queryParam
     * @return
     */
    public static String delete(String url, Map<String, String> headerMap, TreeMap<String, String> queryParam) throws IOException {
        String responseStr = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();

            if (null != queryParam && queryParam.size() > 0) {
                StringBuffer sbParam = new StringBuffer();
                for (String key : queryParam.keySet()) {
                    String value = queryParam.get(key);
                    if (null == value) {
                        continue;
                    }
                    sbParam.append(key + "=" + URLEncoder.encode(value, Constant.ENCODING) + "&");
//					sbParam.append(key+"="+value+"&");
                }
                String sbPath = sbParam.toString();
                if (StringUtils.isNotBlank(sbPath)) {
                    String pathParam = sbPath.substring(0, sbPath.length() - 1);

                    url += "?" + pathParam;
                }
            }

            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.addHeader("Content-type", "application/json; charset=utf-8");
            if (null != headerMap && 0 < headerMap.size()) {
                for (Map.Entry<String, String> map : headerMap.entrySet()) {
                    httpDelete.addHeader(map.getKey(), map.getValue());
                }
            }

            CloseableHttpResponse response = httpClient.execute(httpDelete);
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    responseStr = EntityUtils.toString(resEntity, Charset.forName("UTF-8"));
                    // 打印响应长度
                    log.debug("Response content length: " + resEntity.getContentLength());
                    // 打印响应内容
                    log.debug("Response content: " + responseStr);
                }
                EntityUtils.consume(resEntity);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseStr;
    }
}
