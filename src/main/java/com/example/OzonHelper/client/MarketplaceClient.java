package com.example.OzonHelper.client;

import com.example.OzonHelper.dto.csv.OzonPostingRow;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface MarketplaceClient {

    List<OzonPostingRow> getPostings(LocalDate dateFrom, LocalDate dateTo, String[] skus, String... delivery_schema) throws IOException, InterruptedException;

    String getShopName();
}
