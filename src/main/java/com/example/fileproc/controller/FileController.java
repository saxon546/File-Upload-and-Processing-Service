package com.example.fileproc.controller;

import com.example.fileproc.db.InMemoryDatabase;
import com.example.fileproc.exception.NotFoundException;
import com.example.fileproc.model.ProcessedFile;
import com.example.fileproc.service.FileProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;

@RestController
@RequestMapping("/api/files")
@Validated
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    private final FileProcessingService service;
    private final InMemoryDatabase db;

    public FileController(FileProcessingService service, InMemoryDatabase db) {
        this.service = service;
        this.db = db;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProcessedFile> upload(@RequestPart("file") MultipartFile file) {
        log.debug("Entering upload endpoint");
        ProcessedFile processed = service.uploadAndProcess(file);
        return ResponseEntity.ok(processed);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessedFile> getById(@PathVariable String id) {
        return db.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Processed file not found"));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ProcessedFile>> listAll() {
        return ResponseEntity.ok(db.findAll());
    }
}

