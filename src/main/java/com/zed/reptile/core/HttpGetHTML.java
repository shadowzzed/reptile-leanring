package com.zed.reptile.core;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Zeluo
 * @date 2019/12/2 9:26
 */
public class HttpGetHTML {

    private static CloseableHttpClient httpClient = HttpClients.createDefault();

    /**
     * 通过代理来获取内容
     * @param url
     * @param host
     * @param port
     * @return
     * @throws IOException 解析错误
     * @throws TimeoutException 超时错误
     */
    public static String getContentProxy(String url, String host, int port, String format) throws IOException, TimeoutException {
        HttpGet httpGet = getHttpGet(url);
        HttpHost proxy = new HttpHost(host, port);
        //设置代理和连接，读取超时
        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .build();
        httpGet.setConfig(config);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            throw new TimeoutException("连接/读取超时");
        }
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity,format);
        EntityUtils.consume(entity);
        return content;
    }

    /**
     * 直接爬取
     * @param url
     * @param format
     * @return
     * @throws IOException
     */
    public static String getContentDirect(String url, String format) throws IOException {
        HttpGet httpGet = getHttpGet(url);
        RequestConfig config = RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .build();
        httpGet.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity, format);
        EntityUtils.consume(entity);
        return content;
    }

    private static HttpGet getHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
        return httpGet;
    }
}
