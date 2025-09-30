package com.prapthi.crud_demo.controller;

import com.prapthi.crud_demo.dto.EmployeeDetailsDTO;
import com.prapthi.crud_demo.dto.EmployeeWithCrudDto;
import com.prapthi.crud_demo.entity.EmployeeDetailsEntity;
import com.prapthi.crud_demo.service.EmployeeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @PutMapping("/update/{id}")
    public Optional<EmployeeDetailsDTO> updateTheData(@PathVariable int id,@RequestBody EmployeeDetailsDTO employeeDetailsDTO){
        System.out.println("Data Updated");
        return employeeDetailsService.updated(id,employeeDetailsDTO);
    }

    @DeleteMapping("/delete/{id}")
    public Optional<EmployeeDetailsDTO> deleteById(@PathVariable int id,@RequestBody EmployeeDetailsDTO employeeDetailsDTO){
        System.out.println("Delete The Data");
        return employeeDetailsService.deleteById(id,employeeDetailsDTO);
    }

    @GetMapping("getById/{id}")
    public Optional<EmployeeDetailsEntity> getAll(@RequestBody EmployeeDetailsDTO employeeDetailsDTO,@PathVariable int id){
        System.out.println("Get The Data By The");
        return employeeDetailsService.getAll(employeeDetailsDTO,id);
    }

    @GetMapping("/leftJoin")
    public List<EmployeeWithCrudDto> getEmployeeWithCrudData(){
        System.out.println("Get The Data of Employee With CRUD Data");
        return employeeDetailsService.getEmployeeWithCrudData();
    }

    @GetMapping("/innerJoin")
    public List<EmployeeWithCrudDto> getTheDataByInnerJoin(){
        System.out.println("Get The Data By The INNER JOIN");
        return employeeDetailsService.getDataByInnerJoin();
    }

    public List<EmployeeWithCrudDto> getTheDataByRightJoin(){
        System.out.println("Get The Data By The Right Join");
        return employeeDetailsService.getDataByRightJoin();
    }


}
