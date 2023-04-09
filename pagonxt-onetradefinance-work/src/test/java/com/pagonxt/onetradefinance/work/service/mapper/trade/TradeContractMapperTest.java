package com.pagonxt.onetradefinance.work.service.mapper.trade;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContract;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class TradeContractMapperTest {

    @Test
    void mapToTradeContract_whenPassingDataObject_returnTradeContract(){
        // Given
        TradeContractMapper tradeContractMapper = new TradeContractMapper();
        Date date = new Date();
        String code = "code1";
        String customerCode = "customerCode1";
        String contractReference = "contractReference1";
        Double amount = 12.34d;
        String currency = "EUR";
        String status = "ACTIVE";
        String statusReason = "myStatusReason";
        String country = "country";
        String comment = "my comment";
        String customerId = "my_customer_id";
        String customerSegment = "my_customerSegment";
        String customerEmail = "my_customerEmail";
        String customerFullName = "my_customerFullName";
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = generateDataObject
                (date,date,code,customerCode,contractReference,amount,currency,status,statusReason
                        , country,comment, customerId, customerEmail, customerSegment, customerFullName);
        //When
        TradeContract result = tradeContractMapper.mapToTradeContract(dataObjectInstanceVariableContainer);
        //then
        assertEquals(date, result.getCreationDate());
        assertEquals(date, result.getApprovalDate());
        assertEquals(code, result.getCode());
        assertEquals(customerCode, result.getCustomer().getPersonNumber());
        assertEquals(contractReference, result.getContractReference());
        assertEquals(status, result.getStatus());
        assertEquals(statusReason, result.getStatusReason());
        assertEquals(country, result.getCountry());
        assertEquals(comment, result.getComment());
        assertEquals(amount, result.getDetails().get("amount"));
        assertEquals(currency, result.getDetails().get("currency"));
    }
    private static DataObjectInstanceVariableContainerImpl generateDataObject(Date approvalDate
            , Date creationDate, String code, String customerCode, String contractReference, Double amount
            , String currency, String status, String statusReason, String country
            , String comment, String customerId, String customerEmail, String customerSegment, String customerFullName) {
        DataObjectInstanceVariableContainerImpl dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainerImpl.class);

        //set common fields

        when(dataObjectInstanceVariableContainer.getString(REQUEST_CUSTOMER_CODE)).thenReturn(customerCode);
        when(dataObjectInstanceVariableContainer.getString(REQUEST_CUSTOMER_ID)).thenReturn(customerId);
        when(dataObjectInstanceVariableContainer.getString(REQUEST_CUSTOMER_EMAIL)).thenReturn(customerEmail);
        when(dataObjectInstanceVariableContainer.getString(REQUEST_CUSTOMER_SEGMENT)).thenReturn(customerSegment);
        when(dataObjectInstanceVariableContainer.getString(REQUEST_CUSTOMER_FULL_NAME)).thenReturn(customerFullName);
        //Other contract common data
        when(dataObjectInstanceVariableContainer.getDate(CREATION_DATE)).thenReturn(creationDate);
        when(dataObjectInstanceVariableContainer.getDate(APPROVAL_DATE)).thenReturn(approvalDate);
        when(dataObjectInstanceVariableContainer.getString(REQUEST_CODE)).thenReturn(code);
        when(dataObjectInstanceVariableContainer.getString(STATUS)).thenReturn(status);
        when(dataObjectInstanceVariableContainer.getString(STATUS_REASON)).thenReturn(statusReason);
        when(dataObjectInstanceVariableContainer.getString(CONTRACT_REFERENCE)).thenReturn(contractReference);
        when(dataObjectInstanceVariableContainer.getString(COUNTRY)).thenReturn(country);
        when(dataObjectInstanceVariableContainer.getString(REQUEST_COMMENTS)).thenReturn(comment);

        Map<String, Object > dataMap = new HashMap<>();
        //set some details (some are common dada like status and statusReason and others not
        dataMap.put("amount", amount);
        dataMap.put("currency", currency);
        dataMap.put(STATUS,"myStatus");
        dataMap.put(STATUS_REASON, "myStatusReason");
        when(dataObjectInstanceVariableContainer.getData()).thenReturn(dataMap);

        return dataObjectInstanceVariableContainer;

    }
}
