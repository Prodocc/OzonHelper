package com.example.OzonHelper.parser;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.util.List;

public class JacksonCsvParser<T> implements Parser<T> {
    private final CsvMapper csvMapper;
    private final Class<T> type;

    public JacksonCsvParser(Class<T> type) {
        this.type = type;
        this.csvMapper = new CsvMapper();
        csvMapper.registerModule(new JavaTimeModule());
        csvMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public List<T> parse(String csvData) {
        CsvSchema schema = CsvSchema.emptySchema()
                .withHeader()
                .withColumnSeparator(';');

        try (MappingIterator<T> rowIterator = csvMapper.readerFor(type)
                .with(schema)
                .readValues(csvData)) {

            return rowIterator.readAll();
        } catch (IOException e) {
            System.err.println("Ошибка при парсинге CSV: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
