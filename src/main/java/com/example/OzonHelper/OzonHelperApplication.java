package com.example.OzonHelper;

import com.example.OzonHelper.client.GoogleClient;
import com.example.OzonHelper.service.FbsLogService;
import com.example.OzonHelper.service.Scheduler;
import com.example.OzonHelper.util.FbsLogDataBuilder;
import com.example.OzonHelper.util.GoogleUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class OzonHelperApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        ConfigurableApplicationContext run = SpringApplication.run(OzonHelperApplication.class, args);

//        List<OzonClient> ozonClients = run.getBean("ozonClient", List.class);
//        OzonClient client = ozonClients.get(0);
//        System.out.println(client.getFBSPostingList(LocalDateTime.now().minusHours(7), LocalDateTime.now(), ""));

//        FbsLogService fbsLogService = run.getBean("fbsLogService", FbsLogService.class);
//        fbsLogService.syncLogList();

//        GoogleClient googleClient = run.getBean("googleClient", GoogleClient.class);
//
//        FbsLogDataBuilder fbsLogDataBuilder = new FbsLogDataBuilder();
//        List<List<Object>> rawData = fbsLogDataBuilder.createFbsPostingData();
//        String spreadSheetId = "1eI8apB8e7PCzJAQ8HRcJrUWX1Zfffk7UwzxoIbSjDp4";
//        String range = GoogleUtils.buildRange("Май 2026", "B", "G", 64);
//
//        googleClient.writeTable(rawData, spreadSheetId, range);
    }

}
