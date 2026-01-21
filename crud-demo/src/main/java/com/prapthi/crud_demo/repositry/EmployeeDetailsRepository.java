package com.prapthi.crud_demo.repositry;

import com.prapthi.crud_demo.dto.EmployeeWithCrudDto;
import com.prapthi.crud_demo.entity.EmployeeDetailsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDetailsRepository extends JpaRepository<EmployeeDetailsEntity,Integer> {


    @Query("""
SELECT new com.prapthi.crud_demo.dto.EmployeeWithCrudDto(
    e.empId,
    e.emailId,
    e.address,
    e.destination,
    c.name,
    c.salary,
    c.dateOfBirth,
    c.mobileNumber
)
FROM EmployeeDetailsEntity e
LEFT JOIN CrudDemoEntity c ON e.empId = c.id
WHERE e.isDeleted = 0 
""")
    List<EmployeeWithCrudDto> fetchEmployeeWithCrudDto();

    @Query("select new com.prapthi.crud_demo.dto.EmployeeWithCrudDto(" +
            "e.empId,e.emailId, e.address,e.destination,c.name,c.salary,c.dateOfBirth,c.mobileNumber) From EmployeeDetailsEntity e INNER JOIN CrudDemoEntity c ON e.empId=c.id where e.isDeleted=0")
    List<EmployeeWithCrudDto> fetchTheDataByInnerJoin();

    @Query("select new com.prapthi.crud_demo.dto.EmployeeWithCrudDto(e.empId,e.emailId,e.address,e.destination,c.name,c.salary,c.dateOfBirth,c.mobileNumber) From EmployeeDetailsEntity e RIGHT JOIN CrudDemoEntity c ON e.empId=c.id where e.isDeleted=0")
    List<EmployeeWithCrudDto> fetchTheDataByRightJoin();

    @Query("select new com.prapthi.crud_demo.dto.EmployeeWithCrudDto(e.empId,e.emailId,e.address,e.destination,c.name,c.salary,c.dateOfBirth,c.mobileNumber) From EmployeeDetailsEntity e FULL JOIN CrudDemoEntity c ON e.empId=c.id where e.isDeleted=0")
    List<EmployeeWithCrudDto> fetchTheDataByFullJoin();


    @Query("""
    SELECT e FROM EmployeeDetailsEntity e
    WHERE e.isDeleted = 0 
      AND (LOWER(e.emailId) LIKE :search
           OR LOWER(e.address) LIKE :search
           OR LOWER(e.destination) LIKE :search)
""")
    Page<EmployeeDetailsEntity> searchTheEmployee(@Param("search") String search, Pageable pageable);

}



