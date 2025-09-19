package com.prapthi.demoWebApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebAppDTO {
    private int id;
    private String name;
    private String city;
}
