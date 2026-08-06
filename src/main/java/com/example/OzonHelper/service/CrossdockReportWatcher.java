package com.example.OzonHelper.service;

import com.example.OzonHelper.config.CrossdockReportProperties;
import com.example.OzonHelper.config.OzonStoreConfig;
import com.example.OzonHelper.config.StoreProperties;
import com.example.OzonHelper.parser.ReportExcelParser;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CrossdockReportWatcher {
    private final Path incomingRoot;
    private final Path archiveRoot;
    private final Path errorRoot;

    private final ReportService reportService;
    private final StoreProperties storeProperties;

    public CrossdockReportWatcher(CrossdockReportProperties properties, ReportService reportService, StoreProperties storeProperties) {
        Path root = properties.root();

        this.incomingRoot = root.resolve("incoming");
        this.archiveRoot = root.resolve("archive");
        this.errorRoot = root.resolve("error");

        this.reportService = reportService;
        this.storeProperties = storeProperties;
    }

    public void watch() throws IOException, InterruptedException {
        Map<WatchKey, StoreWatchContext> storesByWatchKey = new HashMap<>();

        WatchService watchService =
                FileSystems.getDefault().newWatchService();

        for (OzonStoreConfig store : storeProperties.getOzon()) {
            Path incomingDir = incomingRoot.resolve(store.getName());

            Files.createDirectories(incomingDir);

            WatchKey key = incomingDir.register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE
            );

            storesByWatchKey.put(
                    key,
                    new StoreWatchContext(
                            store.getClientId(),
                            store.getName(),
                            incomingDir
                    )
            );
        }

        while (true) {
            WatchKey key = watchService.take();

            StoreWatchContext context = storesByWatchKey.get(key);

            if (context == null) {
                key.reset();
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                Path fileName = (Path) event.context();
                Path fullPath = context.incomingDir.resolve(fileName);

                Thread.sleep(1000);
                reportService.processCrossdockReport(context.clientId, fullPath);
            }

            key.reset();
        }

    }

    private record StoreWatchContext(
            String clientId,
            String shopName,
            Path incomingDir) {
    }
}
