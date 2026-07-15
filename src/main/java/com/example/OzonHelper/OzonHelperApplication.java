package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.WarehouseDictionary;
import com.example.OzonHelper.domain.TimeSlot;
import com.example.OzonHelper.domain.Warehouse;
import com.example.OzonHelper.domain.mapper.SupplyOrderMapper;
import com.example.OzonHelper.dto.response.fbo.SupplyTimeSlotInfoDto;
import com.example.OzonHelper.enums.DraftCreateStatus;
import com.example.OzonHelper.enums.SupplyType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class OzonHelperApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

//        Scheduler scheduler = run.getBean("scheduler", Scheduler.class);
//        scheduler.fillFBSLogListMorning();

        List<OzonClient> clients = run.getBean("ozonClient", List.class);
        OzonClient client = clients.get(0);

        WarehouseDictionary warehouseDictionary = run.getBean("warehouseDictionary", WarehouseDictionary.class);
        Warehouse warehouse = warehouseDictionary.getById(1020001649204000L);

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

        System.out.println(client.checkDraftCreateStatus(113709444));

        long draftId = 113709444L;
        SupplyTimeSlotInfoDto availableTimeSlotsInfo = client.getAvailableTimeSlotsInfo(LocalDate.now(), LocalDate.now().plusDays(1), draftId, SupplyType.CROSSDOCK, warehouse.getClusterId());

        SupplyOrderMapper mapper = new SupplyOrderMapper();
        TimeSlot timeSlot = mapper.mapToModel(availableTimeSlotsInfo.getWarehouseTimeslots().getTimeslotsByDays().get(0));

        System.out.println(timeSlot.getIntervals().get(0));

        client.createSupply(draftId, warehouse, timeSlot.getIntervals().get(0), SupplyType.CROSSDOCK);
    }

}
