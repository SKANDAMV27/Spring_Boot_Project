package com.prapthi.demoWebApp.controller;

import com.prapthi.demoWebApp.dto.WebAppDTO;
import com.prapthi.demoWebApp.service.WebAppservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class WebAppController {

    @Autowired
    private WebAppservice webAppservice;

    // Get all records
    @GetMapping("/details")
    public List<WebAppDTO> detail() {
        return webAppservice.details();
    }

    // Get record by ID
    @GetMapping("/get/{id}")
    public WebAppDTO getById(@PathVariable int id) {
        return webAppservice.display(id);
    }

    // Save a new record
    @PostMapping("/save")
    public WebAppDTO saveData(@RequestBody WebAppDTO webAppDTO) {
        return webAppservice.save(webAppDTO);
    }

    // Update a record by ID
    @PutMapping("/update/{id}")
    public WebAppDTO updateById(@PathVariable int id, @RequestBody WebAppDTO webAppDTO) {
        return webAppservice.update(id, webAppDTO);
    }

    // Delete a record by ID
    @DeleteMapping("/delete/{id}")
    public boolean deleteData(@PathVariable int id) {
        return webAppservice.delete(id);
    }
}
