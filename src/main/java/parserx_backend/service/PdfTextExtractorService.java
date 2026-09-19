package parserx_backend.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

@Service
public class PdfTextExtractorService {

    public String extractText(String filePath) throws IOException {

        Path path = Paths.get(filePath);

        if (!Files.exists(path)) {
            throw new RuntimeException("Resume file not found");
        }

        try (PDDocument document = Loader.loadPDF(new File(filePath))) {

            PDFTextStripper stripper = new PDFTextStripper();

            return stripper.getText(document);
        }
    }
}