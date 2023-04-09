package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.document.Document;

/**
 * This interface provides a way of a client to interact with some functionality in the application.
 * In this case provides two methods to save and get documents
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.document.Document
 * @since jdk-11.0.13
 */
public interface DocumentationService {

    /**
     * This method allows to save a document
     * @param document a Document object with a document
     * @return a Document object
     */
    Document saveDocument(Document document);

    /**
     * This methods allows to get a document through an id
     * @param documentId a string with the document id
     * @return a Document object
     */
    Document getDocument(String documentId);
}
