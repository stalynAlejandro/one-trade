package com.pagonxt.onetradefinance.work.service;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionQuery;
import com.pagonxt.onetradefinance.work.service.mapper.ExportCollectionMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.constants.ExportCollectionConstants.COLLECTION_TYPE_ADVANCE;
import static com.pagonxt.onetradefinance.integrations.constants.ExportCollectionConstants.COLLECTION_TYPE_REQUEST;
import static com.pagonxt.onetradefinance.work.common.CaseCommonConstants.DATA_OBJECT_MODEL;
import static com.pagonxt.onetradefinance.work.common.CaseCommonConstants.DATA_OBJECT_MODEL_ADVANCE;

/**
 * service class for export collections
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.dataobject.api.runtime.DataObjectRuntimeService
 * @see com.pagonxt.onetradefinance.work.service.mapper.ExportCollectionMapper
 * @since jdk-11.0.13
 */
@Service
public class ExportCollectionService {
    public static final String SEARCH_BY_CUSTOMER_CODE_FUNCTION_NAME_REQUEST = "searchExportCollectionByCustomerCodeAndStatusOperation";
    public static final String SEARCH_BY_CUSTOMER_CODE_FUNCTION_NAME_ADVANCE = "searchExportCollectionAdvanceByCustomerCodeAndStatusOperation";
    public static final String PARAM_CUSTOMER_CODE = "customerCode";
    public static final String PARAM_STATUS = "status";
    public static final String VALUE_ACTIVE = "ACTIVE";

    //class attributes
    private final DataObjectRuntimeService dataObjectRuntimeService;
    private final ExportCollectionMapper exportCollectionMapper;

    /**
     * constructor method
     *
     * @param dataObjectRuntimeService: a DataObjectRuntimeService object
     * @param exportCollectionMapper  : a ExportCollectionMapper object
     *
     */
    public ExportCollectionService(DataObjectRuntimeService dataObjectRuntimeService,
                                   ExportCollectionMapper exportCollectionMapper) {
        this.dataObjectRuntimeService = dataObjectRuntimeService;
        this.exportCollectionMapper = exportCollectionMapper;
    }

    /**
     * Method to get export collections
     *
     * @param query : a ExportCollectionQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionQuery
     * @return an Object with export collections
     *
     */
    public Object getExportCollections(ExportCollectionQuery query) {
        String collectionType = query.getCollectionType();
        if(COLLECTION_TYPE_REQUEST.equals(collectionType)) {
            DataObjectInstanceVariableContainerQuery dataObjectQuery = this.dataObjectRuntimeService
                    .createDataObjectInstanceQuery()
                    .definitionKey(DATA_OBJECT_MODEL)
                    .operation(SEARCH_BY_CUSTOMER_CODE_FUNCTION_NAME_REQUEST)
                    .value(PARAM_CUSTOMER_CODE,  query.getCustomerPersonNumber())
                    .value(PARAM_STATUS, VALUE_ACTIVE);
            List<DataObjectInstanceVariableContainer> results = dataObjectQuery.list();

            return results.stream().map(exportCollectionMapper::
                    mapDataObjectInstanceVariableContainerToExportCollection).collect(Collectors.toList());
        }
        if(COLLECTION_TYPE_ADVANCE.equals(collectionType)) {
            DataObjectInstanceVariableContainerQuery dataObjectQuery = this.dataObjectRuntimeService
                    .createDataObjectInstanceQuery()
                    .definitionKey(DATA_OBJECT_MODEL_ADVANCE)
                    .operation(SEARCH_BY_CUSTOMER_CODE_FUNCTION_NAME_ADVANCE)
                    .value(PARAM_CUSTOMER_CODE,  query.getCustomerPersonNumber())
                    .value(PARAM_STATUS, VALUE_ACTIVE);
            List<DataObjectInstanceVariableContainer> results = dataObjectQuery.list();

            return results.stream().map(exportCollectionMapper::
                    mapDataObjectInstanceVariableContainerToExportCollectionAdvance).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
