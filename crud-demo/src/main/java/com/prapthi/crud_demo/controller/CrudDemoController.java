package com.prapthi.crud_demo.controller;

import com.prapthi.crud_demo.dto.CrudDemoDto;
import com.prapthi.crud_demo.service.CrudDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CrudDemoController {

    @Autowired
    private CrudDemoService crudDemoService;

    @PostMapping("/")
    public CrudDemoDto save(@RequestBody CrudDemoDto crudDemoDto) {
        System.out.println("Data Saved Successfully");
        System.out.println(crudDemoDto);
        return crudDemoService.save(crudDemoDto);
    }

    @DeleteMapping("deleteId/{id}")
    public boolean deleteData(@PathVariable int id) {
        System.out.println("Data Delete Successfully By Id");
        return crudDemoService.delete(id);
    }

    @DeleteMapping("deleteName/{name}")
    public boolean deleteByName(@PathVariable String name){
        System.out.println("Data Delete Successfully By Name");
        return crudDemoService.deleteByName(name);
    }

    @DeleteMapping("deleteEmail/{emailId}")
    public boolean deleteByEmailId(@PathVariable String emailId){
        System.out.println("Data Delete Successfully By EmailId: "+emailId);
        return crudDemoService.deleteByEmailId(emailId);
    }

    @PutMapping("putId/{id}")
    public CrudDemoDto updateById(@PathVariable int id, @RequestBody CrudDemoDto crudDemoDto) {
        System.out.println("Data Update By Id");
        return crudDemoService.updateById(id, crudDemoDto);
    }

//    @PutMapping("/updateByName/{name}")
//    public List<CrudDemoDto> updateByName(@PathVariable String name, @RequestBody CrudDemoDto crudDemoDto) {
//        return crudDemoService.updateByName(name, crudDemoDto);
//    }

    @GetMapping("getId/{id}")
    public CrudDemoDto readById(@PathVariable int id) {
        System.out.println("Data Get By Name");
        return crudDemoService.readById(id);
    }

    @GetMapping("/")
    public List<CrudDemoDto> readAll() {
        System.out.println("Get All The Data");
        return crudDemoService.readAll();
    }

    @GetMapping("getName/{name}")
    public List<CrudDemoDto> readByName(@PathVariable String name) {
        System.out.println("Get Data By The Name");
        return crudDemoService.readByName(name);
    }

    @GetMapping("getEmail/{email}")
    public List<CrudDemoDto> readByEmail(@PathVariable String email){
        System.out.println("Controller Layer Is Running");
        System.out.println("Read By Email");
        return crudDemoService.readByEmail(email);
    }
}
