package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionList;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.util.LogUtils.sanitizeLog;

/**
 * Controller with endpoints to get a list of export collections requests from a customer
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionService
 * @see ExportCollectionDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.service.UserService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/export-collections")
public class ExportCollectionController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(ExportCollectionController.class);

    /**
     * Class attributes
     */
    private final ExportCollectionService exportCollectionService;

    private final ExportCollectionDtoSerializer serializer;

    private final UserService userService;

    /**
     * Export Collection Controller Constructor
     * @param exportCollectionService Service that provides necessary functionality to this controller
     * @param serializer Component for the conversion and adaptation of the data structure
     * @param userService Service that provides necessary functionality with user
     */
    public ExportCollectionController(
            ExportCollectionService exportCollectionService,
            ExportCollectionDtoSerializer serializer,
            UserService userService) {
        this.exportCollectionService = exportCollectionService;
        this.serializer = serializer;
        this.userService = userService;
    }

    /**
     * This method obtains a customer personNumber from a client and returns his export collections requests
     * @param customer a string with a customer personNumber
     * @return a list with requested advances (approved)
     */
    @GetMapping
    @Secured("ROLE_USER")
    public ExportCollectionList getExportCollectionsByCustomer(@RequestParam String customer) {
        List<ExportCollectionDto> exportCollections = exportCollectionService.
                getCustomerExportCollections(customer, userService.getCurrentUser()).stream()
                .map(serializer::toDto).collect(Collectors.toList());
        ExportCollectionList result = new ExportCollectionList(exportCollections);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getExportCollectionsByCustomer(customer: {}) returned: {}", sanitizeLog(customer), result);
        }
        return result;
    }
}
