package com.example.fileproc.model;

import java.time.Instant;
import java.util.UUID;

public class ProcessedFile {
    private final String id;
    private final String originalFilename;
    private final long lineCount;
    private final long wordCount;
    private final Instant processedAt;

    public ProcessedFile(String id, String originalFilename, long lineCount, long wordCount, Instant processedAt) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.lineCount = lineCount;
        this.wordCount = wordCount;
        this.processedAt = processedAt;
    }

    public static ProcessedFile of(String originalFilename, long lineCount, long wordCount) {
        return new ProcessedFile(UUID.randomUUID().toString(), originalFilename, lineCount, wordCount, Instant.now());
    }

    public String getId() { return id; }
    public String getOriginalFilename() { return originalFilename; }
    public long getLineCount() { return lineCount; }
    public long getWordCount() { return wordCount; }
    public Instant getProcessedAt() { return processedAt; }
}
