package com.zed.reptile.core.utils;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zeluo
 * @date 2019/12/2 10:48
 */
class HttpProxyUtilsTest {

    @Test
    public void testString() {
        String str = "<td>\n" +
                "\t\t\t183.164.238.6\t\t</td>\n" +
                "<td>\n" +
                "\t\t\t9999\t\t</td>\n" +
                "<td>\n" +
                "\t\t    安徽省淮北市\t\t</td>\n" +
                "<td>\n" +
                "\t\t\t电信\t\t</td>\n" +
                "<td>\n" +
                "\t\t\t2019/12/02 10:15:02\t\t</td>\n";
        System.out.println(str.replace('\t', '1'));

        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        System.out.println(dest);

    }

    @Test
    public void test1() throws IOException {
        List<String> list = HttpProxyUtils.getProxyList("http://www.89ip.cn/index_1.html", "utf-8");
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext())
            System.out.println(iterator.next());
    }

    @Test
    public void test2() {
        String str = "<td>117.57.91.208</td>";
        String substring = str.substring(4, str.length() - 5);
        System.out.println(substring);
    }
}