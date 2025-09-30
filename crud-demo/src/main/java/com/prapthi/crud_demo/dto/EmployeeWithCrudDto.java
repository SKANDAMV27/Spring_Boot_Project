package com.prapthi.crud_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWithCrudDto {


        private Integer empId;
        private String emailId;
        private String address;
        private String destination;
        private String name;
        private Integer salary;
        private Date dateOfBirth;
        private Long mobileNumber;

}
