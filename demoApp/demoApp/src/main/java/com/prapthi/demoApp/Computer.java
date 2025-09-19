package com.prapthi.demoApp;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public interface Computer {

    void name();
}
