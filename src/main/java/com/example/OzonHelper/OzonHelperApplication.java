package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.domain.SupplyOrder;
import com.example.OzonHelper.domain.SupplyOrderComposition;
import com.example.OzonHelper.domain.mapper.SupplyOrderCompositionMapper;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderCompositionDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrdersPage;
import com.example.OzonHelper.enums.SupplyState;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

//        ReportService reportService = run.getBean("reportService", ReportService.class);
//        reportService.updateDailyReport(false);

        Map<String, OzonClient> ozonClients = run.getBean("ozonClients", Map.class);
        OzonClient client = ozonClients.get("1140235");

        List<String> supplyOrderIds = new ArrayList<>();
        SupplyOrdersPage page;
        String lastId = null;
        do {
            page = client.getSupplyOrdersIds(lastId, SupplyState.COMPLETED);
            supplyOrderIds.addAll(page.orderIds());
            lastId = page.nextCursor();
            System.out.println("lastId = " + lastId);
            System.out.println("page.orderIds().size() = " + page.orderIds().size());
            Thread.sleep(1000);
        } while (page.orderIds().size() >= 100);

        System.out.println(supplyOrderIds.size());

        int orderIdsMaxLimit = 50;
        List<SupplyOrderDto> supplyOrderDtos
                = new ArrayList<>();
        for (int i = 0; i < supplyOrderIds.size(); i += orderIdsMaxLimit) {
            int to = Math.min(i + orderIdsMaxLimit, supplyOrderIds.size());
            System.out.println("i = " + i);
            System.out.println("to = " + to);
            supplyOrderDtos
                    .addAll(client.getSupplyOrders(supplyOrderIds.subList(i, to)));
            Thread.sleep(1000);
        }

        System.out.println("supplyOrderDtos" +
                ".size() = " + supplyOrderDtos
                .size());

        LocalDateTime periodStart = LocalDate.now().minusMonths(1).atStartOfDay();
        LocalDateTime periodEnd = LocalDate.now().atStartOfDay();

        List<SupplyOrderDto> filteredSupplyOrders = supplyOrderDtos

                .stream()
                .filter(supplyOrderDto -> {
                    LocalDateTime statusUpdateDate = supplyOrderDto.getSupplyStateUpdatedDate();
                    return statusUpdateDate.isAfter(periodStart) && statusUpdateDate.isBefore(periodEnd);
                }).toList();

        System.out.println(filteredSupplyOrders.size());

        String orderNumber = filteredSupplyOrders.get(0).getOrderNumber();
        System.out.println("orderNumber = " + orderNumber);

        Map<String, SupplyOrder> byOrderNumber = new HashMap<>();
        Map<String, SupplyOrder> byBundleId = new HashMap<>();
        for (SupplyOrderDto dto : filteredSupplyOrders) {
            SupplyOrder supplyOrder = new SupplyOrder();
            supplyOrder.setCreatedDate(dto.getCreationDate());
            supplyOrder.setOrderId(dto.getOrderId());
            supplyOrder.setOrderNumber(dto.getOrderNumber());
            supplyOrder.setState(dto.getSupplyState());
            supplyOrder.setBundle_id(dto.getSupplies().get(0).getBundleId());
            byOrderNumber.put(dto.getOrderNumber(), supplyOrder);
            byBundleId.put(supplyOrder.getBundle_id(), supplyOrder);
        }

        SupplyOrderCompositionMapper compositionMapper = new SupplyOrderCompositionMapper();

        for (String bundleId : byBundleId.keySet()) {
            SupplyOrderCompositionDto compositionDto = client.getSupplyOrdersComposition(List.of(bundleId));
            byBundleId.get(bundleId).setComposition(compositionMapper.mapToModel(compositionDto));
            Thread.sleep(300);
        }

        System.out.println(byOrderNumber.get(orderNumber));

        System.exit(0);
    }

}
