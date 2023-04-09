package com.pagonxt.onetradefinance.work.service.mapper;

import com.flowable.content.engine.impl.persistence.entity.ContentItemEntity;
import com.flowable.core.content.api.MetadataService;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class with some methods to serialize and deserialize data of operation documents
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.core.content.api.MetadataService
 * @since jdk-11.0.13
 */
@Component
public class OperationDocumentMapper {

    //class attribute
    private final MetadataService metadataService;

    /**
     * constructor method
     * @param metadataService : a MetadataService object
     */
    public OperationDocumentMapper(MetadataService metadataService) {
        this.metadataService = metadataService;
    }

    /**
     * Method to map operation documents
     * @param operationDocuments : a list of operation documents
     * @see com.flowable.content.engine.impl.persistence.entity.ContentItemEntity
     * @see com.pagonxt.onetradefinance.integrations.model.document.Document
     * @return a list of documents
     */
    public List<Document> mapOperationDocuments(List<ContentItemEntity> operationDocuments) {
        if (operationDocuments == null) {
            return Collections.emptyList();
        }
        List<Document> documents = new ArrayList<>();
        for(ContentItemEntity contentItem : operationDocuments) {
            Document document = new Document();
            document.setDocumentId(contentItem.getId());
            document.setFilename(contentItem.getName());
            document.setMimeType(contentItem.getMimeType());
            document.setUploadedDate(contentItem.getLastModified());
            document.setDocumentType((String) metadataService.getMetadataValue(contentItem.getId(), "typology"));
            documents.add(document);
        }
        return documents;
    }
}
