package com.prapthi.crud_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SalaryStatusDTO {

    private Long salarySum;
    private Double salaryAverage;
    private Long numberOfEmployee;


    public SalaryStatusDTO(Long salarySum, Double salaryAverage, Long numberOfEmployee) {
        this.salarySum = salarySum;
        this.salaryAverage = salaryAverage;
        this.numberOfEmployee = numberOfEmployee;
    }
}
