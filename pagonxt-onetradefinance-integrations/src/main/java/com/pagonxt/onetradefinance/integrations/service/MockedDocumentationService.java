package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get mocked documentation
 * @author -
 * @version jdk-11.0.13
 * @see DocumentationService
 * @see com.pagonxt.onetradefinance.integrations.model.document.Document
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @since jdk-11.0.13
 */
public class MockedDocumentationService implements DocumentationService {
    private static final Logger LOG = LoggerFactory.getLogger(MockedDocumentationService.class);

    private final List<Document> documents = new ArrayList<>();

    /**
     * constructor method
     * @param mapper an ObjectMapper object, that provides functionality for reading and writing JSON
     */
    public MockedDocumentationService(ObjectMapper mapper) {
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("mock-data/documents.json")) {
            documents.addAll(mapper.readValue(resourceStream, new TypeReference<>() {}));
            LOG.debug("Mock documents repository initialized with {} items", documents.size());
        } catch (Exception e) {
            LOG.warn("Error reading documents file, mock repository will be empty", e);
        }
    }

    /**
     * This method allows to save a document
     * @param document a Document object with a document
     * @return a Document object
     */
    @Override
    public Document saveDocument(Document document) {
        if (Strings.isBlank(document.getDocumentId())) {
            document.setDocumentId(UUID.randomUUID().toString());
        }
        return document;
    }

    /**
     * This methods allows to get a document through an id
     * @param documentId a string with the document id
     * @return a Document object
     */
    @Override
    public Document getDocument(String documentId) {
        return documents.parallelStream()
                .filter(document -> documentId.equals(document.getDocumentId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Document with id '" + documentId + "' not found"));
    }
}
