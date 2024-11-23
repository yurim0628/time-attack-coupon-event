package org.example.couponkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CouponKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouponKafkaApplication.class, args);
	}

}
