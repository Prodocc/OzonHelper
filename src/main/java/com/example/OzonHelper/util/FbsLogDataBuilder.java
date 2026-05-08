package com.example.OzonHelper.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Component
public class FbsLogDataBuilder {

    public List<List<Object>> createFbsPostingData() {
        System.out.println("Create fbs posting data");

        return List.of(
                Arrays.asList("", "FBS", "нарвская", "", "", "маркетплейсы")
        );
    }

    public List<List<Object>> createScopeStartData() {
        System.out.println("Create scope start");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String today = LocalDate.now().format(formatter);

        return List.of(
                Arrays.asList(today, "", "", "", "", "")
        );
    }
}
