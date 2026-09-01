package com.example.OzonHelper.domain.mapper;

import com.example.OzonHelper.domain.Product;
import com.example.OzonHelper.domain.Return;
import com.example.OzonHelper.domain.Warehouse;
import com.example.OzonHelper.dto.response.returns.ReturnDto;
import com.example.OzonHelper.dto.response.returns.ReturnProductInfoDto;
import com.example.OzonHelper.dto.response.returns.ReturnWarehouseDto;
import org.springframework.stereotype.Component;

@Component
public class ReturnMapper {

    public Return mapToModel(ReturnDto dto) {
        Return model = new Return();

        model.setId(dto.getId());
        model.setPostingNumber(dto.getPostingNumber());
        model.setLabelBarcode(dto.getLogisticInfo().getLabelBarcode());
        model.setReason(dto.getReason());

        ReturnProductInfoDto productInfoDto = dto.getProductInfo();
        Product product = new Product(productInfoDto.getArticle(), productInfoDto.getName(), productInfoDto.getQuantity());
        model.setProduct(product);

        ReturnWarehouseDto warehouseDto = dto.getWarehouse();
        Warehouse warehouse = new Warehouse(
                warehouseDto.getId(),
                warehouseDto.getName(),
                warehouseDto.getAddress(),
                0L
        );

        model.setWarehouse(warehouse);

        return model;
    }
}
