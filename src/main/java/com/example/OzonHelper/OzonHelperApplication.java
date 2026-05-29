package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.dto.response.supply.ClusterDto;
import com.example.OzonHelper.service.Scheduler;
import com.example.OzonHelper.service.WarehouseDictionary;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class OzonHelperApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

//        Scheduler scheduler = run.getBean("scheduler", Scheduler.class);
//        scheduler.fillFBSLogListMorning();

//        List<OzonClient> clients = run.getBean("ozonClient", List.class);
//        OzonClient client = clients.get(0);
////
//        for (ClusterDto clusterDto : client.getClusters()){
//            System.out.println(clusterDto);
//        }

        WarehouseDictionary warehouseDictionary = run.getBean("warehouseDictionary", WarehouseDictionary.class);
        System.out.println(warehouseDictionary.warehouses);

    }

}
