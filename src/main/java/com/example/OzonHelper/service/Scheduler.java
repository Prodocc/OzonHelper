package com.example.OzonHelper.service;

import com.example.OzonHelper.client.OzonClient;
import com.example.OzonHelper.enums.PostingStatus;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class Scheduler {

    private final List<OzonClient> clients;
    private final FbsLogService fbsLogService;
    private final ReportService reportService;

//    @Scheduled(cron = "0 0 9 * * 1-5")
//    public void fillFBSLogListMorning() throws IOException, InterruptedException {
//        LocalDateTime since = LocalDateTime.now();
//        if (since.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
//            since = since.minusDays(3).withHour(15).withMinute(0).withSecond(0).withNano(0);
//        } else {
//            since = since.minusDays(1).withHour(15).withMinute(0).withSecond(0).withNano(0);
//        }
//        if (processFBSLog(since)) {
//            fbsLogService.syncLogList();
//        }
//    }

    @Scheduled(cron = "0 0 9 * * 2-7", zone = "Europe/Moscow")
    public void reportSheetUpdateDaily() {
        try {
            reportService.updateDailyReport(false);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 9 * * 1", zone = "Europe/Moscow")
    public void reportSheetUpdateWeekly() {
        try {
            reportService.updateDailyReport(true);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


//    @Scheduled(cron = "0 0 14 * * 0-5")
//    public void fillFBSLogListMidday() throws IOException, InterruptedException {
//        if (processFBSLog(LocalDateTime.now().minusHours(5).withMinute(0).withSecond(0).withNano(0))) {
//            fbsLogService.syncLogList();
//        }
//    }
//
//    @Scheduled(cron = "0 0 15 * * 0-5")
//    public void fillFBSLogListAfternoon() throws IOException, InterruptedException {
//        if (processFBSLog(LocalDateTime.now().minusHours(1).withMinute(0).withSecond(0).withNano(0))) {
//            fbsLogService.syncLogList();
//        }
//    }
//
//    private boolean processFBSLog(LocalDateTime since) throws IOException, InterruptedException {
//        return checkFBSPostings(since, LocalDateTime.now());
//    }
//
//    private boolean checkFBSPostings(LocalDateTime since, LocalDateTime to) throws IOException, InterruptedException {
//        for (OzonClient client : clients) {
//            if (!client.getFbsPostingList(since, to, PostingStatus.AWAITING_PACKAGING.getValue()).isEmpty())
//                return true;
//            if (!client.getFbsPostingList(since, to, PostingStatus.AWAITING_DELIVER.getValue()).isEmpty()) return true;
//        }
//        return false;
//    }
}
