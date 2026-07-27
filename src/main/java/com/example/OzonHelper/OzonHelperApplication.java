package com.example.OzonHelper;

import com.example.OzonHelper.service.ReportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class OzonHelperApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

//        ReportService reportService = run.getBean("reportService", ReportService.class);
//        reportService.updateDailyReport(false);
    }

}
