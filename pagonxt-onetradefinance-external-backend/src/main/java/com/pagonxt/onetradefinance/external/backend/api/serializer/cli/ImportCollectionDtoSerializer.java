package com.pagonxt.onetradefinance.external.backend.api.serializer.cli;

import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeContract;
import com.pagonxt.onetradefinance.integrations.service.AccountService;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;

/**
 * This class has methods to convert DTO's into entities and viceversa, for Import Collection
 * @author -
 * @version jdk-11.0.13
 * @see CustomerDtoSerializer
 * @see AccountDtoSerializer
 * @see ImportCollectionDtoSerializer
 * @see CustomerService
 * @see AccountService
 * @since jdk-11.0.13
 */
@Component
public class ImportCollectionDtoSerializer {

    /**
     * Class attributes
     */
    private final CustomerDtoSerializer customerDtoSerializer;

    private final CustomerService customerService;

    private final AccountDtoSerializer accountDtoSerializer;

    private final AccountService accountService;

    private final DateFormat dateTimeFormat;

    /**
     * Class constructor
     * @param dateFormatProperties In this case, we use this object to get the date format
     * @param customerDtoSerializer o convert customerDto into an entity and viceversa
     * @param customerService Service that provides necessary functionality with customers
     * @param accountDtoSerializer o convert accountDto into an entity and viceversa
     * @param accountService Service that provides necessary functionality with accounts
     */
    public ImportCollectionDtoSerializer(DateFormatProperties dateFormatProperties,
                                         CustomerDtoSerializer customerDtoSerializer,
                                         CustomerService customerService,
                                         AccountDtoSerializer accountDtoSerializer,
                                         AccountService accountService) {
        dateTimeFormat = dateFormatProperties.getDateFormatInstance();

        this.customerDtoSerializer = customerDtoSerializer;
        this.customerService = customerService;
        this.accountDtoSerializer = accountDtoSerializer;
        this.accountService = accountService;
    }

    /**
     * Method to convert from entity object to dto object
     * @param tradeContract TradeContract object
     * @see ImportCollectionDto
     * @see TradeContract
     * @return ImportCollectionDto object
     */
    public ImportCollectionDto toDto(TradeContract tradeContract) {
        if(tradeContract == null) {
            return null;
        }
        ImportCollectionDto importCollectionDto = new ImportCollectionDto();
        importCollectionDto.setCode(tradeContract.getCode());
        if(tradeContract.getCustomer() != null) {
            importCollectionDto.setCustomer(customerDtoSerializer.toDto(tradeContract.getCustomer()));
        }
        if (tradeContract.getCreationDate() != null) {
            importCollectionDto.setCreationDate(dateTimeFormat.format(tradeContract.getCreationDate()));
        }
        if (tradeContract.getApprovalDate() != null) {
            importCollectionDto.setApprovalDate(dateTimeFormat.format(tradeContract.getApprovalDate()));
        }
        importCollectionDto.setContractReference(tradeContract.getContractReference());
        injectDetails(importCollectionDto, tradeContract.getDetails());
        return importCollectionDto;
    }

    /**
     * Method to convert from entity object to dto object
     * @param importCollectionMap Map with the importCollection variables
     * @see ImportCollectionDto
     * @return ImportCollectionDto object
     */
    @SuppressWarnings("unchecked")
    public ImportCollectionDto toDto(Map<String, Object> importCollectionMap) {
        if(importCollectionMap == null) {
            return null;
        }
        ImportCollectionDto importCollectionDto = new ImportCollectionDto();
        importCollectionDto.setCode((String) importCollectionMap.get(REQUEST_CODE));
        Map<String, Object> customer = (Map<String, Object>) importCollectionMap.get(REQUEST_CUSTOMER);
        if(customer != null) {
            importCollectionDto.setCustomer(customerDtoSerializer.toDto(
                    customerService.getCustomerByPersonNumber((String) customer.get(PERSON_NUMBER))));
        }
        importCollectionDto.setApprovalDate((String) importCollectionMap.get(APPROVAL_DATE));
        importCollectionDto.setCreationDate((String) importCollectionMap.get(CREATION_DATE));
        importCollectionDto.setContractReference((String) importCollectionMap.get(CONTRACT_REFERENCE));
        injectDetails(importCollectionDto, (Map<String, Object>) importCollectionMap.get(DETAILS));
        return importCollectionDto;
    }

    private void injectDetails(ImportCollectionDto importCollectionDto, Map<String, Object> details) {
        if(details == null) {
            return;
        }
        Double amount = (Double) details.get(REQUEST_AMOUNT);
        if (amount != null) {
            importCollectionDto.setAmount(amount.toString());
        }
        importCollectionDto.setCurrency((String) details.get(REQUEST_CURRENCY));
        String nominalAccountId = (String) details.get(REQUEST_NOMINAL_ACCOUNT_ID);
        if(nominalAccountId != null) {
            importCollectionDto.setNominalAccount(accountDtoSerializer.toDto
                    (accountService.getAccountById(nominalAccountId)));
        }
    }
}
