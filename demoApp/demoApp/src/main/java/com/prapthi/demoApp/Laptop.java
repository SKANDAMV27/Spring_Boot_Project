package com.prapthi.demoApp;

import org.springframework.stereotype.Component;

@Component
public class Laptop implements Computer {



    @Override
    public void name() {
        System.out.println("Laptop Is Working");
    }
}
