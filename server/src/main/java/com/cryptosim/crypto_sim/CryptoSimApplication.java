package com.cryptosim.crypto_sim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CryptoSimApplication {

	public static void main(String[] args) {

		SpringApplication.run(CryptoSimApplication.class, args);
	}

}
