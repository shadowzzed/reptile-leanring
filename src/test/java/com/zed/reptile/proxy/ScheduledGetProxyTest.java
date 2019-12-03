package com.zed.reptile.proxy;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zeluo
 * @date 2019/12/2 13:46
 */
class ScheduledGetProxyTest {
    @Test
    public void testList() {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.forEach(System.out::println);
        list.clear();
    }

}