package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.dto.response.supply.*;
import com.example.OzonHelper.enums.SupplyStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class OzonHelperApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

        List<OzonClient> ozonClients = run.getBean("ozonClient", List.class);
        OzonClient client = ozonClients.get(0);
        List<String> supplyOrdersIds = client.getSupplyOrdersIds(SupplyStatus.READY_TO_SUPPLY);
        System.out.println(supplyOrdersIds);
    }
}
