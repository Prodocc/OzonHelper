package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.domain.PostingAccrual;
import com.example.OzonHelper.domain.SupplyOrder;
import com.example.OzonHelper.domain.SupplyOrderComposition;
import com.example.OzonHelper.domain.mapper.PostingAccrualMapper;
import com.example.OzonHelper.domain.mapper.SupplyOrderCompositionMapper;
import com.example.OzonHelper.dto.report.ozon.PostingAccrualDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderCompositionDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderInfoDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrdersPage;
import com.example.OzonHelper.dto.response.report.AccrualDto;
import com.example.OzonHelper.enums.AccrualType;
import com.example.OzonHelper.enums.SupplyState;
import com.example.OzonHelper.parser.ReportCSVParser;
import com.example.OzonHelper.parser.ReportExcelParser;
import com.example.OzonHelper.service.ReportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SpringBootApplication
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

//        ReportService reportService = run.getBean("reportService", ReportService.class);
//        reportService.updateDailyReport(false);
//
//        Path incomingDir = Path.of(
//                "D:\\reports\\crossdock\\incoming\\shop_name"
//        );
        Path incomingDir = Path.of(
                "D:\\reports\\crossdock\\incoming\\shop_name"
        );

        List<PostingAccrualDto> postingAccruals = new ArrayList<>();

        WatchService watchService = FileSystems.getDefault().newWatchService();

        incomingDir.register(
                watchService,
                StandardWatchEventKinds.ENTRY_CREATE
        );

        WatchKey key = watchService.take();

        for (WatchEvent<?> event : key.pollEvents()) {
            Path fileName = (Path) event.context();

            Path fullPath = incomingDir.resolve(fileName);

            System.out.println("fullPath = " + fullPath);


            //change to - check for file size doesn't change anymore
            Thread.sleep(1000);

            ReportExcelParser parser = new ReportExcelParser();
            List<List<String>> lists = parser.readCSV(fullPath);
//            lists.forEach(System.out::println);

            for (List<String> list : lists) {
                PostingAccrualDto dto = new PostingAccrualDto();
                dto.setSupplyId(list.get(0));
                dto.setSum(list.get(15));
                dto.setType(AccrualType.fromDescription(list.get(3)));
                dto.setAmount(Integer.parseInt(list.get(7)));
                postingAccruals.add(dto);
            }
        }

        key.reset();

        PostingAccrualMapper mapper = new PostingAccrualMapper();

        List<PostingAccrual> accruals = new ArrayList<>();
        for (PostingAccrualDto dto : postingAccruals) {
            accruals.add(mapper.mapToModel(dto));
        }

        List<PostingAccrual> crossDockAccruals = accruals
                .stream()
                .filter(postingAccrual -> postingAccrual.getType() == AccrualType.CROSS_DOCK)
                .peek(System.out::println)
                .toList();

        Map<String, PostingAccrual> supplyIdToSum = new HashMap<>();
        for (PostingAccrual current : crossDockAccruals) {
            supplyIdToSum.compute(current.getSupplyId(),
                    (supplyId, accumulated) -> {
                        if (accumulated == null) {
                            PostingAccrual aggregate = new PostingAccrual();
                            aggregate.setSupplyId(supplyId);
                            aggregate.setSum(current.getSum());
                            return aggregate;
                        }

                        accumulated.setSum(
                                accumulated.getSum().add(current.getSum())
                        );

                        return accumulated;
                    }
            );
        }

        supplyIdToSum.forEach((s, postingAccrual) -> System.out.println(postingAccrual));


//        Map<String, OzonClient> clients = run.getBean("ozonClients", Map.class);
//        OzonClient client = clients.get("2837869");
//        OzonClient client = clients.get("4225962");
//

