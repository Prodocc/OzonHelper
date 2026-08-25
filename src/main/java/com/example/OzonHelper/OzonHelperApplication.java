package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.dto.response.finance.FinancialReportDto;
import com.example.OzonHelper.dto.response.product.ProductDto;
import com.example.OzonHelper.service.ReportService;
import com.example.OzonHelper.service.questions.QuestionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Consumer;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);
//
        Map<String, OzonClient> ozonClients = run.getBean("ozonClients", Map.class);
        OzonClient client = ozonClients.get("2837869");

        Instant from = LocalDate.of(2026, 7, 1).atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant to = LocalDate.of(2026, 7, 5).atStartOfDay().toInstant(ZoneOffset.UTC);

        FinancialReportDto financialReport = client.getFinancialReport(from.toString(), to.toString(), 1);
        System.out.println(financialReport);
        System.out.println(financialReport.getDetails().get(0).getPeriod());
        System.out.println(financialReport.getDetails().get(0).getInvoice());
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

//        ReportService reportService = run.getBean("reportService", ReportService.class);
//        reportService.updateDailyReport(true);
//        reportService.processCrossdockReport("123", Path.of("1"));

        System.exit(0);
    }

}
