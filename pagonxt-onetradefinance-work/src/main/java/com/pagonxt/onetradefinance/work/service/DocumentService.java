package com.pagonxt.onetradefinance.work.service;

import com.flowable.core.content.api.CoreContentService;
import com.flowable.core.content.api.MetadataService;
import com.flowable.platform.service.content.PlatformContentItemService;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.work.security.DocumentSecurityService;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import org.apache.commons.io.IOUtils;
import org.flowable.content.api.ContentItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * service class for documents
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.platform.service.content.PlatformContentItemService
 * @see com.pagonxt.onetradefinance.work.security.DocumentSecurityService
 * @see com.flowable.core.content.api.CoreContentService
 * @see com.flowable.core.content.api.MetadataService
 * @since jdk-11.0.13
 */
@Service
public class DocumentService {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(DocumentService.class);

    public static final String FIND_DOCUMENT = "findDocument";

    //class attributes
    private final PlatformContentItemService contentItemService;
    private final DocumentSecurityService documentSecurityService;
    private final CoreContentService contentService;
    private final MetadataService metadataService;

    /**
     * constructor method
     * @param contentItemService        : a PlatformContentItemService object
     * @param documentSecurityService   : a DocumentSecurityService object
     * @param contentService            : a CoreContentService object
     * @param metadataService           : a MetadataService object
     */
    public DocumentService(PlatformContentItemService contentItemService,
                           DocumentSecurityService documentSecurityService,
                           CoreContentService contentService,
                           MetadataService metadataService) {
        this.contentItemService = contentItemService;
        this.documentSecurityService = documentSecurityService;
        this.contentService = contentService;
        this.metadataService = metadataService;
    }

    /**
     * Method to find a document
     * @param userInfo  : a UserInfo object
     * @param id        : a string with the id
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.integrations.model.document.Document
     * @return a Document object
     */
    public Document findDocument(UserInfo userInfo, String id) {
        ContentItem contentItem = contentItemService.getContentItem(id);
        if (!contentItem.isContentAvailable()) {
            throw new ResourceNotFoundException("No data available for content item " +
                    contentItem.getId(), FIND_DOCUMENT);
        }
        documentSecurityService.checkReadDocument(userInfo, contentItem);
        InputStream dataStream = contentService.getContentItemData(id);
        if (dataStream == null) {
            throw new ResourceNotFoundException("Content item with id '" + id +
                    "' doesn't have content associated with it.", FIND_DOCUMENT);
        }
        try {
            Document foundDocument = new Document();
            foundDocument.setDocumentId(contentItem.getId());
            byte[] bytes = IOUtils.toByteArray(dataStream);
            String encoded = Base64.getEncoder().encodeToString(bytes);
            foundDocument.setData(encoded);
            foundDocument.setFilename(contentItem.getName());
            foundDocument.setMimeType(contentItem.getMimeType());
            String documentType = (String) metadataService.getMetadataValue(id, "typology");
            foundDocument.setDocumentType(documentType);
            return foundDocument;
        } catch (IOException e) {
            LOG.error("Error getting data from document", e);
            throw new ServiceException(String.format("Error getting data from document with id %s", id), FIND_DOCUMENT);
        }
    }
}
