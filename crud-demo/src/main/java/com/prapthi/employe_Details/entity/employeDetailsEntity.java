package com.prapthi.employe_Details.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class employeDetailsEntity {

    private int id;
    private int empId;
    private String Address;
    private String Destination;
    private String emailId;

    private String CreatedBy;
    private Date creationTime;

    private String lastModifiedBy;
    private Date lastModifiedTime;

    private int isDeleted;
    private String deletedBy;
    private Date deletedTime;


}
