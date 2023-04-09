package com.pagonxt.onetradefinance.external.backend.api.serializer.cli;

import com.pagonxt.onetradefinance.external.backend.api.model.*;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.integrations.service.AccountService;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.*;
import static com.pagonxt.onetradefinance.integrations.constants.TradeConstants.EVENT_REQUEST;
import static com.pagonxt.onetradefinance.integrations.constants.TradeConstants.PRODUCT_IMPORT_COLLECTION;
import static com.pagonxt.onetradefinance.integrations.util.ParseUtils.parseBooleanToYesNo;
import static com.pagonxt.onetradefinance.integrations.util.ParseUtils.parseYesNoToBoolean;

/**
 * This class has methods to convert DTOs into entities and vice-versa,
 * for Import Collection Request(CLE_C001)
 * @author -
 * @version jdk-11.0.13
 * @see CustomerDtoSerializer
 * @see RiskLineDtoSerializer
 * @see AccountDtoSerializer
 * @see DocumentationDtoSerializer
 * @see AccountService
 * @see com.pagonxt.onetradefinance.integrations.service.RiskLineService
 * @see CustomerService
 * @see DateFormat
 * @since jdk-11.0.13
 */
@Component
public class ImportCollectionRequestDtoSerializer implements TradeRequestDtoSerializer {

    private final DateFormat dateTimeFormat;
    private final AccountDtoSerializer accountDtoSerializer;
    private final CustomerDtoSerializer customerDtoSerializer;

    private final DocumentationDtoSerializer documentationDtoSerializer;
    private final AccountService accountService;
    private final CustomerService customerService;

    /**
     * Class constructor
     * @param dateFormatProperties In this case, we use this object to get the date format
     * @param accountDtoSerializer to convert accountDto into an entity and vice-versa
     * @param customerDtoSerializer to convert customerDto into an entity and vice-versa
     * @param documentationDtoSerializer to convert documentationDto into an entity and vice-versa
     * @param accountService Service that provides necessary functionality with accounts
     * @param customerService Service that provides necessary functionality with customer
     */
    @SuppressWarnings("java:S107") // Constructor has more than 7 parameters. This is necessary.
    public ImportCollectionRequestDtoSerializer(DateFormatProperties dateFormatProperties,
                                                AccountDtoSerializer accountDtoSerializer,
                                                CustomerDtoSerializer customerDtoSerializer,
                                                DocumentationDtoSerializer documentationDtoSerializer,
                                                AccountService accountService,
                                                CustomerService customerService) {
        dateTimeFormat = dateFormatProperties.getDateFormatInstance();

        this.accountDtoSerializer = accountDtoSerializer;
        this.customerDtoSerializer = customerDtoSerializer;
        this.documentationDtoSerializer = documentationDtoSerializer;
        this.accountService = accountService;
        this.customerService = customerService;
    }

    /**
     * Method to convert from Dto object to entity object
     * @param from ImportCollectionAdvanceRequestDto Object
     * @see ImportCollectionRequestDto
     * @see TradeRequest
     * @return ImportCollectionRequest object
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    public TradeRequest toModel(CommonRequestDto from) throws DateFormatException {
        if (from == null) {
            return null;
        }
        TradeRequest to = new TradeRequest();
        injectFields((ImportCollectionRequestDto) from, to);
        return to;
    }

    /**
     * Method to inject fields from Dto object to entity object
     * @param from ImportCollectionRequestDto Object
     * @param to ImportCollectionRequest Object
     * @see ImportCollectionRequestDto
     * @see TradeRequest
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    protected void injectFields(ImportCollectionRequestDto from,
                                TradeRequest to)  throws DateFormatException {

        // Form Data
        // 1 - Customer
        to.setCustomer(customerDtoSerializer.toModel(from.getCustomer()));
        // 2 - Details
        Map<String, Object> detailsMap = new HashMap<>();
        to.setDetails(detailsMap);
        if(from.getOperationDetails() != null) {
            injectDetails(detailsMap, from.getOperationDetails());
            to.setOffice(from.getOperationDetails().getOffice());
            to.setComment(from.getOperationDetails().getComments());
        }
        // 3 - Documentation
        DocumentationDto documentation = from.getDocumentation();
        if (documentation != null) {
            injectDocumentation(to, documentation);
            detailsMap.put(REQUEST_CUSTOMER_PRICES_AGREEMENT_COLLECTED,
                    parseBooleanToYesNo(documentation.isClientAcceptance()));
        }
        // Internal Data
        to.setCode(from.getCode());
        to.setProduct(PRODUCT_IMPORT_COLLECTION);
        to.setEvent(EVENT_REQUEST);
        if(Strings.isNotBlank(from.getSlaEnd())) {
            try {
                to.setSlaEnd(dateTimeFormat.parse(from.getSlaEnd()));
            } catch (ParseException e) {
                throw new DateFormatException(from.getSlaEnd(), e);
            }
        }
        to.setSavedStep(from.getSavedStep());
        detailsMap.put(CONTRACT_REFERENCE, from.getContractReference());
    }

    private static void injectDetails(Map<String, Object> detailsMap,
                                                     OperationDetailsDto operationDetails) {
        detailsMap.put(REQUEST_CUSTOMER_HAS_ACCOUNT, parseBooleanToYesNo(operationDetails.isHasAccount()));

        AccountDto nominalAccount = operationDetails.getClientAccount();
        if (nominalAccount != null) {
            detailsMap.put(REQUEST_NOMINAL_ACCOUNT_CURRENCY, nominalAccount.getCurrency());
            detailsMap.put(REQUEST_NOMINAL_ACCOUNT_IBAN, nominalAccount.getIban());
            detailsMap.put(REQUEST_NOMINAL_ACCOUNT_ID, nominalAccount.getId());
        }

        AccountDto commissionAccount = operationDetails.getCommissionAccount();
        if (commissionAccount != null) {
            detailsMap.put(REQUEST_COMMISSION_ACCOUNT_CURRENCY, commissionAccount.getCurrency());
            detailsMap.put(REQUEST_COMMISSION_ACCOUNT_IBAN, commissionAccount.getIban());
            detailsMap.put(REQUEST_COMMISSION_ACCOUNT_ID, commissionAccount.getId());
        }

        detailsMap.put(REQUEST_AMOUNT, operationDetails.getCollectionAmount());
        if (operationDetails.getCollectionCurrency() != null) {
            detailsMap.put(REQUEST_CURRENCY, operationDetails.getCollectionCurrency().getCurrency());
        }

        detailsMap.put(REQUEST_COLLECTION_TYPE, operationDetails.getCollectionType());
        detailsMap.put(REQUEST_CUSTOMER_REFERENCE, operationDetails.getClientReference());
        detailsMap.put(REQUEST_DEBTOR_NAME, operationDetails.getDebtorName());
        detailsMap.put(REQUEST_DEBTOR_BANK, operationDetails.getDebtorBank());
    }

    /**
     * Method to inject documents from Dto class to entity object
     * @param to ImportCollectionRequest Object
     * @param documentation DocumentationDto with documents
     * @see DocumentationDto
     * @see TradeRequest
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    private void injectDocumentation(TradeRequest to,
                                     DocumentationDto documentation) throws DateFormatException {
        List<Document> documents = documentationDtoSerializer.mapDocumentation(documentation);
        to.setDocuments(documents);
        to.setPriority(documentation.getPriority());
    }

    /**
     * Method to convert from entity object to dto object
     * @param from ImportCollectionAdvanceRequest Object
     * @see ImportCollectionRequestDto
     * @see TradeRequest
     * @return ImportCollectionRequestDto object
     */
    public ImportCollectionRequestDto toDto(TradeRequest from) {
        if (from == null) {
            return null;
        }
        ImportCollectionRequestDto to = new ImportCollectionRequestDto();
        injectFields(from, to);
        return to;
    }

