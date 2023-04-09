package com.pagonxt.onetradefinance.work.service.mapper.trade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flowable.content.engine.impl.persistence.entity.ContentItemEntity;
import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.flowable.dataobject.engine.impl.runtime.DataObjectInstanceVariableContainerImpl;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.work.common.CaseCommonVariablesHelper;
import com.pagonxt.onetradefinance.work.service.mapper.OperationDocumentMapper;
import com.pagonxt.onetradefinance.work.utils.PagoNxtRequestUtils;
import com.pagonxt.onetradefinance.work.utils.trade.TradeUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * Class with some methods to serialize and deserialize data
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class TradeRequestMapper {
    private final TradeContractMapper tradeContractMapper;
    private final CaseCommonVariablesHelper caseCommonVariablesHelper;
    private final OperationDocumentMapper operationDocumentMapper;
    private final PagoNxtRequestUtils pagoNxtRequestUtils;

    private static final String[] OPERATION_COMMON_DATA = {REQUEST_CODE, CREATION_DATE, REQUEST_PRIORITY,
            COUNTRY,DISPLAYED_STATUS, REQUEST_COMMENTS, REQUEST_OFFICE, REQUEST_MIDDLE_OFFICE,
            REQUEST_CUSTOMER_FULL_NAME, REQUEST_CUSTOMER_ID, REQUEST_CUSTOMER_CODE, REQUEST_CUSTOMER_SEGMENT,
            REQUEST_CUSTOMER_EMAIL, RESOLUTION, RESOLUTION_REASON,
            REQUESTER_TYPE, REQUESTER_DISPLAYED_NAME, REQUESTER_ID};

    public TradeRequestMapper(TradeContractMapper tradeContractMapper,
                              CaseCommonVariablesHelper caseCommonVariablesHelper,
                              OperationDocumentMapper operationDocumentMapper,
                              PagoNxtRequestUtils pagoNxtRequestUtils) {
        this.tradeContractMapper = tradeContractMapper;
        this.caseCommonVariablesHelper = caseCommonVariablesHelper;
        this.operationDocumentMapper = operationDocumentMapper;
        this.pagoNxtRequestUtils = pagoNxtRequestUtils;
    }
    /**
     * Method to convert a map of case instance variables to a TradeRequest object
     * @param caseInstanceVariables     : a Map of case instance variables
     * @param userType                  : a String with the user type
     * @return                          : a {@link TradeRequest} object
     * @throws JsonProcessingException  : handles Json Processing exceptions
     */
    public TradeRequest mapToTradeRequest(Map<String, Object> caseInstanceVariables,
                                                         String userType) throws JsonProcessingException {
        TradeRequest request = new TradeRequest();
        request.setCode((String) caseInstanceVariables.get(TradeUtils.CASE_VAR_OPERATION_CODE));
        DataObjectInstanceVariableContainerImpl operation =
                (DataObjectInstanceVariableContainerImpl) caseInstanceVariables.get(OPERATION);

        request.setCreationDate(operation.getDate(CREATION_DATE));

        request.setProduct((String) caseInstanceVariables.get(PRODUCT));
        request.setEvent((String) caseInstanceVariables.get(EVENT));
        request.setCountry(operation.getString(COUNTRY));
        request.setPriority(operation.getString(REQUEST_PRIORITY));
        request.setOffice(operation.getString(REQUEST_OFFICE));
        request.setMiddleOffice(operation.getString(REQUEST_MIDDLE_OFFICE));
        request.setComment(operation.getString(REQUEST_COMMENTS));
        request.setDisplayedStatus(operation.getString(DISPLAYED_STATUS));
        request.setSavedStep((Integer) caseInstanceVariables.get(REQUEST_SAVED_STEP));
        //Customer
        Customer customer = new Customer();
        customer.setTaxId(operation.getString(REQUEST_CUSTOMER_ID));
        customer.setName(operation.getString(REQUEST_CUSTOMER_FULL_NAME));
        customer.setPersonNumber(operation.getString(REQUEST_CUSTOMER_CODE));
        customer.setSegment(operation.getString(REQUEST_CUSTOMER_SEGMENT));
        customer.setEmail(operation.getString(REQUEST_CUSTOMER_EMAIL));
        request.setCustomer(customer);
        //Requester
        User user = new User();
        user.setUserId(operation.getString(REQUESTER_ID));
        user.setUserDisplayedName(operation.getString(REQUESTER_DISPLAYED_NAME));
        user.setUserType(operation.getString(REQUESTER_TYPE));
        request.setRequester(new UserInfo(user));
        request.setResolution(operation.getString(RESOLUTION));
        request.setResolutionReason(operation.getString(RESOLUTION_REASON));
        // Documents
        List<Document> documents;
        if (VALUE_DRAFT.equals(operation.getString(DISPLAYED_STATUS))) {
            documents = caseCommonVariablesHelper.convertToDocumentList(caseInstanceVariables.get(REQUEST_DOCUMENTS));
        } else {
            documents = operationDocumentMapper
                    .mapOperationDocuments((List<ContentItemEntity>) caseInstanceVariables.get(OPERATION_DOCUMENTS));
        }
        // Filter documents by visibility
        documents = documents.stream().filter(d -> pagoNxtRequestUtils.isVisible(d.getDocumentId(), userType))
                .collect(Collectors.toList());
        request.setDocuments(documents);

        //Details are operation caseInstanceVariables that are not common for all products and events
        Map<String, Object> detailsMap = getSimplifiedMap(operation.getData());
        //Remove some operation fields that are ommon for all products and events
        detailsMap.keySet().removeAll(Arrays.asList(OPERATION_COMMON_DATA));
        request.setDetails(detailsMap);
        return request;
    }
    private Map<String, Object> getSimplifiedMap (Map<String, Object> dataMap){
        Map<String, Object> simplifiedMap = new HashMap<>();
        // Iterate over the HashMap
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            if (entry.getValue() instanceof DataObjectInstanceVariableContainer){
                //simplify value, getting only map data
                simplifiedMap.put(entry.getKey()
                        ,  tradeContractMapper
                                .mapToTradeContract(((DataObjectInstanceVariableContainerImpl)entry.getValue())));
            }else {
                simplifiedMap.put(entry.getKey(), entry.getValue());
            }
        }
        return simplifiedMap;
    }
}
