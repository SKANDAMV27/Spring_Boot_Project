package com.prapthi.crud_demo.repositry;

import com.prapthi.crud_demo.dto.EmployeeWithCrudDto;
import com.prapthi.crud_demo.entity.EmployeeDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetailsEntity,Integer> {


    @Query("select new com.prapthi.crud_demo.dto.EmployeeWithCrudDto(e.empId, e.)")
    List<EmployeeWithCrudDto> fetchEmployeeWithCrudDto();


}
