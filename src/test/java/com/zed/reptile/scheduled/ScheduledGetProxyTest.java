package com.zed.reptile.scheduled;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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