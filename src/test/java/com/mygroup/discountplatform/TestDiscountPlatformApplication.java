package com.mygroup.discountplatform;

import org.springframework.boot.SpringApplication;

public class TestDiscountPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.from(DiscountPlatformApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
