package com.nitb.planservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(scanBasePackages = {"com.study.planservice", "com.study.common"})
@EnableDiscoveryClient
public class PlanServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlanServiceApplication.class, args);
    }

}
