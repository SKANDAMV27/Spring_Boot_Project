package com.prapthi.webSpringBoot.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class DTO {

    private int age;
    private String name;
    private String address;

    public DTO(int age, String name, String address) {
        this.age = age;
        this.name = name;
        this.address = address;
    }
}
