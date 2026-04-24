package com.example.OzonHelper;

import com.example.OzonHelper.util.TableManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class OzonHelperApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

//        List<OzonClient> ozonClients = run.getBean("ozonClient", List.class);
//        OzonClient client = ozonClients.get(0);
//        System.out.println(client.getFBSPostingList(LocalDateTime.now().minusHours(7), LocalDateTime.now(), ""));

        TableManager tableManager = run.getBean("tableManager", TableManager.class);
//        tableManager.someMethod();
    }

}
