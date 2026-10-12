package com.example.OzonHelper.service.supply;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrdersPage;
import com.example.OzonHelper.enums.ozon.SupplyState;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SupplyOrderLoader {

    public List<String> getAllSupplyOrderIds(OzonClient client, SupplyState state) throws IOException, InterruptedException {
        List<String> result = new ArrayList<>();
        SupplyOrdersPage page;
        String lastId = null;
        do {
            page = client.getSupplyOrdersIds(lastId, state);
            if (page == null) break;
            result.addAll(page.orderIds());
            lastId = page.nextCursor();
            Thread.sleep(1000);
        } while (page.orderIds().size() >= 100 && !lastId.isBlank());

        return result;
    }

    public List<SupplyOrderDto> getSupplyOrderDtos(OzonClient client, List<String> supplyOrderIds) throws IOException, InterruptedException {
        int orderIdsMaxLimit = 50;
        List<SupplyOrderDto> result = new ArrayList<>();
        for (int i = 0; i < supplyOrderIds.size(); i += orderIdsMaxLimit) {
            int to = Math.min(i + orderIdsMaxLimit, supplyOrderIds.size());
            result.addAll(client.getSupplyOrders(supplyOrderIds.subList(i, to)));
            Thread.sleep(1000);
        }
        return result;
    }
}
