package com.prapthi.demoWebApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoWebAppApplication {

	public static void main(String[] args) {
        System.out.println("Spring Boot");
		SpringApplication.run(DemoWebAppApplication.class, args);
        System.out.println("SpringBoot");
	}

}
