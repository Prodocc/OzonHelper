package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.dto.response.product.ProductDto;
import com.example.OzonHelper.service.ReportService;
import com.example.OzonHelper.service.questions.QuestionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.function.Consumer;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);
//
        Map<String, OzonClient> ozonClients = run.getBean("ozonClients", Map.class);
//        ozonClients.values().forEach(client -> {
//            System.out.println("client.getShopName() = " + client.getShopName());
//            try {
//                System.out.println(client.getSubscriptionInfo().getType());
//            } catch (IOException | InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        });


//        QuestionService questionService = run.getBean("questionService", QuestionService.class);
//        questionService.syncQuestions();

//        CrossdockReportWatcher crossdockReportWatcher = run.getBean("crossdockReportWatcher", CrossdockReportWatcher.class);
//        crossdockReportWatcher.watch();

        ReportService reportService = run.getBean("reportService", ReportService.class);
        reportService.updateDailyReport(true);
//        reportService.processCrossdockReport("123", Path.of("1"));

        System.exit(0);
    }

}
