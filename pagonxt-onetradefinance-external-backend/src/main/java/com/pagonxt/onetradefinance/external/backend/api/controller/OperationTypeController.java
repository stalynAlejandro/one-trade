package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.OperationTypeDto;
import com.pagonxt.onetradefinance.external.backend.api.model.OperationTypeSearchResponse;
import com.pagonxt.onetradefinance.external.backend.api.serializer.OperationTypeDtoSerializer;
import com.pagonxt.onetradefinance.integrations.service.OperationTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller with the endpoints to get operation Types from the external app
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.OperationTypeDtoSerializer
 * @see com.pagonxt.onetradefinance.integrations.service.OperationTypeService
 * @since jdk-11.0.13
 */
@Validated
@RestController
@RequestMapping("${controller.path}/operation-types")
public class OperationTypeController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(OperationTypeController.class);

    /**
     * Class attributes
     */
    private final OperationTypeDtoSerializer operationTypeDtoSerializer;

    private final OperationTypeService operationTypeService;

    /**
     * Operation Type Controller Constructor
     * @param operationTypeDtoSerializer Component for the conversion and adaptation of the data structure
     * @param operationTypeService Service that provides necessary functionality to this controller
     */
    public OperationTypeController(
            OperationTypeDtoSerializer operationTypeDtoSerializer,
            OperationTypeService operationTypeService) {
        this.operationTypeDtoSerializer = operationTypeDtoSerializer;
        this.operationTypeService = operationTypeService;
    }

    /**
     * This method gets a product id and returns a list of operation Types
     * @param productId a string with the productId (CLE, CLI,...)
     * @return a list of operation types with their data
     */
    @GetMapping
    @Secured("ROLE_USER")
    public OperationTypeSearchResponse searchOperationTypes(@RequestParam (name = "product_id") @NotEmpty String productId) {
        List<OperationTypeDto> operationTypes = operationTypeService.
                getOperationTypeByProduct(productId).stream().map(operationTypeDtoSerializer::toDto)
                .collect(Collectors.toList());
        OperationTypeSearchResponse result = new OperationTypeSearchResponse(operationTypes);
        LOG.debug("searchCollection() returned: {}", result);
        return result;
  }

}
