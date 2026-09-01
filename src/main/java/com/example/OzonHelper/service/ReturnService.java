package com.example.OzonHelper.service;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.domain.Return;
import com.example.OzonHelper.domain.mapper.ReturnMapper;
import com.example.OzonHelper.dto.response.returns.ReturnDto;
import com.example.OzonHelper.enums.ReturnVisualStatus;
import com.example.OzonHelper.util.SheetAnalyzer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

@Service
public class ReturnService {
    private static final long APPZ_4_ID = 23954972810000L;

    private final Map<String, OzonClient> clients;
    private final GoogleSheetsProperties sheetsProperties;
    private final GoogleClient googleClient;
    private final SheetAnalyzer sheetAnalyzer;
    private final ReturnMapper returnMapper;

    public ReturnService(Map<String, OzonClient> clients, GoogleSheetsProperties sheetsProperties,
                         GoogleClient googleClient, SheetAnalyzer sheetAnalyzer, ReturnMapper returnMapper) {
        this.clients = clients;
        this.sheetsProperties = sheetsProperties;
        this.googleClient = googleClient;
        this.sheetAnalyzer = sheetAnalyzer;
        this.returnMapper = returnMapper;
    }

    public void sendReturnBarcodeNotifications() {
        Map<OzonClient, List<Return>> returnsByClient = new HashMap<>();
        Map<OzonClient, Path> barcodePngsByClient = new HashMap<>();

        clients.forEach(
                (s, client) -> {
                    try {
                        List<ReturnDto> returnDtos = client.getReturns("FBS", ReturnVisualStatus.RETURN_VISUAL_STATUS_ARRIVED_AT_RETURN_PLACE);
                        List<Return> returns = returnDtos.stream().map(returnMapper::mapToModel)
                                .filter(aReturn -> aReturn.getWarehouse().getId() == APPZ_4_ID).toList();

                        if (!returns.isEmpty()) {
                            returnsByClient
                                    .computeIfAbsent(client, client1 -> new ArrayList<>())
                                    .addAll(returns);
                        }
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        returnsByClient.forEach(
                (client, returns) -> {
                    String png = null;
                    try {
                        png = client.getReturnBarcodePng();
                        byte[] decode = Base64.getDecoder().decode(png);
                        Path path = Path.of("data", "returns", "return-barcode_" + client.getShopName() + ".png");
                        Files.write(path, decode);
                        barcodePngsByClient.put(client, path);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                //for each client in barcodePngsByLegalEntity
                //send png image to chat + LegalEntity string
        );
    }
}
