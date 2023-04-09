package com.pagonxt.onetradefinance.work.utils;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainerQuery;
import com.flowable.dataobject.api.runtime.DataObjectRuntimeService;
import com.pagonxt.onetradefinance.work.service.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Class with export collection utils
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.dataobject.api.runtime.DataObjectRuntimeService
 * @since jdk-11.0.13
 */
@Component
public class ExportCollectionUtils {
    public static final String FIND_BY_ID_FUNCTION_NAME = "findById";
    public static final String PARAM_LOOKUP_ID = "lookupId";

    //class attribute
    private final DataObjectRuntimeService dataObjectRuntimeService;

    /**
     * constructor method
     * @param dataObjectRuntimeService : a DataObjectRuntimeService object
     */
    public ExportCollectionUtils(DataObjectRuntimeService dataObjectRuntimeService) {
        this.dataObjectRuntimeService = dataObjectRuntimeService;
    }

    /**
     * method to find a data object by id and model
     * @param code              : a string object with the model
     * @param dataObjectModel   : a string object with the dataObject model
     * @see com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer
     * @return a DataObjectInstanceVariableContainer object
     */
    public DataObjectInstanceVariableContainer findDataObjectByIdAndModel(String code, String dataObjectModel) {
        DataObjectInstanceVariableContainerQuery dataObjectQuery = this.dataObjectRuntimeService
                .createDataObjectInstanceQuery()
                .definitionKey(dataObjectModel)
                .operation(FIND_BY_ID_FUNCTION_NAME)
                .value(PARAM_LOOKUP_ID, code);
        DataObjectInstanceVariableContainer exportCollection = dataObjectQuery.singleResult();
        if (exportCollection == null) {
            throw new ResourceNotFoundException(String.format("No collection found with" +
                    " code %s", code), "findExportCollection");
        }
        return exportCollection;
    }
}
