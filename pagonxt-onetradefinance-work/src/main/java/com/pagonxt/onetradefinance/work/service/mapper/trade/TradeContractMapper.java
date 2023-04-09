package com.pagonxt.onetradefinance.work.service.mapper.trade;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContract;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * Class with some methods to serialize and deserialize data of export collections and data objects
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class TradeContractMapper {
    private static final String[] CONTRACT_COMMON_FIELDS = {CREATION_DATE, APPROVAL_DATE
            , REQUEST_CUSTOMER_CODE, REQUEST_CUSTOMER_ID, REQUEST_CUSTOMER_EMAIL,REQUEST_CUSTOMER_SEGMENT
            , REQUEST_CUSTOMER_FULL_NAME, REQUEST_CODE, STATUS, STATUS_REASON, CONTRACT_REFERENCE, COUNTRY
            , REQUEST_COMMENTS};

    /**
     * Class method to map data from a data object to an Trade Contract
     *
     * @param dataObject : a DataObjectInstanceVariableContainer object
     * @return a {@link TradeContract} object
     * @see DataObjectInstanceVariableContainer
     * @see TradeContract
     */
    public TradeContract mapToTradeContract(
            DataObjectInstanceVariableContainer dataObject) {
        if (dataObject == null) {
            return null;
        }
        TradeContract result = new TradeContract();
        //COMMON DATA
        //customer data
        result.setCustomer(new Customer());
        result.getCustomer().setPersonNumber(dataObject.getString(REQUEST_CUSTOMER_CODE));
        result.getCustomer().setTaxId(dataObject.getString(REQUEST_CUSTOMER_ID));
        result.getCustomer().setEmail(dataObject.getString(REQUEST_CUSTOMER_EMAIL));
        result.getCustomer().setSegment(dataObject.getString(REQUEST_CUSTOMER_SEGMENT));
        result.getCustomer().setName(dataObject.getString(REQUEST_CUSTOMER_FULL_NAME));
        //Other contract common data
        result.setCreationDate(dataObject.getDate(CREATION_DATE));
        result.setApprovalDate(dataObject.getDate(APPROVAL_DATE));
        result.setCode(dataObject.getString(REQUEST_CODE));
        result.setStatus(dataObject.getString(STATUS));
        result.setStatusReason(dataObject.getString(STATUS_REASON));
        result.setContractReference(dataObject.getString(CONTRACT_REFERENCE));
        result.setCountry(dataObject.getString(COUNTRY));
        result.setComment(dataObject.getString(REQUEST_COMMENTS));
        //DETAILS
        //Details are data fields that are not common
        Map<String, Object> detailsContractMap= getSimplifiedMap(((DataObjectInstanceVariableContainerImpl)dataObject)
                .getData());
        detailsContractMap.keySet().removeAll(Arrays.asList(CONTRACT_COMMON_FIELDS));
        result.setDetails(detailsContractMap);
        return result;
    }

    /**
     * Convert a data map of variables into a simplified map, avoiding including unnecesary information
     *  for business when variables is of type 'flowableDataObject'
     * @param dataMap       : a {@link Map} object
     * @return a simplified map
     */
    private Map<String, Object> getSimplifiedMap (Map<String, Object> dataMap){
        Map<String, Object> simplifiedMap = new HashMap<>();
        // Iterate over the HashMap
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            //when variable is a flowableDataObject there area a lot of unnecessary information
            //that is not needed for business
            if (entry.getValue() instanceof DataObjectInstanceVariableContainerImpl){
                //simplify value, getting only data map
                simplifiedMap.put(entry.getKey(),
                        this.mapToTradeContract((DataObjectInstanceVariableContainerImpl) entry.getValue()));
            }else {
                simplifiedMap.put(entry.getKey(), entry.getValue());
            }
        }
        return simplifiedMap;
    }

}
