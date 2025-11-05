package com.example.fileproc;

import com.example.fileproc.db.InMemoryDatabase;
import com.example.fileproc.model.ProcessedFile;
import com.example.fileproc.exception.BadRequestException;
import com.example.fileproc.service.FileProcessingService;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessingServiceTest {

    @Test
    void countsLinesAndWords() {
        InMemoryDatabase db = new InMemoryDatabase();
        FileProcessingService svc = new FileProcessingService(db);
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.txt", "text/plain", "hello world\nsecond line\n\nthird".getBytes()
        );

        ProcessedFile pf = svc.uploadAndProcess(file);
        assertEquals(4, pf.getLineCount());  // includes empty line
        assertEquals(5, pf.getWordCount());  // "hello","world","second","line","third"
    }

    @Test
    void rejectsEmptyFile() {
        InMemoryDatabase db = new InMemoryDatabase();
        FileProcessingService svc = new FileProcessingService(db);
        MockMultipartFile file = new MockMultipartFile("file", "empty.txt", "text/plain", new byte[0]);

        assertThrows(BadRequestException.class, () -> svc.uploadAndProcess(file));
    }

    @Test
    void rejectsDisallowedExtension() {
        InMemoryDatabase db = new InMemoryDatabase();
        FileProcessingService svc = new FileProcessingService(db);
        MockMultipartFile file = new MockMultipartFile("file", "image.png", "image/png", "data".getBytes());

        assertThrows(BadRequestException.class, () -> svc.uploadAndProcess(file));
    }
}
