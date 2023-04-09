package com.pagonxt.onetradefinance.integrations.model;

import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.document.DocumentMultipartFile;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@UnitTest
class DocumentMultipartFileTest {

    @Test
    void constructor_ok_returnInstance() throws IOException {
        // Given
        Document document = new Document("docId", "test.txt", "text/plain", new Date(), "document", "eW91ciBmaWxlIGNvbnRlbnRz");
        // When
        DocumentMultipartFile documentMultipartFile = new DocumentMultipartFile(document);
        // Then
        assertEquals("test.txt", documentMultipartFile.getName());
        assertEquals("test.txt", documentMultipartFile.getOriginalFilename());
        assertEquals("text/plain", documentMultipartFile.getContentType());
        assertFalse(documentMultipartFile.isEmpty());
        assertEquals(18, documentMultipartFile.getSize());
        assertEquals(18, documentMultipartFile.getBytes().length);
    }

    @Test
    void isEmpty_nullData_returnTrue() {
        // Given
        Document document = new Document("docId", "test.txt", "text/plain", new Date(), "document", "");
        DocumentMultipartFile documentMultipartFile = new DocumentMultipartFile(document);
        ReflectionTestUtils.setField(documentMultipartFile, "fileContent", null);
        // When and Then
        assertTrue(documentMultipartFile.isEmpty());
    }

    @Test
    void isEmpty_emptyData_returnTrue() {
        // Given
        Document document = new Document("docId", "test.txt", "text/plain", new Date(), "document", "");
        DocumentMultipartFile documentMultipartFile = new DocumentMultipartFile(document);
        // When and Then
        assertTrue(documentMultipartFile.isEmpty());
    }

    @Test
    void getInputStream_ok_returnInputStream() throws IOException {
        // Given
        Document document = new Document("docId", "test.txt", "text/plain", new Date(), "document", "eW91ciBmaWxlIGNvbnRlbnRz");
        DocumentMultipartFile documentMultipartFile = new DocumentMultipartFile(document);
        // When
        InputStream inputStream = documentMultipartFile.getInputStream();
        // Then
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
                (inputStream, StandardCharsets.UTF_8))) {
            int c;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }
        assertEquals("your file contents", textBuilder.toString());
    }
}
