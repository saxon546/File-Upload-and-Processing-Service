package com.example.fileproc.db;

import com.example.fileproc.model.ProcessedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryDatabase {
    private static final Logger log = LoggerFactory.getLogger(InMemoryDatabase.class);

    private final Map<String, ProcessedFile> storage = new ConcurrentHashMap<>();

    public ProcessedFile saveToDatabase(ProcessedFile processedFile) {
        storage.put(processedFile.getId(), processedFile);
        log.debug("Saved processed file: id={}, name={}", processedFile.getId(), processedFile.getOriginalFilename());
        return processedFile;
    }

    public Optional<ProcessedFile> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Collection<ProcessedFile> findAll() {
        return Collections.unmodifiableCollection(storage.values());
    }
}

