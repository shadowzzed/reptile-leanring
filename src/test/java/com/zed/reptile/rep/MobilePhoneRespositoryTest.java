package com.zed.reptile.rep;

import com.zed.reptile.core.pojo.MobilePhone;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Zeluo
 * @date 2019/12/3 16:57
 */
@SpringBootTest
class MobilePhoneRespositoryTest {
    @Autowired
    MobilePhoneRespository respository;

    @Test
    public void test() {
        MobilePhone mobilePhone = new MobilePhone();
        mobilePhone.setSku(1L);
        mobilePhone.setLink("11");
        mobilePhone.setName("111");
        mobilePhone.setPrice(new BigDecimal(10.3));
        mobilePhone.setShopName("test");
        respository.save(mobilePhone);
    }
}