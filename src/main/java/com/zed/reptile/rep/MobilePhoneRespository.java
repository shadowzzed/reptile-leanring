package com.zed.reptile.rep;

import com.zed.reptile.core.pojo.MobilePhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author Zeluo
 * @date 2019/12/3 16:56
 */
@Component
public interface MobilePhoneRespository extends JpaRepository<MobilePhone, Long> {

}
