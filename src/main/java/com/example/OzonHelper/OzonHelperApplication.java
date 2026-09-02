package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.service.ReturnService;
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

        Map<String, OzonClient> ozonClients = run.getBean("ozonClients", Map.class);

        ReturnService returnService = run.getBean("returnService", ReturnService.class);
        returnService.sendReturnBarcodeNotifications("414671305");
//
//        QuestionService questionService = run.getBean("questionService", QuestionService.class);
//        questionService.syncQuestions();
//
//        CrossdockReportWatcher crossdockReportWatcher = run.getBean("crossdockReportWatcher", CrossdockReportWatcher.class);
//        crossdockReportWatcher.watch();

//        ReportService reportService = run.getBean("reportService", ReportService.class);
//        reportService.updateDailyReport(false);
//        reportService.processCrossdockReport("123", Path.of("1"));

        System.exit(0);
    }

}
