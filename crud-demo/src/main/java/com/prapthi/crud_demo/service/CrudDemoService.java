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

    public CrudDemoDto save(CrudDemoDto crudDemoDto){

        CrudDemoEntity entity = toEntity(crudDemoDto);
        CrudDemoEntity savedEntity = crudDemoRepositry.save(entity);
        return toDto(savedEntity);
    }

    public boolean delete(int id){
        if(!crudDemoRepositry.existsById(id)){
            System.out.println("Id Does not exist on the Database");
            return false;

        }
        System.out.println("Successfully Deleted");
        crudDemoRepositry.deleteById(id);
        return true;

    }

    public CrudDemoDto updateById(int id, CrudDemoDto crudDemoDto){
        System.out.println("Update the Data Using Id");
        return crudDemoRepositry.findById(id).map(exist->{
            if(crudDemoDto.getName()!=null) exist.setName(crudDemoDto.getName());
            if(crudDemoDto.getEmailId()!=null) exist.setEmailId(crudDemoDto.getEmailId());
            if(crudDemoDto.getSalary()!=null) exist.setSalary(crudDemoDto.getSalary());
            if(crudDemoDto.getDateOfBirth()!=null) exist.setDateOfBirth(crudDemoDto.getDateOfBirth());
            if(crudDemoDto.getMobileNumber()!=null) exist.setMobileNumber(crudDemoDto.getMobileNumber());

            CrudDemoEntity save = crudDemoRepositry.save(exist);
            return toDto(save);
        }).orElse(null);
    }

    public CrudDemoDto updateByName(String name,CrudDemoDto crudDemoDto){
        System.out.println("Update By Name Service Layer");
        List<CrudDemoEntity> entity = Collections.singletonList((CrudDemoEntity) crudDemoRepositry.findByName(name));
        for(CrudDemoEntity exist:entity){
            if(crudDemoDto.getName()!=null)exist.setName(crudDemoDto.getName());
            if(crudDemoDto.getEmailId()!=null)exist.setEmailId(crudDemoDto.getEmailId());
            if(crudDemoDto.getSalary()!=null)exist.setSalary(crudDemoDto.getSalary());
            if(crudDemoDto.getDateOfBirth()!=null)exist.setDateOfBirth(crudDemoDto.getDateOfBirth());
            crudDemoRepositry.save(exist);
        }
        return (CrudDemoDto) entity.stream().map(this::toDto).toList();

    }

    public CrudDemoDto readById(int id){
        System.out.println("Read By Id");

//        return crudDemoRepositry.
//                findById(id).
//                map(this::toDto)
//                .orElse(null);

        CrudDemoEntity readById = crudDemoRepositry.findById(id).orElse(null);
        return toDto(readById);

    }

    public List<CrudDemoDto> readAll(CrudDemoDto crudDemoDtor){
        System.out.println("Read All The Data");
        return crudDemoRepositry.findAll().
                stream().
                map(this::toDto).
                collect(Collectors.toList());


    }

}
