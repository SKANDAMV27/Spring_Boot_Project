package com.prapthi.demoApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")

@Component
public class Hello  implements  Computer{


    private Computer computer;

    public Hello (@Qualifier("laptop") Computer computer){
        this.computer=computer;
    }

    public void name(){
        computer.name();
        System.out.println("Hello");
        System.out.println("Welcome To Spring Boot");
    }
}
