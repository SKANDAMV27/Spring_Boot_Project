package com.prapthi.crud_demo.controller;

import com.prapthi.crud_demo.Response.CrudResponse;
import com.prapthi.crud_demo.Response.SalaryStatusResponse;
import com.prapthi.crud_demo.dto.CrudDemoDto;
import com.prapthi.crud_demo.entity.CrudDemoEntity;
import com.prapthi.crud_demo.service.CrudDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CrudDemoController {

    @Autowired
    private CrudDemoService crudDemoService;

    @PostMapping("/save")
    public CrudDemoDto save(@RequestBody CrudDemoDto crudDemoDto) {
        System.out.println("Data Saved Successfully");
        return crudDemoService.save(crudDemoDto);
    }

    @DeleteMapping("deleteId/{id}")
    public boolean deleteData(@PathVariable int id) {
        System.out.println("Data Delete Successfully By Id");
        return crudDemoService.delete(id);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAll(){
        System.out.println("Delete All tThe Data");
        crudDemoService.deleteAll();
    }

    @PutMapping("putId/{id}")
    public CrudDemoDto updateById(@PathVariable int id, @RequestBody CrudDemoDto crudDemoDto) {
        System.out.println("Data Update By Id");
        return crudDemoService.updateById(id, crudDemoDto);
    }

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

    @DeleteMapping("/softDelete/{id}")
    public CrudResponse softDelete(@PathVariable int id){
        System.out.println("Soft Delete");

         try{
             crudDemoService.softDelete(id);
             System.out.println("Data Saved Successfully");
             return new CrudResponse("Delete Successfully","Deleted Id:"+id);

         }catch (Exception e){
             System.out.println("Error: "+e.getMessage());
             return new CrudResponse("Error: "+e.getMessage(),e);
         }
    }

    @GetMapping("/salaryStatus")
    public SalaryStatusResponse getSalaryStatus(){
        System.out.println("Get The Salary Details");
        return crudDemoService.salaryDetails();
    }


}
