package com.zed.reptile.scheduled;

import com.zed.reptile.core.Config;
import com.zed.reptile.core.utils.HttpProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Zeluo
 * @date 2019/12/2 13:41
 */
public class ProxyProvider {

    @Autowired
    private Config config;

    public static List<String> list = new ArrayList<>(128);

    /**
     * 每秒爬取一次网页刷新list
     */
    @Scheduled(fixedDelay = 1000L)
    public void getProxyList() {
        list.clear();
        try {
            list = HttpProxyUtils.getProxyList(config.proxyURL, config.proxyFormat);
            config.on = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static String getProxy() {
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String proxy = iterator.next();
            iterator.remove();
            return proxy;
        }
        return null;
    }
}
