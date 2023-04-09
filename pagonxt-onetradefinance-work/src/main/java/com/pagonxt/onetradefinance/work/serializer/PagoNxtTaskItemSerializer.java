package com.pagonxt.onetradefinance.work.serializer;

import com.pagonxt.onetradefinance.integrations.constants.FieldConstants;
import com.pagonxt.onetradefinance.integrations.model.tasks.PagoNxtTaskItem;
import com.pagonxt.onetradefinance.work.service.model.Task;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.EVENT;
import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.PRODUCT;
import static com.pagonxt.onetradefinance.work.common.FilterConstants.*;
import static com.pagonxt.onetradefinance.work.utils.TaskUtils.*;

/**
 * Service class to serialize data
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Service
public class PagoNxtTaskItemSerializer {

    /**
     * Method to serialize some fields
     * @param instance  : a Task object
     * @see com.pagonxt.onetradefinance.work.service.model.Task
     * @see  com.pagonxt.onetradefinance.integrations.model.tasks.PagoNxtTaskItem
     * @return a PagoNxtTaskItem object
     */
    public PagoNxtTaskItem serialize(Task instance) {
        if (instance == null) {
            return null;
        }
        PagoNxtTaskItem result = new PagoNxtTaskItem();
        result.setRowId(getUUID());
        result.setOperationId(getString(instance, OPERATION_CODE, SCOPE_ROOT));
        result.setTaskId(instance.getId());
        result.setMercuryRef(getString(instance, OPERATION_CONTRACT_REFERENCE, SCOPE_ROOT));
        result.setIssuanceDate(getDate(instance, OPERATION_CREATION_DATE, SCOPE_ROOT));
        result.setProduct(getString(instance, PRODUCT, SCOPE_ROOT));
        result.setEvent(getString(instance, EVENT, SCOPE_ROOT));
        result.setClient(getString(instance, OPERATION_CUSTOMER_FULL_NAME, SCOPE_ROOT));
        result.setTask(getString(instance, FieldConstants.EXTERNAL_TASK_TYPE, null));
        result.setPriority(getString(instance, OPERATION_PRIORITY, SCOPE_ROOT));
        String operationDisplayedStatus = getString(instance, OPERATION_DISPLAYED_STATUS, SCOPE_ROOT);
        result.setStatus(operationDisplayedStatus == null ? "DRAFT" : operationDisplayedStatus);
        result.setRequesterName(getString(instance, OPERATION_REQUESTER_DISPLAYED_NAME, SCOPE_ROOT));
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
