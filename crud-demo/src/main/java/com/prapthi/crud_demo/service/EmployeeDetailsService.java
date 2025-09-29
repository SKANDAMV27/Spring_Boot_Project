package com.prapthi.crud_demo.service;

import com.prapthi.crud_demo.dto.EmployeeDetailsDTO;
import com.prapthi.crud_demo.entity.EmployeeDetailsEntity;
import com.prapthi.crud_demo.repositry.EmployeeDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDetailsService {

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    public EmployeeDetailsEntity toEntity(EmployeeDetailsDTO employeeDetailsDTO){
        EmployeeDetailsEntity employeeDetailsEntity = new EmployeeDetailsEntity();
        employeeDetailsEntity.setId(employeeDetailsDTO.getId());
        employeeDetailsEntity.setEmpId(employeeDetailsDTO.getEmpId());
        employeeDetailsEntity.setAddress(employeeDetailsDTO.getAddress());
        employeeDetailsEntity.setDestination(employeeDetailsDTO.getDestination());
        employeeDetailsEntity.setEmailId(employeeDetailsDTO.getEmailId());
        employeeDetailsEntity.setCreatedBy(employeeDetailsDTO.getCreatedBy());
        employeeDetailsEntity.setCreationTime(employeeDetailsDTO.getCreationTime());
        employeeDetailsEntity.setLastModifiedBy(employeeDetailsDTO.getLastModifiedBy());
        employeeDetailsEntity.setLastModifiedTime(employeeDetailsDTO.getLastModifiedTime());
        employeeDetailsEntity.setIsDeleted(employeeDetailsDTO.getIsDeleted());
        employeeDetailsEntity.setDeletedBy(employeeDetailsDTO.getDeletedBy());
        employeeDetailsEntity.setDeletedTime(employeeDetailsDTO.getDeletedTime());
        return employeeDetailsEntity;
    }

    public EmployeeDetailsDTO detailsDTO(EmployeeDetailsEntity entity){
        return new EmployeeDetailsDTO(entity.getId(),
                entity.getEmpId(),
                entity.getAddress(),
                entity.getDestination(),
                entity.getEmailId(),
                entity.getCreatedBy(),
                entity.getCreationTime(),
                entity.getLastModifiedBy(),
                entity.getLastModifiedTime(),
                entity.getIsDeleted(),
                entity.getDeletedBy(),
                entity.getDeletedTime());
    }

    public EmployeeDetailsDTO save(){
        System.out.println("DataSaved Successfully");
        return employeeDetailsRepository.save()
    }
}
