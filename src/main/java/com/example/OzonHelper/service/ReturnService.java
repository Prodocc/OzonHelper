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
import java.util.function.Predicate;

@Service
public class ReturnService {
    private static final long APPZ_4_ID = 23954972810000L;
    private static final String SCHEMA_FBS = "FBS";
    private static final String SCHEMA_FBO = "FBO";

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
        Map<String, List<Return>> returnsByLegalEntity = new HashMap<>();
        Map<OzonClient, Path> barcodePngsByClient = new HashMap<>();

        clients.forEach(
                (s, client) -> {
                    try {
                        List<ReturnDto> returnDtos = client.getReturns(SCHEMA_FBS, ReturnVisualStatus.RETURN_VISUAL_STATUS_ARRIVED_AT_RETURN_PLACE);
                        List<Return> returns = returnDtos.stream().map(returnMapper::mapToModel)
                                .filter(aReturn -> aReturn.getWarehouse().getId() == APPZ_4_ID).toList();

                        if (!returns.isEmpty()) {
                            returnsByLegalEntity
                                    .computeIfAbsent(client.getLegalEntity(), client1 -> new ArrayList<>())
                                    .addAll(returns);
                        }
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        System.out.println(returnsByLegalEntity);

        returnsByLegalEntity.forEach(
                (legalEntity, returns) -> {
                    String png = null;
                    try {
                        OzonClient ozonClient = clients
                                .values()
                                .stream()
                                .filter(client -> client.getLegalEntity().equals(legalEntity))
                                .findFirst().get();

                        png = ozonClient.getReturnBarcodePng();
                        byte[] decode = Base64.getDecoder().decode(png);
                        Path path = Path.of("data", "returns", "return-barcode_" + ozonClient.getShopName() + ".png");
                        Files.write(path, decode);
                        barcodePngsByClient.put(ozonClient, path);
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });

        System.out.println(barcodePngsByClient);

        for (OzonClient client : barcodePngsByClient.keySet()){

        }
//
//                //for each client in barcodePngsByLegalEntity
//                //send png image to chat + LegalEntity string
//        );
    }
}
