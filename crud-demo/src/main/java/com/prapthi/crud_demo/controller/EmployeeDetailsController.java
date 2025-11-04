package com.prapthi.crud_demo.controller;

import com.prapthi.crud_demo.Response.PageResponse;
import com.prapthi.crud_demo.Response.PageResponse;
import com.prapthi.crud_demo.dto.EmployeeDetailsDTO;
import com.prapthi.crud_demo.dto.EmployeeWithCrudDto;
import com.prapthi.crud_demo.entity.EmployeeDetailsEntity;
import com.prapthi.crud_demo.service.EmployeeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @DeleteMapping("/deleteById/{id}")
    public Optional<EmployeeDetailsEntity> delete(@PathVariable int id){
        System.out.println("Delete The Data From The Table");
        return employeeDetailsService.deleteById(id);
    }

    @DeleteMapping("/softDelete/{id}")
    public Optional<EmployeeDetailsDTO> deleteById(@PathVariable int id){
        System.out.println("Delete The Data");
        return employeeDetailsService.softDeleteById(id);
    }

    @GetMapping("/getById/{id}")
    public Optional<EmployeeDetailsEntity> getAll(@PathVariable int id){
        System.out.println("Get The Data By The");
        return employeeDetailsService.getAll(id);
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

    @GetMapping("/rightJoin")
    public List<EmployeeWithCrudDto> getTheDataByRightJoin(){
        System.out.println("Get The Data By The Right Join");
        return employeeDetailsService.getDataByRightJoin();
    }

    @GetMapping("/fullJoin")
    public List<EmployeeWithCrudDto> getTheDataByFullJoin(){
        System.out.println("Get The Data By The Full Join");
        return employeeDetailsService.getDataByFullJoin();
    }

    @GetMapping("/search")
    public PageResponse<EmployeeDetailsDTO> searchEmployee(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "") String search
    ){

        Page<EmployeeDetailsDTO> result = employeeDetailsService.searchTheEmployee(page, size, sortBy, sortDir, search);
        return new PageResponse<>(result);
    }


}
