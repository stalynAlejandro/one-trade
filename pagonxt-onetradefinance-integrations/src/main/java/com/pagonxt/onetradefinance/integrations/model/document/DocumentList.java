package com.pagonxt.onetradefinance.integrations.model.document;

import java.util.ArrayList;

/**
 * Model class to create an object with a list of documents
 * @author -
 * @version jdk-11.0.13
 * @see Document
 * @since jdk-11.0.13
 */
public class DocumentList extends ArrayList<Document> {

    /**
     * Constructor method
     * @param documentArrayList a list of documents
     */
    public DocumentList(ArrayList<Document> documentArrayList){
        super(documentArrayList);
    }
}
