package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.config.WarehouseDictionary;
import com.example.OzonHelper.domain.TimeSlot;
import com.example.OzonHelper.domain.Warehouse;
import com.example.OzonHelper.domain.mapper.SupplyOrderMapper;
import com.example.OzonHelper.dto.response.fbo.SupplyTimeSlotInfoDto;
import com.example.OzonHelper.dto.response.fbo.TimeSlotDto;
import com.example.OzonHelper.dto.response.fbo.WarehouseTimeSlotByDaysDto;
import com.example.OzonHelper.enums.DraftCreateStatus;
import com.example.OzonHelper.enums.SupplyType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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

//        long draftId = client.createSupplyCrossdockDraft(4496559952L, 1, warehouse);
//        System.out.println(draftId);


//        DraftCreateStatus draftCreateStatus = client.checkDraftCreateStatus(draftId);
        DraftCreateStatus draftCreateStatus = client.checkDraftCreateStatus(112889645);
        System.out.println(draftCreateStatus);
//        DraftCreateStatus draftCreateStatus = client.checkDraftCreateStatus(draftId);
//        System.out.println(draftCreateStatus);

        SupplyTimeSlotInfoDto availableTimeSlotsInfo = client.getAvailableTimeSlotsInfo(LocalDate.now(), LocalDate.now().plusDays(1), 112889645, SupplyType.CROSSDOCK, warehouse.getClusterId());

        List<WarehouseTimeSlotByDaysDto> timeslotsByDays = availableTimeSlotsInfo.getWarehouseTimeslots().getTimeslotsByDays();

        SupplyOrderMapper mapper = new SupplyOrderMapper();

        List<TimeSlot> timeSlots = new ArrayList<>();

        for (WarehouseTimeSlotByDaysDto dto : timeslotsByDays) {
            timeSlots.add(mapper.mapToModel(dto));
        }

        for (TimeSlot timeslot : timeSlots) {
            System.out.println(timeslot);
        }
    }

}
