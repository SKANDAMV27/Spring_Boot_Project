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

        System.out.println("Save Operation");
        return crudDemoService.save(crudDemoDto);
    }

    @DeleteMapping("/delete/{id}")
    public boolean delateData(@PathVariable int id, @RequestBody CrudDemoDto crudDemoDto) {

        System.out.println("Delete Operation");

        return crudDemoService.delete(id);
    }

    @PutMapping("/update/{id}")
    public CrudDemoDto updateById(@PathVariable int id, @RequestBody CrudDemoDto crudDemoDto) {

        System.out.println("Update The Data By The Id");

        return crudDemoService.updateById(id, crudDemoDto);
    }

    @GetMapping("/get/{id}")
    public CrudDemoDto readById(@PathVariable int id){

        System.out.println("Read The Data By Id.");
        return crudDemoService.readById(id);

    }

    @GetMapping("/getAll")
    public List<CrudDemoDto> readAll(@RequestBody CrudDemoDto crudDemoDto){

        System.out.println("Controller is Running");
        System.out.println("Real All The Data");
        System.out.println(crudDemoDto);
        return crudDemoService.readAll(crudDemoDto);

    }



}
