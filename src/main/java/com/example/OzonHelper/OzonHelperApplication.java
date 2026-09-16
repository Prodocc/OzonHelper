package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.service.CrossdockReportWatcher;
import com.example.OzonHelper.service.ReportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.Path;
import java.util.*;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

//        Map<String, OzonClient> ozonClients = run.getBean("ozonClients", Map.class);
//        OzonClient client = ozonClients.get("1140235");
//
//        List<ReturnDto> returnByBarcode = client.getReturnByBarcode( "ii18516982627");
//        System.out.println(returnByBarcode);

//
//        ReturnService returnService = run.getBean("returnService", ReturnService.class);
//        Path path = Path.of("D:\\returns\\shopname\\029199977 от 24.08.2026.pdf");
//        List<ReturnEntityDto> returnActEntities = returnService.getReturnActEntities(path);
//
//        returnActEntities.forEach(System.out::println);

//        returnService.sendReturnBarcodeNotifications("414671305");
//
//        QuestionService questionService = run.getBean("questionService", QuestionService.class);
//        questionService.syncQuestions();
//
//        CrossdockReportWatcher crossdockReportWatcher = run.getBean("crossdockReportWatcher", CrossdockReportWatcher.class);
//        crossdockReportWatcher.watch();
//
        ReportService reportService = run.getBean("reportService", ReportService.class);
        Path path = Path.of("D:\\cost_price\\Себестоимость_товаров.xlsx");
        reportService.processStocksReport(path);
//        reportService.updateDailyReport(false);
//        reportService.processCrossdockReport("123", Path.of("1"));

        System.exit(0);
    }

}