    /**
     * Method to inject fields from entity object to Dto object
     * @param to ImportCollectionRequestDto Object
     * @param from ImportCollectionRequest Object
     * @see ImportCollectionRequestDto
     * @see TradeRequest
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    protected void injectFields(TradeRequest from, ImportCollectionRequestDto to) {
        // Form Data
        // 1 - Customer
        if(from.getCustomer() != null) {
            to.setCustomer(customerDtoSerializer.toDto(customerService.
                    getCustomerByPersonNumber(from.getCustomer().getPersonNumber())));
        }
        // 2 - Details
        Map<String, Object> detailsMap = from.getDetails();
        if (detailsMap != null) {
            OperationDetailsDto operationDetailsDto = injectDetails(from, detailsMap);
            to.setOperationDetails(operationDetailsDto);
        }
        // 3 - Documentation
        to.setDocumentation(documentationDtoSerializer.mapDocumentationTradeDto(from));
        if (detailsMap != null) {
            to.getDocumentation().setClientAcceptance(parseYesNoToBoolean(
                    (String) detailsMap.get(REQUEST_CUSTOMER_PRICES_AGREEMENT_COLLECTED)));
        }
        // Internal Data
        if (detailsMap != null) {
            to.setContractReference((String) detailsMap.get(CONTRACT_REFERENCE));
        }
        to.setCode(from.getCode());
        if(from.getSlaEnd() != null) {
            to.setSlaEnd(dateTimeFormat.format(from.getSlaEnd()));
        }
        to.setSavedStep(from.getSavedStep());
        to.setDisplayedStatus(from.getDisplayedStatus());
    }

    private OperationDetailsDto injectDetails(TradeRequest from, Map<String, Object> detailsMap) {
        OperationDetailsDto operationDetails = new OperationDetailsDto();

        operationDetails.setHasAccount(parseYesNoToBoolean((String) detailsMap.get(REQUEST_CUSTOMER_HAS_ACCOUNT)));

        String nominalAccountId = (String) detailsMap.get(REQUEST_NOMINAL_ACCOUNT_ID);
        if (nominalAccountId != null) {
            AccountDto nominalAccount = accountDtoSerializer.toDto(accountService.getAccountById(nominalAccountId));
            operationDetails.setClientAccount(nominalAccount);
        }

        String commissionAccountId = (String) detailsMap.get(REQUEST_COMMISSION_ACCOUNT_ID);
        if (commissionAccountId != null) {
            AccountDto commissionAccount = accountDtoSerializer.toDto(accountService.getAccountById(commissionAccountId));
            operationDetails.setCommissionAccount(commissionAccount);
        }

        operationDetails.setCollectionAmount((Double) detailsMap.get(REQUEST_AMOUNT));
        String currency = (String) detailsMap.get(REQUEST_CURRENCY);
        if (currency != null) {
            CurrencyDto currencyDto = new CurrencyDto();
            currencyDto.setId(currency);
            currencyDto.setCurrency(currency);
            operationDetails.setCollectionCurrency(currencyDto);
        }
        operationDetails.setCollectionType((String) detailsMap.get(REQUEST_COLLECTION_TYPE));
        operationDetails.setClientReference((String) detailsMap.get(REQUEST_CUSTOMER_REFERENCE));
        operationDetails.setDebtorName((String) detailsMap.get(REQUEST_DEBTOR_NAME));
        operationDetails.setDebtorBank((String) detailsMap.get(REQUEST_DEBTOR_BANK));
        operationDetails.setOffice(from.getOffice());
        operationDetails.setComments(from.getComment());

        return operationDetails;
    }

}
