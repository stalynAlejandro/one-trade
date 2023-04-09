package com.pagonxt.onetradefinance.external.backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService
 * @since jdk-11.0.13
 */
@Service
public class DocumentService {

    /**
     * Class attributes
     */
    public final RestTemplateWorkService restTemplateWorkService;

    /**
     * Class constructor
     * @param restTemplateWorkService RestTemplateWorkService object
     */
    public DocumentService(RestTemplateWorkService restTemplateWorkService) {
        this.restTemplateWorkService = restTemplateWorkService;
    }

    /**
     * Method to find a document
     * @param id document id
     * @param userInfo UserInfo object
     * @see com.pagonxt.onetradefinance.integrations.model.document.Document
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a document, mapped
     */
    public Document findDocument(String id, UserInfo userInfo) {
        ControllerResponse response = restTemplateWorkService.findDocument(id, new AuthenticatedRequest(userInfo));
        return new ObjectMapper().convertValue(response.getEntity(), Document.class);
    }

    /**
     * Method to write document data
     * @param outputStream OutputStream object
     * @param document a document object
     * @see java.io.OutputStream
     *  @see com.pagonxt.onetradefinance.integrations.model.document.Document
     */
    public void writeDocumentData(OutputStream outputStream, Document document) {
        try {
            outputStream.write(Base64.getDecoder().decode(document.getData()));
        } catch (IOException e) {
            throw new ServiceException("Error writing document data", "writeDocumentDataError");
        }
    }
}
