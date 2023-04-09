package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.CollectionTypeDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CollectionTypeSearchResponse;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CollectionTypeDtoSerializer;
import com.pagonxt.onetradefinance.integrations.service.CollectionTypeService;
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
 * Controller with the endpoints to get collection Types from the external app
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.service.CollectionTypeService
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.CollectionTypeDtoSerializer
 * @since jdk-11.0.13
 */
@Validated
@RestController
@RequestMapping("${controller.path}/collection-types")
public class CollectionTypeController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(CollectionTypeController.class);

    /**
     * Class attributes
     */
    private final CollectionTypeDtoSerializer collectionTypeDtoSerializer;
    private final CollectionTypeService collectionTypeService;

    /**
     * Collection Type Constructor
     * @param collectionTypeDtoSerializer Component for the conversion and adaptation of the data structure
     * @param collectionTypeService Service that provides necessary functionality to this controller
     */
    public CollectionTypeController(CollectionTypeDtoSerializer collectionTypeDtoSerializer,
                                    CollectionTypeService collectionTypeService) {
        this.collectionTypeDtoSerializer = collectionTypeDtoSerializer;
        this.collectionTypeService = collectionTypeService;
    }

    /**
     * This method gets a product id and returns a list of collection Types
     * @param productId a string with the productId
     * @param currency a string with the currency
     * @return a list of collection types with their data
     */
    @GetMapping
    @Secured("ROLE_USER")
    public CollectionTypeSearchResponse searchCollectionTypes(@RequestParam(name = "product_id") @NotEmpty String productId,
                                                              @RequestParam(required = false) String currency) {
        List<CollectionTypeDto> collectionTypes = collectionTypeService.
                getCollectionType(productId, currency).stream().map(collectionTypeDtoSerializer::toDto)
                .collect(Collectors.toList());
        CollectionTypeSearchResponse result = new CollectionTypeSearchResponse(collectionTypes);
        LOG.debug("searchCollection() returned: {}", result);
        return result;
    }
}
