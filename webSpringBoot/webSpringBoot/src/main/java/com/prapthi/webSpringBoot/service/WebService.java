package com.prapthi.webSpringBoot.service;


import com.prapthi.webSpringBoot.DTO.DTO;
import com.prapthi.webSpringBoot.controller.WebController;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WebService {


    List<DTO> products = Arrays.asList(new DTO(20,"Skanda M V","Thirthahalli"),
            new DTO(24,"Raju","Shivamogga"),
            new DTO(26,"Amith","Banglore"));

    public List<DTO> product(){
        return products;
    }
}
