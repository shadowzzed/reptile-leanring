package com.zed.reptile.core.utils;

import com.zed.reptile.core.HttpGetHTML;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 动态IP队列
 * @author Zeluo
 * @date 2019/12/2 9:26
 */
public class HttpProxyUtils {

    private static final String regHost = "[0-9]{1,}\\.[0-9]{1,}\\.[0-9]{1,}\\.[0-9]{1,}";

    private static final String regPort = "[0-9]{4}";

    private static final String reg80 = "80";

    private static final String regLine = "<tr>[\\s\\S]*?</tr>";

    private static final String filter = "\\s*|\t|\r|\n";

    private static final String regTd = "<td>.*?</td>";

    /**
     * 获取代理IP列表，以host,port存储
     * @param url
     * @param format
     * @return
     * @throws IOException
     */
    public static List<String> getProxyList(String url, String format) throws IOException {
        Pattern patternLine = Pattern.compile(regLine);
        Pattern patternFilter = Pattern.compile(filter);
        Pattern patternTd = Pattern.compile(regTd);

        List<String> list = new ArrayList<>();
        String content = HttpGetHTML.getContentDirect(url, format);
        Matcher matcher_line = patternLine.matcher(content);
        while (matcher_line.find()) {
            String line = matcher_line.group();
            //去除空格回车等制表符
            Matcher matcher_filter = patternFilter.matcher(line);
            String lineTrim = matcher_filter.replaceAll("");
            //匹配host和port
            Matcher matcher_td = patternTd.matcher(lineTrim);
            int count = 0;
            StringBuilder hostAndPort = new StringBuilder();
            while (count < 2 && matcher_td.find()) {
                count++;
                String group = matcher_td.group();
                String temp = group.substring(4, group.length() - 5);
                if (count == 1)
                    hostAndPort.append(temp);
                else
                    hostAndPort.append(",").append(temp);
            }
            list.add(hostAndPort.toString());
        }
        return list;
    }
}
