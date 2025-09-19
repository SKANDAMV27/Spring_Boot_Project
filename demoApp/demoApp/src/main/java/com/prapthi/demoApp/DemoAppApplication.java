package com.prapthi.demoApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DemoAppApplication {

	public static void main(String[] args) {
        System.out.println("Spring");



		    ApplicationContext applicationContext = SpringApplication.run(DemoAppApplication.class, args);

               Hello object =  applicationContext.getBean(Hello.class);

               object.name();




	}

}
