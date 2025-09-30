package com.prapthi.crud_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailsDTO {


    private int id;
    private int empId;
    private String address;
    private String destination;
    private String emailId;

    private String CreatedBy;
    private Date creationTime;

    private String lastModifiedBy;
    private Date lastModifiedTime;

    private int isDeleted;
    private String deletedBy;
    private Date deletedTime;
}
