package com.prapthi.crud_demo.service;

import com.prapthi.crud_demo.dto.CrudDemoDto;
import com.prapthi.crud_demo.entity.CrudDemoEntity;
import com.prapthi.crud_demo.repositry.CrudDemoRepositry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        if(crudDemoRepositry.existsById(id)){
            crudDemoRepositry.deleteById(id);
            return true;
        }
        return false;
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


}
