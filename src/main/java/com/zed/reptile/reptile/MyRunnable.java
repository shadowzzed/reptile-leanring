package com.zed.reptile.reptile;

import com.zed.reptile.core.Config;
import com.zed.reptile.core.HttpGetHTML;
import com.zed.reptile.scheduled.ProxyProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
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
public class MyRunnable implements Runnable {
    private Config config;

    @Autowired
    public MyRunnable(Config config) {
        this.config = config;
    }

    private static final String href = "href=\".*?\"";

    List<String> list_url = new ArrayList<>();

    @Bean
    public void read() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new MyRunnable(config));
    }

    public void readIndex() throws InterruptedException {

        Pattern pattern = Pattern.compile(href);
        String content = this.rep(ProxyProvider.getProxy(), config.targetURL, config.targetFormat);
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
        System.out.println(list_url.size());

    }

    /**
     * 动态IP爬取页面
     * @param proxy
     * @return
     */
    private String rep(String proxy, String url, String format) {
        String content = null;
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
        }
        if (content.startsWith("Maximum number of open connections reached"))
            rep(ProxyProvider.getProxy(), url, format);
        return content;
    }

    @Override
    public void run() {
        while (!config.on) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            this.readIndex();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
