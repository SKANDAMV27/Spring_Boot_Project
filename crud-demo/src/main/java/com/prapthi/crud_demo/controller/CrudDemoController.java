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

    @PostMapping("/saveData")
    public CrudDemoDto save(@RequestBody CrudDemoDto crudDemoDto) {
        return crudDemoService.save(crudDemoDto);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteData(@PathVariable int id) {
        return crudDemoService.delete(id);
    }

    @PutMapping("/update/{id}")
    public CrudDemoDto updateById(@PathVariable int id, @RequestBody CrudDemoDto crudDemoDto) {
        return crudDemoService.updateById(id, crudDemoDto);
    }

    @PutMapping("/updateByName/{name}")
    public List<CrudDemoDto> updateByName(@PathVariable String name, @RequestBody CrudDemoDto crudDemoDto) {
        return crudDemoService.updateByName(name, crudDemoDto);
    }

    @GetMapping("/get/{id}")
    public CrudDemoDto readById(@PathVariable int id) {
        return crudDemoService.readById(id);
    }

    @GetMapping("/getAll")
    public List<CrudDemoDto> readAll() {
        return crudDemoService.readAll();
    }

    @GetMapping("/getByName/{name}")
    public List<CrudDemoDto> readByName(@PathVariable String name) {
        return crudDemoService.readByName(name);
    }

    @GetMapping("/getByEmail/{email}")
    public List<CrudDemoDto> readByEmail(@PathVariable String email){
        System.out.println("Controller Layer Is Running");
        System.out.println("Read By Email");
        return crudDemoService.readByEmail(email);
    }
}
