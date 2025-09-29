package com.prapthi.crud_demo.repositry;

import com.prapthi.crud_demo.entity.EmployeeDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetailsEntity,Integer> {


}
