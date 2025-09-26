package com.prapthi.crud_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrudDemoDto {

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
