package com.xwcet.tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class WebToolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebToolsApplication.class, args);
	}
}
