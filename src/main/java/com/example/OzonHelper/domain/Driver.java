package com.example.OzonHelper.domain;

import lombok.Data;

@Data
public class Driver {
    private String name;
    private String passportNumber;
    private String phoneNumber;

    public String toString() {
        return "водитель " + name + ", " + passportNumber + ", " + phoneNumber;
    }
}
