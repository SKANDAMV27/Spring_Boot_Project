package com.prapthi.crud_demo.controller;

import com.prapthi.crud_demo.dto.EmployeeDetailsDTO;
import com.prapthi.crud_demo.service.EmployeeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeDetailsController {

    @Autowired
    private EmployeeDetailsService employeeDetailsService;

    @PostMapping("/save")
    public EmployeeDetailsDTO save(@RequestBody EmployeeDetailsDTO employeeDetailsDTO){
        System.out.println("Employee Details Data Saved");
        return employeeDetailsService.save(employeeDetailsDTO);
    }
}
