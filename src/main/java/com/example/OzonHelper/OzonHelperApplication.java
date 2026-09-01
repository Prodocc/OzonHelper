package com.example.OzonHelper;

import com.example.OzonHelper.client.MaxClient;
import com.example.OzonHelper.dto.response.max.UploadImageResponse;
import com.example.OzonHelper.enums.max.ButtonType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.file.*;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

        MaxClient client = run.getBean("maxClient", MaxClient.class);
        client.addButton("414671305", "Получить возвраты", ButtonType.CALLBACK, "return_get");
//        String chatId = "414671305";
//        String text = "text";
//        Path path = Path.of("data", "returns", "return-barcode_puresin_ecolife.png");
//        client.sendImage(chatId, text, path);
//        System.out.println(client.getBotInfo());

//        Map<String, OzonClient> ozonClients = run.getBean("ozonClients", Map.class);
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
//        reportService.updateDailyReport(false);
//        reportService.processCrossdockReport("123", Path.of("1"));

        System.exit(0);
    }
}
