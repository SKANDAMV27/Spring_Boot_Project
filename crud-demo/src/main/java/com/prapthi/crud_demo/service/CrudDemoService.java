package com.prapthi.crud_demo.service;

import com.prapthi.crud_demo.dto.CrudDemoDto;
import com.prapthi.crud_demo.entity.CrudDemoEntity;
import com.prapthi.crud_demo.repositry.CrudDemoRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CrudDemoService {

    @Autowired
    private CrudDemoRepositry crudDemoRepositry;

    private CrudDemoDto toDto(CrudDemoEntity entity){
        return new CrudDemoDto(
                entity.getId(),
                entity.getName(),
                entity.getEmailId(),
                entity.getSalary(),
                entity.getDateOfBirth(),
                entity.getMobileNumber()
        );
    }

    private CrudDemoEntity toEntity(CrudDemoDto dto){
        CrudDemoEntity entity = new CrudDemoEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setEmailId(dto.getEmailId());
        entity.setSalary(dto.getSalary());
        entity.setDateOfBirth(dto.getDateOfBirth());
        entity.setMobileNumber(dto.getMobileNumber());
        return entity;
    }

    //Insert the Data
    public CrudDemoDto save(CrudDemoDto crudDemoDto){
        CrudDemoEntity entity = toEntity(crudDemoDto);
        CrudDemoEntity savedEntity = crudDemoRepositry.save(entity);
        return toDto(savedEntity);
    }


    //Delete The Data By The id
    public boolean delete(int id){
        if(!crudDemoRepositry.existsById(id)){
            System.out.println("Id does not exist in the Database");
            return false;
        }
        crudDemoRepositry.deleteById(id);
        System.out.println("Successfully Deleted");
        return true;
    }
    //Update By id
    public CrudDemoDto updateById(int id, CrudDemoDto crudDemoDto){
        return crudDemoRepositry.findById(id).map(exist -> {
            if(crudDemoDto.getName()!=null) exist.setName(crudDemoDto.getName());
            if(crudDemoDto.getEmailId()!=null) exist.setEmailId(crudDemoDto.getEmailId());
            if(crudDemoDto.getSalary()!=null) exist.setSalary(crudDemoDto.getSalary());
            if(crudDemoDto.getDateOfBirth()!=null) exist.setDateOfBirth(crudDemoDto.getDateOfBirth());
            if(crudDemoDto.getMobileNumber()!=null) exist.setMobileNumber(crudDemoDto.getMobileNumber());
            CrudDemoEntity saved = crudDemoRepositry.save(exist);
            return toDto(saved);
        }).orElse(null);
    }

    //Read By id
    public CrudDemoDto readById(int id){
        return crudDemoRepositry.findById(id).map(this::toDto).orElse(null);
    }

    //Read All The Data
    public List<CrudDemoDto> readAll(){
        return crudDemoRepositry.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    //Read The Data By The Name
    public List<CrudDemoDto> readByName(String name){
        List<CrudDemoEntity> entities = crudDemoRepositry.findByName(name);
        if (entities.isEmpty()) {
            System.out.println("No records found for name: " + name);
            return Collections.emptyList();
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    //Read The Data By the Email
    public List<CrudDemoDto> readByEmail(String emailId){
        System.out.println("Read The Data By Email");
        List<CrudDemoEntity> readByEmail = crudDemoRepositry.findByEmailId(emailId);
        if(readByEmail.isEmpty()){
            System.out.println("Email Record exist");
            return Collections.emptyList();
        }
        return readByEmail.stream().map(this::toDto).collect(Collectors.toList());
    }
}
