package com.prapthi.crud_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWithCrudDto {


        private int empId;
        private String emailId;
        private String address;
        private String destination;
        private String name;
        private Integer salary;

}
