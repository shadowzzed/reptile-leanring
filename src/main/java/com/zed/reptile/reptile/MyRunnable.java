package com.zed.reptile.reptile;

import com.zed.reptile.core.Config;
import com.zed.reptile.core.http.HttpGetHTML;
import com.zed.reptile.proxy.ProxyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zeluo
 * @date 2019/12/2 14:12
 */
@Component
@EnableBinding({Processor.class})
public class MyRunnable implements Runnable {
    private Config config;

    @Autowired
    Processor source;

    @Autowired
    public MyRunnable(Config config) {
        this.config = config;
        list_url.add(this.config.targetURL);
    }

    private static final String href = "href=\".*?\"";

    private static List<String> list_url = new ArrayList<>();

    @Bean
    public void read() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new MyRunnable(config));
    }

    private synchronized String getURL() {
        Iterator<String> iterator = list_url.iterator();
        String url = null;
        while (iterator.hasNext()) {
            url = iterator.next();
            iterator.remove();
            return url;
        }
        return null;
    }

    private void readIndex(String proxy) throws InterruptedException {
        String targetURL = this.getURL();
        if (StringUtils.isEmpty(targetURL))
            throw new NullPointerException("URL为空");
        String content = this.rep(proxy, targetURL, config.targetFormat);
        if (StringUtils.isEmpty(content))
            throw new NullPointerException("content为空");
        // 1.读取URL放到队列中
        this.getIndexURL(content);
        // 2.读取页面中需要提取的东西
        this.getIndexContent(content);
    }

    private void getIndexURL(String content) {
        //发现新的url
        Pattern pattern = Pattern.compile(href);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            String url = matcher.group();
            if (url.endsWith("js") ||
                    url.endsWith("css") ||
                    url.endsWith("png"))
                continue;
            if (url.startsWith("http"))
                list_url.add(url);
            else
                list_url.add(config.targetURL + url);
        }
    }

    private void getIndexContent(String content) {
        // 提取信息

    }

    /**
     * 爬取页面封装 如果传入代理则走代理 如果没有则直接爬取
     * @param proxy
     * @return
     */
    private String rep(String proxy, String url, String format) {
        String content = null;
        // 走代理
        if (!StringUtils.isEmpty(proxy)) {
            String[] strings = proxy.split(",");
            try {
                content = HttpGetHTML.getContentProxy(url,
                        strings[0],
                        Integer.parseInt(strings[1]),
                        format);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                // 超时则更换IP
                String proxy_instead = ProxyProvider.getProxy();
                rep(proxy_instead, url, format);
            }
            // 如果爬取的代理连接上限了
            if (!StringUtils.isEmpty(content) && content.startsWith("Maximum number of open connections reached"))
                rep(ProxyProvider.getProxy(), url, format);
        } else {
            // 直接爬取
            try {
                content = HttpGetHTML.getContentDirect(url, format);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.readIndex(ProxyProvider.getProxy());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
