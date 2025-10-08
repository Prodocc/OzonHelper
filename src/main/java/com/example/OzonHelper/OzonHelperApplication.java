package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.dto.response.supply.*;
import com.example.OzonHelper.enums.SupplyStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SpringBootApplication
public class OzonHelperApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

        OzonClient ozonClient = run.getBean("ozonClient", OzonClient.class);
        GetSupplyOrdersResponse supplyOrders = ozonClient.getSupplyOrders(
                List.of(
                        SupplyStatus.ORDER_STATE_READY_TO_SUPPLY
                ),
                100
        );
        System.out.println(supplyOrders.getSupplyOrderId());
        GetSupplyOrderInfoResponse supplyOrdersInfo = ozonClient.getSupplyOrdersInfo(supplyOrders.getSupplyOrderId());
        List<SupplyOrderInfo> filteredOrders = supplyOrdersInfo.getOrders()
                .stream()
                .filter(supplyOrderInfo -> !supplyOrderInfo.isSupplyIsSuper())
                .peek(System.out::println)
                .toList();
        List<SupplyOrderInfo> list = filteredOrders
                .stream()
                .filter(supplyOrderInfo 
                        -> supplyOrderInfo.getSupplies().get(0)
                        .getBundleId().equalsIgnoreCase("019970f3-fab8-74d4-a319-cd1a79af34e7"))
                .toList();
        System.out.println(list);
        GetSupplyOrdersCompositionResponse supplyOrdersComposition = ozonClient.getSupplyOrdersComposition(list.get(0).getSupplies(), 100);
        System.out.println(supplyOrdersComposition);
    }
}
