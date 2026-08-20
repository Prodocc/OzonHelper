package com.example.OzonHelper.service.report.crossdock;

import com.example.OzonHelper.domain.PostingAccrual;
import com.example.OzonHelper.domain.Supply;
import com.example.OzonHelper.domain.SupplyOrderComposition;
import com.example.OzonHelper.dto.response.fbo.SupplyInfoDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderDto;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrossDockSupplyBuilderTest {

    @Test
    public void test(){
        PostingAccrual postingAccrual = new PostingAccrual();
        Supply supply = new Supply();
        supply.setComposition(new SupplyOrderComposition());
        supply.setClusterName("Moscow");
        supply.setSupplyId("101");
        postingAccrual.setSupply(supply);
    }

    public Map<String, Supply> buildSuppliesByBundleId(Map<String, PostingAccrual> accrualsBySupplyId, List<SupplyOrderDto> supplyOrderDtos,
                                                       Map<Long, String> clustersById) {
        Map<String, Supply> result = new HashMap<>();
        for (SupplyOrderDto dto : supplyOrderDtos) {
            for (SupplyInfoDto infoDto : dto.getSupplies()) {
                String supplyId = infoDto.getSupplyId();
                if (!accrualsBySupplyId.containsKey(supplyId)) {
                    continue;
                }
                Supply supply = new Supply();
                supply.setCreatedDate(dto.getCreationDate());
                supply.setOrderId(dto.getOrderId());
                supply.setOrderNumber(dto.getOrderNumber());
                supply.setState(infoDto.getSupplyState());
                supply.setBundleId(infoDto.getBundleId());
                supply.setSupplyId(supplyId);
                supply.setClusterName(clustersById.get(infoDto.getClusterId()));

                result.put(supply.getBundleId(), supply);

                accrualsBySupplyId.get(supplyId).setSupply(supply);
            }
        }

        return result;
    }
}
