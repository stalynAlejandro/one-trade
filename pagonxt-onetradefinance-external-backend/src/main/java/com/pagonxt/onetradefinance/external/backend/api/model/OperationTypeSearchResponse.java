package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines an object with a list of operation types
 * @author -
 * @version jdk-11.0.13
 * @see  com.pagonxt.onetradefinance.external.backend.api.model.OperationTypeDto
 * @since jdk-11.0.13
 */
public class OperationTypeSearchResponse extends ArrayList<OperationTypeDto> {

    /**
     * Class constructor
     * @param operationTypes list of operations types
     */
    public OperationTypeSearchResponse(List<OperationTypeDto> operationTypes) {
        super(operationTypes);
    }

}
