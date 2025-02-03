package com.gold_hunter.gold_hunter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class GoldHunterApplication {

    public static void main(String[] args) {
        SpringApplication.run(GoldHunterApplication.class, args);
    }
}