package com.zed.reptile.demo;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zeluo
 * @date 2019/11/28 16:29
 */
@Slf4j
public class ReptileDemo {
    private static final String href = "href=\".*?\"";

    private static final String url = "https://item.jd.com/100003395445.html";

    private static List<String> list = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        log.info("tst");
        String content = getContent(url);
        Pattern pattern = Pattern.compile(href);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find()) {
            String subURL = matcher.group().substring(6, matcher.group().length() - 1);
            if (StringUtils.isEmpty(subURL) ||
                    subURL.endsWith("css") ||
                    subURL.endsWith("png")
            )
                continue;
            if (subURL.startsWith("http")) {
                list.add(subURL);
                continue;
            }
            list.add(url + subURL);
        }
        int count = 0;
        System.out.println(list.size());
//        for (String str: list) {
//            System.out.println(count++);
////            System.out.println("********************************************");
//            String con = getContent(str);
//            log.info(con);
//            System.out.println(con);
////            System.out.println("********************************************");
////            Thread.sleep(5000L);
//        }
    }

    private static String getContent(String url) throws IOException {
        //建立一个新的请求客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
        HttpHost proxy = new HttpHost("182.34.17.78",9999);
        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy)
                .setConnectTimeout(10000)
                .setSocketTimeout(30000)
                .build();
//        httpGet.setConfig(config);
        CloseableHttpResponse response = httpClient.execute(httpGet);
//        if (response.getStatusLine().getStatusCode() == 200) {
//            Header[] allHeaders = response.getAllHeaders();
//            List<Header> headers = Arrays.asList(allHeaders);
//            headers.forEach(str -> System.out.println("header:" + str));
//        }
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity, "utf-8");
        System.out.println(content);
        EntityUtils.consume(entity);
        return content;
    }
}
