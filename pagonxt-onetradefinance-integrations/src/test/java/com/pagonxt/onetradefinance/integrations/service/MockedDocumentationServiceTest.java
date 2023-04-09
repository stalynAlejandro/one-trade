package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
class MockedDocumentationServiceTest {

    private final MockedDocumentationService documentationService = new MockedDocumentationService(new ObjectMapper());

    @Test
    void saveDocument_ok_returnsDocumentWithId() {
        // Given

        // When
        Document result = documentationService.saveDocument(new Document());

        // Then
        assertNotNull(result.getDocumentId());
    }

    @Test
    void loadDocument_ok_returnsValidData() {
        // Given

        // When
        Document result = documentationService.getDocument("002");

        // Then
        assertNotNull(result, "Should return non-null value");
        assertEquals("002", result.getDocumentId(), "Should return valid id");
        assertEquals("miercoles.jpg", result.getFilename(), "Should return valid filename");
        assertEquals("image/jpeg", result.getMimeType(), "Should return valid mime type");
        assertEquals("File contents", result.getData(), "Should return valid data");
    }

    @Test
    void loadDocument_invalidId_throwsException() {
        // Given

        // When
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> documentationService.getDocument("001"));

        // Then
        assertEquals("Document with id '001' not found", exception.getMessage(), "Should throw exception with valid message");
    }
}
