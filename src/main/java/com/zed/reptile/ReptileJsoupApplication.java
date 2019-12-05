package com.zed.reptile;

import com.zed.reptile.core.pojo.MobilePhone;
import com.zed.reptile.rep.MobilePhoneRespository;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootApplication
@Slf4j
public class ReptileJsoupApplication {

    private static final String UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36";

    @Autowired
    MobilePhoneRespository respository;

    public static void main(String[] args) {
        SpringApplication.run(ReptileJsoupApplication.class, args);
    }

    @Bean
    public void rep() throws InterruptedException, IOException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("https://search.jd.com/search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&wq=%E6%89%8B%E6%9C%BA&page=" + 2 * i);
        }

        int page = 0;
        for (String url: list) {
            Document document = Jsoup.connect(url).userAgent(UA).timeout(10000).get();
            Elements elements = document.select("li.gl-item");
            int urlCount = 0;
            int finalPage = page++;
            AtomicInteger count = new AtomicInteger();
            elements.forEach(d -> {
                count.getAndIncrement();
                MobilePhone mobilePhone = new MobilePhone();
//                System.out.println(d.attr("data-sku"));
                mobilePhone.setSku(Long.parseLong(d.attr("data-sku")));
//                System.out.println(d.select("div.p-name").select("em").text());
                mobilePhone.setName(d.select("div.p-name").select("em").text());
//                System.out.println(d.select("div.p-shop").text());
                String shopName = d.select("div.p-shop").text();
                if (!StringUtil.isBlank(shopName))
                    mobilePhone.setShopName(shopName);
//                System.out.println(d.getElementsByTag("strong").select("i").text());
                String price = d.getElementsByTag("strong").select("i").text();
                if (!StringUtil.isBlank(price)) {
                    try {
                        mobilePhone.setPrice(new BigDecimal(price));
                    } catch (NumberFormatException e) {
                        log.info(price);
                    }
                }
                String url_goods = d.getElementsByTag("a").attr("href");
                mobilePhone.setLink(url_goods.substring(2));
                mobilePhone.setOriginal(d.text());
                mobilePhone.setPage(finalPage);
//                log.info(mobilePhone.toString());
                respository.save(mobilePhone);
            });
            log.info("第{}页写入,{}",finalPage,count);
//            Thread.sleep(1000L);
        }
    }
}
