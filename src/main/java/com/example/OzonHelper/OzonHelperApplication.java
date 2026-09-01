package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.domain.Return;
import com.example.OzonHelper.domain.mapper.ReturnMapper;
import com.example.OzonHelper.dto.response.fbo.ClusterDto;
import com.example.OzonHelper.dto.response.fbo.WarehouseDto;
import com.example.OzonHelper.dto.response.fbs.FBSWarehouseDto;
import com.example.OzonHelper.dto.response.returns.ReturnDto;
import com.example.OzonHelper.enums.ClusterType;
import com.example.OzonHelper.enums.ReturnVisualStatus;
import com.example.OzonHelper.enums.WarehouseType;
import com.example.OzonHelper.service.CrossdockReportWatcher;
import com.example.OzonHelper.service.ReportService;
import com.example.OzonHelper.service.ReturnService;
import com.example.OzonHelper.service.questions.QuestionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

        Map<String, OzonClient> ozonClients = run.getBean("ozonClients", Map.class);

        ReturnService returnService = run.getBean("returnService", ReturnService.class);
        returnService.sendReturnBarcodeNotifications();

//        String png = client.getReturnBarcodePng();
//
//        byte[] decode = Base64.getDecoder().decode(png);
//        Path path = Path.of("data", "returns", "return-barcode_" + client.getShopName() + ".png");
//        Files.write(path, decode);
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
