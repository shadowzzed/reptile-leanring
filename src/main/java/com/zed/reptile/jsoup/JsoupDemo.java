package com.zed.reptile.jsoup;

import com.zed.reptile.core.pojo.MobilePhone;
import com.zed.reptile.core.pojo.Proxy;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Zeluo
 * @date 2019/12/3 14:37
 */
public class JsoupDemo {

    private static final String reg_host = "[0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+";

    private static final String reg_port = "[0-9]{2,}";

    public static void main(String[] args) throws IOException, InterruptedException {

        List<String> urls = new ArrayList<>();

        List<Proxy> proxyList = new ArrayList<>();
        //TODO 添加代理
        Document doc = Jsoup.connect("https://www.google.com/search?q=%E4%BB%A3%E7%90%86IP&rlz=1C1GCEU_zh-CNUS872US873&oq=%E4%BB%A3%E7%90%86IP&aqs=chrome..69i57j69i59l2j0l2j69i61l3.2161j0j7&sourceid=chrome&ie=UTF-8")
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                .timeout(3000)
                .proxy("10.119.32.45", 8080)
                .get();
        Pattern pattern_host = Pattern.compile(reg_host);
        Pattern pattern_port = Pattern.compile(reg_port);
//        System.out.println(doc.body().getElementById("search").select("div.g"));
        doc.getElementById("search").select("div.g")
                .forEach(d ->
                urls.add(d.getElementsByTag("a").attr("href"))
                );
        urls.forEach(url -> {
                    try {
//                        Document document = Jsoup.connect(url)
//                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
//                                .timeout(10000)
//                                .proxy("10.119.32.45", 8080)
//                                .get();
                        Connection connection = Jsoup.connect(url)
                                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                                .timeout(10000)
                                .proxy("10.119.32.45", 8080);
                        if (connection.execute().statusCode() != 200)
                            return;
                        Document document = connection.get();

                        // 获取host和port
                        document.body().getElementsByTag("table")
                                .select("tr").forEach(row -> {
                                    Proxy proxy = new Proxy();
                                    row.select("td").forEach(td ->
                                            {
                                                String td_text = td.text();
                                                Matcher matcher_host = pattern_host.matcher(td_text);
                                                Matcher matcher_port = pattern_port.matcher(td_text);
                                                if (matcher_host.find())
                                                    proxy.setHost(matcher_host.group());
                                                if (matcher_port.find())
                                                    proxy.setPort(new Integer(matcher_port.group()));
                                            }
                                    );
                                    if (!StringUtil.isBlank(proxy.getHost()) && proxy.getPort() != 0 && proxy.getPort() < 65535) {
                                        Connection conn_test = Jsoup.connect("http://www.baidu.com").timeout(1000).proxy(proxy.getHost(), proxy.getPort());
                                        try {
                                            if (conn_test.execute().statusCode() == 200)
                                                proxyList.add(proxy);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                );
        System.out.println(proxyList.size());
        proxyList.forEach(System.out::println);
//        doc.getElementsByTag("a").forEach(d -> {
//            System.out.println(d.text() + "******" + d.attr("href"));
//        });
//        System.out.println(doc.getElementsByTag("a").attr("href"));
//        System.out.println("`````````````");
//        System.out.println(doc.getElementById("qrcode").select("div.qrcode-text"));
//        System.out.println("*****************");
//        System.out.println(doc.select("div.qrcode-text"));


    }
}
