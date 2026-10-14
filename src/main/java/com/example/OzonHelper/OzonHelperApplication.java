package com.example.OzonHelper;

import com.example.OzonHelper.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.*;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

        OzonAgentService ozonAgentService = run.getBean("ozonAgentService", OzonAgentService.class);
        System.out.println(ozonAgentService.ask("Покажи магазины, где есть FBS-заказы"));

        System.exit(0);
    }

}
