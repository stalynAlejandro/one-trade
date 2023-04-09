package com.pagonxt.onetradefinance.work.service.mapper;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.pagonxt.onetradefinance.integrations.model.Account;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollection;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance;
import com.pagonxt.onetradefinance.integrations.util.ParseUtils;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.Date;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * Class with some methods to serialize and deserialize data of export collections and data objects
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class ExportCollectionMapper {

    /**
     * Class method to map data from a data object to an export collection request
     *
     * @param dataObject : a DataObjectInstanceVariableContainer object
     * @see com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollection
     *
     * @return an ExportCollection object
     */
    public ExportCollection mapDataObjectInstanceVariableContainerToExportCollection(
            DataObjectInstanceVariableContainer dataObject) {
        if (dataObject == null) {
            return null;
        }
        ExportCollection result = new ExportCollection();
        result.setCreationDate(dataObject.getDate(CREATION_DATE));
        result.setApprovalDate(dataObject.getDate(APPROVAL_DATE));
        result.setCustomer(new Customer());
        result.setCode(dataObject.getString(REQUEST_CODE));
        result.getCustomer().setPersonNumber(dataObject.getString(REQUEST_CUSTOMER_CODE));
        result.setContractReference(dataObject.getString(CONTRACT_REFERENCE));
        result.setAmount(ParseUtils.parseObjectToDouble(dataObject.getDouble(REQUEST_AMOUNT)));
        result.setCurrency(dataObject.getString(REQUEST_CURRENCY));
        Account account = new Account();
        account.setAccountId(dataObject.getString(REQUEST_NOMINAL_ACCOUNT_ID));
        result.setNominalAccount(account);
        return result;
    }

    /**
     * Class method to map data from a data object to an export collection advance request
     *
     * @param dataObject : a DataObjectInstanceVariableContainer object
     * @see com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance
     *
     * @return an ExportCollectionAdvance object
     */
    public ExportCollectionAdvance mapDataObjectInstanceVariableContainerToExportCollectionAdvance(
            DataObjectInstanceVariableContainer dataObject) {
        if (dataObject == null) {
            return null;
        }
        ExportCollectionAdvance result = new ExportCollectionAdvance();
        result.setCreationDate(dataObject.getDate(CREATION_DATE));
        result.setApprovalDate(dataObject.getDate(APPROVAL_DATE));
        result.setCustomer(new Customer());
        result.setCode(dataObject.getString(REQUEST_CODE));
        result.getCustomer().setPersonNumber(dataObject.getString(REQUEST_CUSTOMER_CODE));
        result.setContractReference(dataObject.getString(ADVANCE_CONTRACT_REFERENCE));
        result.setAmount(ParseUtils.parseObjectToDouble(dataObject.getDouble(REQUEST_ADVANCE_AMOUNT)));
        result.setCurrency(dataObject.getString(REQUEST_ADVANCE_CURRENCY));
        result.setExportCollection(mapDataObjectInstanceVariableContainerToExportCollection
                ((DataObjectInstanceVariableContainer) dataObject.getVariable(EXPORT_COLLECTION)));
        if(dataObject.getLocalDate(ADVANCE_DUE_DATE) != null) {
            result.setExpirationDate(Date.from(dataObject
                    .getLocalDate(ADVANCE_DUE_DATE).atStartOfDay(ZoneId.of("Europe/Madrid")).toInstant()));
        }
        return result;
    }
}
