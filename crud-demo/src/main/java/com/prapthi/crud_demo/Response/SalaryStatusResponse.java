package com.prapthi.crud_demo.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryStatusResponse {

    private Long salarySum;

    private double salaryAverage;

    private int numberOfEmployee;
}
