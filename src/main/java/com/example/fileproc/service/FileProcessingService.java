package com.example.fileproc.service;

import com.example.fileproc.db.InMemoryDatabase;
import com.example.fileproc.model.ProcessedFile;
import com.example.fileproc.exception.BadRequestException;
import com.example.fileproc.exception.FileProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class FileProcessingService {
    private static final Logger log = LoggerFactory.getLogger(FileProcessingService.class);

    private final InMemoryDatabase db;

    public FileProcessingService(InMemoryDatabase db) {
        this.db = db;
    }

    public ProcessedFile uploadAndProcess(MultipartFile file) {
        log.info("Upload received: name={}, size={}", file.getOriginalFilename(), file.getSize());

        if (file.isEmpty()) {
            throw new BadRequestException("Uploaded file is empty.");
        }
        String filename = sanitizeFilename(file.getOriginalFilename());
        if (!isAllowedFile(filename)) {
            throw new BadRequestException("File type not allowed. Allowed: .txt, .csv");
        }

        log.info("Processing started: {}", filename);
        try {
            Counts counts = processFile(file, filename);
            log.info("Processing finished: {}, lines={}, words={}", filename, counts.lines, counts.words);
            ProcessedFile processed = ProcessedFile.of(filename, counts.lines, counts.words);
            return db.saveToDatabase(processed);
        } catch (IOException e) {
            log.error("I/O error during processing: {}", e.getMessage());
            throw new FileProcessingException("Failed to process file due to I/O error.");
        } catch (RuntimeException e) {
            log.error("Unexpected error: {}", e.getMessage());
            throw new FileProcessingException("Failed to process file due to an unexpected error.");
        }
    }

    boolean isAllowedFile(String filename) {
        if (filename == null || filename.isBlank()) return false;
        String lower = filename.toLowerCase();
        return lower.endsWith(".txt") || lower.endsWith(".csv");
    }

    private String sanitizeFilename(String original) {
        if (original == null) return "unknown";
        // Remove any path components to avoid surprises in logs
        String name = original.replace("\\", "/");
        int idx = name.lastIndexOf('/');
        return (idx >= 0) ? name.substring(idx + 1) : name;
    }

    private Counts processFile(MultipartFile file, String filename) throws IOException {
        long lines = 0;
        long words = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines++;
                String trimmed = line.trim();
                if (!trimmed.isEmpty()) {
                    String[] tokens;
                    if (filename.toLowerCase().endsWith(".csv")) {
                        // Split on commas OR whitespace
                        tokens = trimmed.split("[,\\s]+");
                    } else {
                        // Default: split on whitespace only
                        tokens = trimmed.split("\\s+");
                    }
                    words += tokens.length;
                }
            }
        }
        return new Counts(lines, words);
    }

    private record Counts(long lines, long words) {}
}

