package com.pagonxt.onetradefinance.work.serializer;

import com.pagonxt.onetradefinance.integrations.model.requests.PagoNxtRequestItem;
import com.pagonxt.onetradefinance.work.service.model.Case;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.EVENT;
import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.PRODUCT;
import static com.pagonxt.onetradefinance.work.common.FilterConstants.*;
import static com.pagonxt.onetradefinance.work.utils.CaseVariableUtils.*;

/**
 * Service class to serialize data
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Service
public class PagoNxtRequestItemSerializer {

    /**
     * Method to serialize some fields
     * @param instance  : a Case object
     * @see com.pagonxt.onetradefinance.work.service.model.Case
     * @see com.pagonxt.onetradefinance.integrations.model.requests.PagoNxtRequestItem
     * @return a PagoNxtRequestItem object
     */
    public PagoNxtRequestItem serialize(Case instance) {
        if (instance == null) {
            return null;
        }
        PagoNxtRequestItem result = new PagoNxtRequestItem();
        result.setRowId(getUUID());
        result.setOperationId(getString(instance, OPERATION_CODE));
        result.setMercuryRef(getString(instance, OPERATION_CONTRACT_REFERENCE));
        String operationDisplayedStatus = getStringOrDefault(instance, OPERATION_DISPLAYED_STATUS,"DRAFT");
        result.setStatus(operationDisplayedStatus);
        result.setIssuanceDate(getDate(instance, OPERATION_CREATION_DATE));
        result.setProduct(getString(instance, PRODUCT));
        result.setEvent(getString(instance, EVENT));
        result.setPriority(getString(instance, OPERATION_PRIORITY));
        result.setAmount(getDouble(instance, OPERATION_AMOUNT));
        result.setCurrency(getString(instance, OPERATION_CURRENCY));
        result.setContractReference(getString(instance, OPERATION_CONTRACT_REFERENCE));
        result.setRequestedDate(getDate(instance, OPERATION_CREATION_DATE));
        result.setOffice(getString(instance, OPERATION_OFFICE));
        result.setClient(getString(instance, OPERATION_CUSTOMER_FULL_NAME));
        result.setRequesterName(getString(instance, OPERATION_REQUESTER_DISPLAYED_NAME));
        result.setResolution(operationDisplayedStatus.equals("DRAFT") ? null: getStringOrDefault(instance,
                OPERATION_RESOLUTION,"IN_PROGRESS") );
        return result;
    }

    /**
     * Method to get the UUID
     * @return a string object with the UUID
     */
    protected String getUUID() {
        return UUID.randomUUID().toString();
    }
}
