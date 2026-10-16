package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.dto.response.fbs.FBSWarehouseDto;
import com.example.OzonHelper.service.FbsService;
import com.example.OzonHelper.service.OzonAgentService;
import com.example.OzonHelper.service.ReturnService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

        FbsService fbsService = run.getBean("fbsService", FbsService.class);
//        System.out.println(fbsService.getShopNamesWithAwaitingDeliver());
//        System.out.println(fbsService.getShopNamesWithAwaitingPackaging());

        fbsService.createAndSendFbsShipmentActs();
//
//        ReturnService returnService = run.getBean("returnService", ReturnService.class);
//        System.out.println(returnService.getShopNamesWithReturns());

//        Map<String, OzonClient> ozonClients = run.getBean("ozonClients", Map.class);
//        OzonClient client = ozonClients.get("453792");

//        System.out.println(client.getFBSWarehouses());

//        System.out.println(client.canCreateCarriage());

        System.exit(0);
    }

}