//        List<String> supplyOrderIds = new ArrayList<>();
//        SupplyOrdersPage page;
//        String lastId = null;
//        do {
//            page = client.getSupplyOrdersIds(lastId, SupplyState.COMPLETED);
//            supplyOrderIds.addAll(page.orderIds());
//            lastId = page.nextCursor();
//            System.out.println("lastId = " + lastId);
//            System.out.println("page.orderIds().size() = " + page.orderIds().size());
//            Thread.sleep(1000);
//        } while (page.orderIds().size() >= 100);
//
//        System.out.println(supplyOrderIds.size());
//
//        int orderIdsMaxLimit = 50;
//        List<SupplyOrderDto> supplyOrderDtos
//                = new ArrayList<>();
//        for (int i = 0; i < supplyOrderIds.size(); i += orderIdsMaxLimit) {
//            int to = Math.min(i + orderIdsMaxLimit, supplyOrderIds.size());
//            System.out.println("i = " + i);
//            System.out.println("to = " + to);
//            supplyOrderDtos
//                    .addAll(client.getSupplyOrders(supplyOrderIds.subList(i, to)));
//            Thread.sleep(1000);
//        }
//
//        System.out.println("supplyOrderDtos" +
//                ".size() = " + supplyOrderDtos
//                .size());
//
//        for (SupplyOrderDto dto : supplyOrderDtos) {
//            System.out.println("dto.getOrderNumber() = " + dto.getOrderNumber());
//            System.out.println("dto.getSupplies().size() = " + dto.getSupplies().size());
//            System.out.println("dto.getSupplies().get(0).getSupplyId() = " + dto.getSupplies().get(0).getSupplyId());
//        }
//
//        LocalDateTime periodStart = LocalDate.now().minusMonths(1).minusDays(5).atStartOfDay();
//        LocalDateTime periodEnd = LocalDate.now().atStartOfDay();


//        List<SupplyOrderDto> filteredSupplyOrders = supplyOrderDtos
//                .stream()
//                .filter(supplyOrderDto -> {
//                    LocalDateTime statusUpdateDate = supplyOrderDto.getSupplyStateUpdatedDate();
//                    return statusUpdateDate.isAfter(periodStart) && statusUpdateDate.isBefore(periodEnd);
//                }).toList();
//
//         USE THIS AS FILTER
//        List<Object> accural = new ArrayList();
//        HashSet<Object> allowedSupplies = new HashSet<>(accural);

//        List<SupplyOrderDto> filteredSupplyOrders = supplyOrderDtos
//                .stream()
//                .filter(supplyOrderDto -> allowedSupplies.contains(supplyOrderDto.getOrderNumber())).toList();


//        System.out.println(filteredSupplyOrders.size());


//        long supplyId = filteredSupplyOrders
//                .stream()
//                .filter(supplyOrderDto -> supplyOrderDto.getSupplies().size() > 1)
//                .toList()
//                .stream()
//                .peek(supplyOrderDto -> System.out.println(supplyOrderDto.getOrderNumber()))
//                .toList().get(0).getSupplies().get(0).getSupplyId();
//        System.out.println("supplyId = " + supplyId);

//        Map<Long, SupplyOrder> bySupplyId = new HashMap<>();
//        Map<String, SupplyOrder> byBundleId = new HashMap<>();
//        for (SupplyOrderDto dto : filteredSupplyOrders) {
//            SupplyOrder supplyOrder;
//            for (SupplyOrderInfoDto infoDto : dto.getSupplies()) {
//                supplyOrder = new SupplyOrder();
//                supplyOrder.setCreatedDate(dto.getCreationDate());
//                supplyOrder.setOrderId(dto.getOrderId());
//                supplyOrder.setOrderNumber(dto.getOrderNumber());
//                supplyOrder.setState(dto.getSupplyState());
//                supplyOrder.setBundle_id(infoDto.getBundleId());
//                supplyOrder.setSupplyId(infoDto.getSupplyId());
//                bySupplyId.put(supplyOrder.getSupplyId(), supplyOrder);
//                byBundleId.put(supplyOrder.getBundle_id(), supplyOrder);
//            }
//        }
//
//        SupplyOrderCompositionMapper compositionMapper = new SupplyOrderCompositionMapper();
//
//        for (String bundleId : byBundleId.keySet()) {
//            SupplyOrderCompositionDto compositionDto = client.getSupplyOrdersComposition(List.of(bundleId));
//            byBundleId.get(bundleId).setComposition(compositionMapper.mapToModel(compositionDto));
//            Thread.sleep(300);
//        }
//
//        for (SupplyOrder supplyOrder : bySupplyId.values()) {
//            System.out.println(supplyOrder);
//        }
//        System.out.println(bySupplyId.get(supplyId));

        System.exit(0);
    }

}
