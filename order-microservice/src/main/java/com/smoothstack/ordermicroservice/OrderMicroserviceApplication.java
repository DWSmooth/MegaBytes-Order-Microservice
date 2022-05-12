package com.smoothstack.ordermicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.smoothstack.common")
@SpringBootApplication
public class OrderMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderMicroserviceApplication.class, args);
	}

}
