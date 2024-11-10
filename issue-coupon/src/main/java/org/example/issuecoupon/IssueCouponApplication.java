package org.example.issuecoupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.example.coupon", "org.example.issuecoupon"})
public class IssueCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(IssueCouponApplication.class, args);
    }

}
