package com.prapthi.crud_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalaryStatusDTO {

    private Long salarySum;

    private double salaryAverage;

    private int numberOfEmployee;


}
