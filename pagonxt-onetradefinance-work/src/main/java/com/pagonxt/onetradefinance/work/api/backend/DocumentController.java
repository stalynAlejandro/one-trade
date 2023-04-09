package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.work.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for documents
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.DocumentService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/document", produces = "application/json")
public class DocumentController {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(DocumentController.class);

    //class attribute
    private final DocumentService documentService;

    /**
     * constructor method
     * @param documentService a DocumentService object. Service that provides necessary functionality to this controller
     */
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * Method to find a document through an id
     * @param id        : a string with the document id
     * @param request   : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/get/{id}")
    public ControllerResponse findDocument(@PathVariable String id, @RequestBody AuthenticatedRequest request) {
        Document foundDocument = documentService.findDocument(request.getRequester(), id);
        LOG.debug("findDocument(id: {}) returned: {}", id, foundDocument);
        return ControllerResponse.success("findDocument", foundDocument);
    }

}
