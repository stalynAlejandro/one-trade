package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.service.DocumentService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static com.pagonxt.onetradefinance.integrations.util.LogUtils.sanitizeLog;

/**
 * Controller with the endpoints to get documents from the external app
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.DocumentService
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/documents")
public class DocumentController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(DocumentController.class);

    /**
     * Class attributes
     */
    private final DocumentService documentService;
    private final UserInfoService userInfoService;

    /**
     * Document Controller Constructor
     * @param documentService Service that provides necessary functionality to this controller
     * @param userInfoService Service that provides necessary functionality for userInfo
     */
    public DocumentController(DocumentService documentService,
                              UserInfoService userInfoService) {
        this.documentService = documentService;
        this.userInfoService = userInfoService;
    }

    /**
     * This method gets a document id for search and returns a document
     * @param id a string with document id
     * @return document with data
     */
    @GetMapping("/{document_id}")
    public ResponseEntity<StreamingResponseBody> findDocument(@PathVariable(name = "document_id") String id) {
        Document foundDocument = documentService.findDocument(id, userInfoService.getUserInfo());
        if (LOG.isDebugEnabled()) {
            LOG.debug("findDocument(id: {}): Found document : {}", sanitizeLog(id), sanitizeLog(foundDocument));
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(foundDocument.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + foundDocument.getFilename())
                .body(outputStream -> documentService.writeDocumentData(outputStream, foundDocument));
    }
}
