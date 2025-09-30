package com.prapthi.crud_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Entity
@Table(name="EMPLOYEE_DETAILS_ENTITY")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDetailsEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="emp_id")
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
