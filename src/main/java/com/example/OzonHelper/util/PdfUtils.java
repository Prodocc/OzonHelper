package com.example.OzonHelper.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class PdfUtils {

    public Path savePdf(byte[] carriageActPdf, long carriageId) {
        try {
            Path path = Path.of(
                    "data",
                    "fbs",
                    "carriage-act_" + carriageId + ".pdf"
            );

            Files.createDirectories(path.getParent());
            Files.write(path, carriageActPdf);

            return path;
        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to save carriage act PDF. carriageId=" + carriageId,
                    e
            );
        }
    }

    public byte[] extractPage(byte[] pdfBytes) {
        try (
                PDDocument source = Loader.loadPDF(pdfBytes);
                PDDocument result = new PDDocument();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ) {
            result.importPage(source.getPage(2));
            result.save(outputStream);

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to extract PDF page");
        }
    }
}
