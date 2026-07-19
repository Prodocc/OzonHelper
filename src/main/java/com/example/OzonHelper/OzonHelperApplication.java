package com.example.OzonHelper;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.domain.mapper.PostingDtoMapper;
import com.example.OzonHelper.dto.response.PostingsReportInfoResult;
import com.example.OzonHelper.dto.response.fbo.PostingDto;
import com.example.OzonHelper.dto.response.fbo.StockDto;
import com.example.OzonHelper.parser.ReportCSVParser;
import com.example.OzonHelper.service.ReportService;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@SpringBootApplication
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

        ReportService reportService = run.getBean("reportService", ReportService.class);
        reportService.updateReportTable();

//        Scheduler scheduler = run.getBean("scheduler", Scheduler.class);
//        scheduler.fillFBSLogListMorning();

//        Map<String, OzonClient> clients = run.getBean("ozonClient", Map.class);
//
//        Instant from = LocalDate.now().minusWeeks(3).atStartOfDay().toInstant(ZoneOffset.UTC);
//        Instant to = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
//
//        clients.forEach((s, client) -> {
//            String postingsReportCode = null;
//            try {
//                postingsReportCode = client.createPostingsReportCode(from.toString(), to.toString(), List.of("fbo"));
//                PostingsReportInfoResult postingsReportFile = client.getPostingsReportInfoByCode(postingsReportCode);
//                while (!postingsReportFile.getStatus().equals("success")) {
//                    Thread.sleep(2000);
//                    postingsReportFile = client.getPostingsReportInfoByCode(postingsReportCode);
//                }
//                System.out.println(postingsReportFile);
//            } catch (IOException | InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//
//        });
//        OzonClient client = clients.get("1140235");
////        OzonClient client = clients.get("453792");
//
//        Instant from = LocalDate.now().minusWeeks(3).atStartOfDay().toInstant(ZoneOffset.UTC);
//        Instant to = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
//
//        String postingsReportCode = client.createPostingsReportCode(from.toString(), to.toString(), List.of("fbo"));;
//        PostingsReportInfoResult postingsReportFile = client.getPostingsReportInfoByCode(postingsReportCode);
//        while (!postingsReportFile.getStatus().equals("success")) {
//            Thread.sleep(2000);
//            postingsReportFile = client.getPostingsReportInfoByCode(postingsReportCode);
//        }
//
//        System.out.println(postingsReportFile);

//
//        Map<String, List<String>> clientSkuMap = reportService.readClientIdsAndSkus();
//
//        List<StockDto> fboStocks = client.getFBOStocks(clientSkuMap.get(client.getClientId()));
//
//        System.out.println(fboStocks);
//
//        String postingsReportCode = client.createPostingsReportCode(from.toString(), to.toString(), List.of("fbo"));
//        System.out.println(postingsReportCode);

//        PostingsReportInfoResult postingsReportFile = client.getPostingsReportInfoByCode(postingsReportCode);
//        while (!postingsReportFile.getStatus().equals("success")) {
//            Thread.sleep(1000);
//            postingsReportFile = client.getPostingsReportInfoByCode(postingsReportCode);
//            System.out.println(postingsReportFile.getStatus());
//        String postingsReportFile = "https://ir.ozone.ru/s3/ord-report-service-2/seller_postings_v2/seller_postings_v2-seller-453792-time-1784407397.csv?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=57ZFADTV8-17683-3UD0%2F20260718%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20260718T204332Z&X-Amz-Expires=604800&X-Amz-SignedHeaders=host&X-Amz-Signature=ebb5c8ca961d021b916ac310919c3296b9912b2a041167b39d85d5ea895506a4";
//            System.out.println(postingsReportFile);

        //fill three weeks before sells


//        List<PostingDto> postingDtos = reportService.aggregatePostings(dtos);
//
//        System.out.println(postingDtos);
//
//        List<StockDto> stocks = client.getFBOStocks(List.of("258010079"));
//        List<StockDto> stocks = client.getFBOStocks(List.of("3369947679"));

//        System.out.println(stocks);

//
//        WarehouseDictionary warehouseDictionary = run.getBean("warehouseDictionary", WarehouseDictionary.class);
//        Warehouse warehouse = warehouseDictionary.getById(1020001649204000L);
//
//        long draftId = client.createSupplyCrossdockDraft(3369921328L, 1, warehouse);
//        System.out.println(draftId);
//
//        DraftCreateStatus draftCreateStatus = client.checkDraftCreateStatus(draftId);
//
//        System.out.println(draftCreateStatus);
//
//        SupplyTimeSlotInfoDto availableTimeSlotsInfo = client.getAvailableTimeSlotsInfo(LocalDate.now(), LocalDate.now().plusDays(1), draftId, SupplyType.CROSSDOCK, warehouse.getClusterId());
//
//        SupplyOrderMapper mapper = new SupplyOrderMapper();
//        TimeSlot timeSlot = mapper.mapToModel(availableTimeSlotsInfo.getWarehouseTimeslots().getTimeslotsByDays().get(0));
//
//        client.createSupply(draftId, warehouse, timeSlot.getIntervals().get(0), SupplyType.CROSSDOCK);

//        System.out.println(client.checkDraftCreateStatus(113709444));
//
//        long draftId = 113709444L;
//        SupplyTimeSlotInfoDto availableTimeSlotsInfo = client.getAvailableTimeSlotsInfo(LocalDate.now(), LocalDate.now().plusDays(1), draftId, SupplyType.CROSSDOCK, warehouse.getClusterId());
//
//        SupplyOrderMapper mapper = new SupplyOrderMapper();
//        TimeSlot timeSlot = mapper.mapToModel(availableTimeSlotsInfo.getWarehouseTimeslots().getTimeslotsByDays().get(0));
//
//        System.out.println(timeSlot.getIntervals().get(0));
//
//        client.createSupply(draftId, warehouse, timeSlot.getIntervals().get(0), SupplyType.CROSSDOCK);
    }

}
