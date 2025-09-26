package com.prapthi.crud_demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrudDemoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String emailId;
    private Integer salary;
    private Date dateOfBirth;
    private Long mobileNumber;

    private int isDelete;
    private String deletedBy;
    private Date deletedTime;

    private int isGet;

    private int isPut;
}
