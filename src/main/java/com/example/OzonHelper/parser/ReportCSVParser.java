package com.example.OzonHelper.parser;

import com.opencsv.*;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class ReportCSVParser {
    private static final int POSTINGS_STATUS_INDEX = 4;

    public List<List<String>> downloadCSV(String url) throws IOException, CsvException {
        List<List<String>> result = new ArrayList<>();

        ICSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .withQuoteChar('"')
                .build();

        try (CSVReader reader = new CSVReaderBuilder(
                new InputStreamReader(new URL(url).openStream(), StandardCharsets.UTF_8))
                .withCSVParser(parser)
                .build()) {
            reader.skip(1);

            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                List<String> row = new ArrayList<>(Arrays.asList(nextLine));
                result.add(row);
            }
        }
        return result;
    }

    public List<List<String>> filterCSV(List<List<String>> records, String filter) {
        return records.stream().
                filter(row -> {
                    return !row.get(POSTINGS_STATUS_INDEX).equals(filter);
                }).
                toList();
    }
}
