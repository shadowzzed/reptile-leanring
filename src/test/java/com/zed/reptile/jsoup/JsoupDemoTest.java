package com.zed.reptile.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import javax.persistence.Table;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zeluo
 * @date 2019/12/5 15:32
 */
class JsoupDemoTest {
    @Test
    public void test() throws IOException {
        Document document = Jsoup.connect("http://www.baidu.com")
                .timeout(10000)
                .proxy("117.88.5.16", 3000)
                .get();
        System.out.println(document.body());
    }

}