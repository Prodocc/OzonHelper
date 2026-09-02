package com.example.OzonHelper.service;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.MaxClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.GoogleSheetsProperties;
import com.example.OzonHelper.domain.Return;
import com.example.OzonHelper.domain.mapper.ReturnMapper;
import com.example.OzonHelper.dto.response.returns.ReturnDto;
import com.example.OzonHelper.enums.ozon.ReturnVisualStatus;
import com.example.OzonHelper.util.SheetAnalyzer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class ReturnService {
    private static final long APPZ_4_ID = 23954972810000L;
    private static final String SCHEMA_FBS = "FBS";
    private static final String SCHEMA_FBO = "FBO";

    private final Map<String, OzonClient> clients;
    private final GoogleSheetsProperties sheetsProperties;
    private final MaxClient maxClient;
    private final GoogleClient googleClient;
    private final SheetAnalyzer sheetAnalyzer;
    private final ReturnMapper returnMapper;

    public ReturnService(Map<String, OzonClient> clients, GoogleSheetsProperties sheetsProperties,
                         GoogleClient googleClient, MaxClient maxClient, SheetAnalyzer sheetAnalyzer, ReturnMapper returnMapper) {
        this.clients = clients;
        this.sheetsProperties = sheetsProperties;
        this.googleClient = googleClient;
        this.maxClient = maxClient;
        this.sheetAnalyzer = sheetAnalyzer;
        this.returnMapper = returnMapper;
    }

    public void sendReturnBarcodeNotifications(String chatId) {
        Map<String, List<Return>> returnsByLegalEntity = new HashMap<>();
        Map<String, OzonClient> clientsByLegalEntity = new HashMap<>();
        Map<String, Path> barcodePngsByLegalEntity = new HashMap<>();

        clients.forEach(
                (s, client) -> {
                    try {
                        List<ReturnDto> returnDtos = client.getReturns(SCHEMA_FBS, ReturnVisualStatus.RETURN_VISUAL_STATUS_ARRIVED_AT_RETURN_PLACE);
                        List<Return> returns = returnDtos.stream().map(returnMapper::mapToModel)
                                .filter(aReturn -> aReturn.getWarehouse().getId() == APPZ_4_ID).toList();

                        if (!returns.isEmpty()) {
                            returnsByLegalEntity
                                    .computeIfAbsent(client.getLegalEntity(), legalEntity -> new ArrayList<>())
                                    .addAll(returns);
                            clientsByLegalEntity.putIfAbsent(client.getLegalEntity(), client);
                        }
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
        );

        System.out.println(returnsByLegalEntity);
        System.out.println(clientsByLegalEntity);

        returnsByLegalEntity.keySet().forEach(legalEntity -> {
            try {
                OzonClient client = clientsByLegalEntity.get(legalEntity);

                String png = client.getReturnBarcodePng();
                byte[] decode = Base64.getDecoder().decode(png);
                Path path = Path.of("data", "returns", "return-barcode_" + client.getShopName() + ".png");
                Files.write(path, decode);
                barcodePngsByLegalEntity.put(legalEntity, path);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        System.out.println(barcodePngsByLegalEntity);

        for (String legalEntity : barcodePngsByLegalEntity.keySet()) {
            try {
                StringBuilder sb = new StringBuilder();
                for (Return shopReturn : returnsByLegalEntity.get(legalEntity)) {
                    sb.append(shopReturn.getProduct().getQuantity());
                    sb.append(" - ");
                    sb.append(shopReturn.getProduct().getArticle());
                    sb.append("\n");
                }
                sb.append(returnsByLegalEntity.get(legalEntity).get(0).getWarehouse().getAddress());
                sb.append("\n");
                sb.append(legalEntity);

                maxClient.sendImage(chatId, sb.toString(), barcodePngsByLegalEntity.get(legalEntity));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
