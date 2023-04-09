package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionQuery;
import com.pagonxt.onetradefinance.work.service.ExportCollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class with generic methods to work with export collection
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.ExportCollectionService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/export-collections", produces = "application/json")
public class ExportCollectionController {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(ExportCollectionController.class);

    //Class attribute
    private final ExportCollectionService exportCollectionService;

    /**
     * constructor method
     * @param exportCollectionService object to provide necessary functionality
     */
    public ExportCollectionController(ExportCollectionService exportCollectionService) {
        this.exportCollectionService = exportCollectionService;
    }

    /**
     * Method to get export collections
     * @param query an ExportCollectionQuery object
     * @return a ControllerResponse object
     */
    @PostMapping
    public ControllerResponse getExportCollections(@RequestBody ExportCollectionQuery query) {
        Object request = exportCollectionService.getExportCollections(query);
        LOG.debug("getExportCollections(query: {}) returned: {}", query, request);
        return ControllerResponse.success("getExportCollections", request);
    }
}
