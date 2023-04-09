package com.pagonxt.onetradefinance.external.backend.api.controller.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceList;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionAdvanceDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionAdvanceService;
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
 * Controller with endpoints to get a list of export collections advance requests from a customer
 * @author -
 * @version jdk-11.0.13
 * @see ExportCollectionAdvanceService
 * @see ExportCollectionAdvanceDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/export-collection-advances")
public class ExportCollectionAdvanceController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(ExportCollectionAdvanceController.class);

    /**
     * Class attributes
     */
    private final ExportCollectionAdvanceService exportCollectionAdvanceService;

    private final ExportCollectionAdvanceDtoSerializer serializer;

    private final UserService userService;

    /**
     * Export Collection Advance Controller Constructor
     * @param exportCollectionAdvanceService Service that provides necessary functionality to this controller
     * @param serializer Component for the conversion and adaptation of the data structure
     * @param userService Service that provides necessary functionality with user
     */
    public ExportCollectionAdvanceController(
            ExportCollectionAdvanceService exportCollectionAdvanceService,
            ExportCollectionAdvanceDtoSerializer serializer,
            UserService userService) {
        this.exportCollectionAdvanceService = exportCollectionAdvanceService;
        this.serializer = serializer;
        this.userService = userService;
    }

    /**
     * This method obtains a customer personNumber from a client and returns his export collections advance requests
     * @param customer a string with a customer personNumber
     * @return a list with requested advances (approved)
     */
    @GetMapping
    @Secured("ROLE_USER")
    public ExportCollectionAdvanceList getExportCollectionsAdvancesByCustomer(@RequestParam String customer) {
        List<ExportCollectionAdvanceDto> exportCollectionAdvances = exportCollectionAdvanceService.
                getCustomerExportCollectionAdvances(customer, userService.getCurrentUser()).stream()
                .map(serializer::toDto).collect(Collectors.toList());
        ExportCollectionAdvanceList result = new ExportCollectionAdvanceList(exportCollectionAdvances);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getExportCollectionsAdvancesByCustomer(customer:" +
                    " {}) returned: {}", sanitizeLog(customer), result);
        }
        return result;
    }
}
