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
    private static final int HOURS_SINCE_FRIDAY = 66;
    private static final int HOURS_OVERNIGHT = 18;
    private static final int HOURS_SINCE_MORNING = 5;
    private static final int HOURS_LAST_STEP = 1;

    private final List<OzonClient> clients;
    private final FbsLogService fbsLogService;

    @Scheduled(cron = "0 0 9 * * 1-5")
    public void fillFBSLogListMorning() throws IOException, InterruptedException {
        LocalDateTime since = LocalDateTime.now();
        if (since.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            since = since.minusHours(HOURS_SINCE_FRIDAY);
        } else {
            since = since.minusHours(HOURS_OVERNIGHT);
        }
        if (processFBSLog(since)) {
            //write to log list
            fbsLogService.syncLogList();
        }
    }

    @Scheduled(cron = "0 0 14 * * 0-5")
    public void fillFBSLogListMidday() throws IOException, InterruptedException {
        if (processFBSLog(LocalDateTime.now().minusHours(HOURS_SINCE_MORNING))) {
            //check log list and write
        }
    }

    @Scheduled(cron = "0 0 15 * * 0-5")
    public void fillFBSLogListAfternoon() throws IOException, InterruptedException {
        if (processFBSLog(LocalDateTime.now().minusHours(HOURS_LAST_STEP))) {
            //check log list and write
        }
    }

    @Scheduled(cron = "0 0 0 * * 5L")
    public void createNewLogListSheet() {
        //check for existing log list for next month, if no then
        //create new log list sheet and write dates for all working days
    }

    private boolean processFBSLog(LocalDateTime since) throws IOException, InterruptedException {
        return checkFBSPostings(since, LocalDateTime.now());
    }

    private boolean checkFBSPostings(LocalDateTime since, LocalDateTime to) throws IOException, InterruptedException {
        for (OzonClient client : clients) {
            if (client.getFbsPostingList(since, to, PostingStatus.AWAITING_PACKAGING.getValue()).size() > 1)
                return true;
            if (client.getFbsPostingList(since, to, PostingStatus.AWAITING_DELIVER.getValue()).size() > 1) return true;
        }
        return false;
    }
}
