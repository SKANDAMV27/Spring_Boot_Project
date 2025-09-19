package com.prapthi.webSpringBoot.controller;

import com.prapthi.webSpringBoot.service.WebService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class WebController {


//    @RequestMapping("/")
//    public String name(){
//        System.out.println("Welcome");
//        return "welcome";
//    }
//
////    @RequestMapping("/")
//    @ResponseBody
//    public String age(){
//        System.out.println("Mame");
//        return "20";
//    }
    @Autowired
    private WebService webService;


    @RequestMapping("/")
    public String name(){
        System.out.println("Service Running");

        return webService.product().toString();
    }
}
