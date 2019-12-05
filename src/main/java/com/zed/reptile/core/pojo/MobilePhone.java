package com.zed.reptile.core.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author Zeluo
 * @date 2019/12/3 16:47
 */
@Data
@Entity(name = "jd")
public class MobilePhone {
    @Id
    private Long sku;

    private String name;

    private String shopName;

    private BigDecimal price;

    private String link;

    private String original;

    private int page;
}
