package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.OperationTypeDto;
import com.pagonxt.onetradefinance.integrations.model.OperationType;
import org.springframework.stereotype.Component;

/**
 * This class has methods to convert a DTO class into an entity class and viceversa
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class OperationTypeDtoSerializer {

    /**
     * This method converts an OperationType object to an OperationTypeDto object
     * @param operationType OperationType object
     * @return operationTypeDto object
     */
    public OperationTypeDto toDto(OperationType operationType) {
        if(operationType == null) {
            return null;
        }
        OperationTypeDto operationTypeDto = new OperationTypeDto();
        operationTypeDto.setId(operationType.getId());
        operationTypeDto.setLabel(operationType.getName());
        operationTypeDto.setProduct(operationType.getProduct());
        operationTypeDto.setKey(operationType.getKey());
        return operationTypeDto;
    }

    /**
     * This method converts an OperationTypeDto object to an OperationType object
     * @param operationTypeDto accountDto object
     * @return operationType object
     */
    public OperationType toModel(OperationTypeDto operationTypeDto) {
        if (operationTypeDto == null) {
            return new OperationType();
        }
        OperationType operationType = new OperationType();
        operationType.setId(operationTypeDto.getId());
        operationType.setName(operationTypeDto.getLabel());
        operationType.setProduct(operationTypeDto.getProduct());
        operationType.setKey(operationTypeDto.getKey());
        return operationType;
    }

}
