package com.tarmiz.SIRH_backend;

import com.tarmiz.SIRH_backend.config.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class SirhBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SirhBackendApplication.class, args);
	}

}
