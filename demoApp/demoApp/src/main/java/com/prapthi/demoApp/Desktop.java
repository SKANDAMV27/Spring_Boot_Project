package com.prapthi.demoApp;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component

public class Desktop implements Computer{
    @Override
    public void name() {
        System.out.println("Desktop is also");
    }
}
