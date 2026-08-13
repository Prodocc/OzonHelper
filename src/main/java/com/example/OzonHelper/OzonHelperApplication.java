package com.example.OzonHelper;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.domain.PostingAccrual;
import com.example.OzonHelper.domain.SupplyOrderComposition;
import com.example.OzonHelper.domain.mapper.PostingAccrualMapper;
import com.example.OzonHelper.domain.mapper.SupplyOrderCompositionMapper;
import com.example.OzonHelper.dto.report.ozon.PostingAccrualDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderCompositionDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrderDto;
import com.example.OzonHelper.dto.response.fbo.SupplyOrdersPage;
import com.example.OzonHelper.dto.response.report.AccrualDto;
import com.example.OzonHelper.enums.AccrualType;
import com.example.OzonHelper.enums.SupplyState;
import com.example.OzonHelper.parser.ReportCSVParser;
import com.example.OzonHelper.parser.ReportExcelParser;
import com.example.OzonHelper.service.CrossdockReportWatcher;
import com.example.OzonHelper.service.ReportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.FileWriter;
import java.math.BigDecimal;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@SpringBootApplication
@ConfigurationPropertiesScan
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);
//
//        Map<String, OzonClient> ozonClients = run.getBean("ozonClients", Map.class);
//        OzonClient client = ozonClients.get("1140235");

        CrossdockReportWatcher crossdockReportWatcher = run.getBean("crossdockReportWatcher", CrossdockReportWatcher.class);
        crossdockReportWatcher.watch();

//        ReportService reportService = run.getBean("reportService", ReportService.class);
//        reportService.updateDailyReport(false);
//        reportService.processCrossdockReport("123", Path.of("1"));

        System.exit(0);
    }

}
