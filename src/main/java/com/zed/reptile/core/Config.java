package com.zed.reptile.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author Zeluo
 * @date 2019/12/2 13:42
 */
@Configuration
public class Config {
    @Value("${proxy.url}")
    public String proxyURL;

    @Value("${proxy.format}")
    public String proxyFormat;

    @Value("${target.url}")
    public String targetURL;

    @Value("${target.format}")
    public String targetFormat;

    public Boolean on;
}
