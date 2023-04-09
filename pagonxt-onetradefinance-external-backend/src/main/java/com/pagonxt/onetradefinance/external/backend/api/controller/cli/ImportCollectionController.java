package com.pagonxt.onetradefinance.external.backend.api.controller.cli;

import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionList;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cle.ExportCollectionDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.cli.ImportCollectionDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.external.backend.service.cle.ExportCollectionService;
import com.pagonxt.onetradefinance.external.backend.service.trade.TradeContractService;
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
 * @see UserService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/import-collections")
public class ImportCollectionController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(ImportCollectionController.class);

    /**
     * Class attributes
     */
    private final TradeContractService tradeContractService;

    private final ImportCollectionDtoSerializer serializer;

    private final UserInfoService userInfoService;

    /**
     * Import Collection Controller Constructor
     *
     * @param tradeContractService      Service that provides necessary functionality to this controller
     * @param serializer                Component for the conversion and adaptation of the data structure
     * @param userInfoService           Service that provides necessary functionality with user
     */
    public ImportCollectionController(
            TradeContractService tradeContractService,
            ImportCollectionDtoSerializer serializer,
            UserInfoService userInfoService) {
        this.tradeContractService = tradeContractService;
        this.serializer = serializer;
        this.userInfoService = userInfoService;
    }

    /**
     * This method obtains a customer personNumber from a client and returns his export collections requests
     * @param customer a string with a customer personNumber
     * @return a list with requested advances (approved)
     */
    @GetMapping
    @Secured("ROLE_USER")
    public ImportCollectionList getImportCollectionsByCustomer(@RequestParam String customer) {
        List<ImportCollectionDto> importCollections = tradeContractService.
                getCustomerImportCollections(customer, userInfoService.getUserInfo()).stream()
                .map(serializer::toDto).collect(Collectors.toList());
        ImportCollectionList result = new ImportCollectionList(importCollections);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getImportCollectionsByCustomer(customer: {}) returned: {}", sanitizeLog(customer), result);
        }
        return result;
    }
}
