package com.zed.reptile.core;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zeluo
 * @date 2019/12/3 9:29
 */
@SpringBootTest
class ConfigTest {

    @Autowired
    Config config;

    @Test
    public void test() {
        System.out.println(config.executorsPoolSize);
    }

}