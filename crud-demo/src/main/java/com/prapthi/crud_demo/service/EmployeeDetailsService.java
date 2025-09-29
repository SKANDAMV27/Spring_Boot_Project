package com.prapthi.crud_demo.service;

import com.prapthi.crud_demo.dto.EmployeeDetailsDTO;
import com.prapthi.crud_demo.entity.EmployeeDetailsEntity;
import com.prapthi.crud_demo.repositry.EmployeeDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class EmployeeDetailsService {

    @Autowired
    private EmployeeDetailsRepository employeeDetailsRepository;

    public EmployeeDetailsEntity toEntity(EmployeeDetailsDTO employeeDetailsDTO){
        EmployeeDetailsEntity employeeDetailsEntity = new EmployeeDetailsEntity();
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


    public EmployeeDetailsDTO save(EmployeeDetailsDTO employeeDetailsDTO){
        System.out.println("DataSaved Successfully");
        EmployeeDetailsEntity entity = toEntity(employeeDetailsDTO);
        entity.setCreatedBy("Skanda M V");
        entity.setCreationTime(new Date());
        EmployeeDetailsEntity savedEntity = employeeDetailsRepository.save(entity);
        return detailsDTO(savedEntity);
    }

    public Optional<EmployeeDetailsDTO> updated(int id, EmployeeDetailsDTO employeeDetailsDTO){
        System.out.println("Update the Employee Details");
        return employeeDetailsRepository.findById(id).map(exist ->{
                if(employeeDetailsDTO.getAddress()!=null) exist.setAddress(employeeDetailsDTO.getAddress());
                if(employeeDetailsDTO.getEmailId()!=null) exist.setEmailId(employeeDetailsDTO.getEmailId());
                if(employeeDetailsDTO.getDestination()!=null) exist.setDestination(employeeDetailsDTO.getDestination());
                exist.setLastModifiedBy("Skanda M V");
                exist.setLastModifiedTime(new Date());
                EmployeeDetailsEntity saved = employeeDetailsRepository.save(exist);
                return detailsDTO(saved);
        });
    }
}
