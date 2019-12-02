package com.zed.reptile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ReptileDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReptileDemoApplication.class, args);
    }

}
