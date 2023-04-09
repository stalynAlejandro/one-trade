package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.AdvanceDto;
import com.pagonxt.onetradefinance.external.backend.api.model.CurrencyDto;
import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.OperationDetailsDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.service.AccountService;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.List;

/**
 * This class has methods to convert DTO's into entities and viceversa,
 * for Export Collection Request(CLE_C001)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer
 * @see com.pagonxt.onetradefinance.integrations.service.AccountService
 * @see com.pagonxt.onetradefinance.integrations.service.RiskLineService
 * @see com.pagonxt.onetradefinance.integrations.service.CustomerService
 * @see java.text.DateFormat
 * @since jdk-11.0.13
 */
@Component
public class ExportCollectionRequestDtoSerializer {

    private final DateFormat dateTimeFormat;

    private final RiskLineDtoSerializer riskLineDtoSerializer;
    private final AccountDtoSerializer accountDtoSerializer;
    private final CustomerDtoSerializer customerDtoSerializer;

    private final DocumentationDtoSerializer documentationDtoSerializer;
    private final AccountService accountService;
    private final CustomerService customerService;

    /**
     * Class constructor
     * @param dateFormatProperties In this case, we use this object to get the date format
     * @param riskLineDtoSerializer to convert riskLineDto into an entity and viceversa
     * @param accountDtoSerializer to convert accountDto into an entity and viceversa
     * @param customerDtoSerializer to convert customerDto into an entity and viceversa
     * @param documentationDtoSerializer to convert documentationDto into an entity and viceversa
     * @param accountService Service that provides necessary functionality with accounts
     * @param customerService Service that provides necessary functionality with customer
     */
    @SuppressWarnings("java:S107") // Constructor has more than 7 parameters. This is necessary.
    public ExportCollectionRequestDtoSerializer(DateFormatProperties dateFormatProperties,
                                                RiskLineDtoSerializer riskLineDtoSerializer,
                                                AccountDtoSerializer accountDtoSerializer,
                                                CustomerDtoSerializer customerDtoSerializer,
                                                DocumentationDtoSerializer documentationDtoSerializer,
                                                AccountService accountService,
                                                CustomerService customerService) {
        dateTimeFormat = dateFormatProperties.getDateFormatInstance();

        this.riskLineDtoSerializer = riskLineDtoSerializer;
        this.accountDtoSerializer = accountDtoSerializer;
        this.customerDtoSerializer = customerDtoSerializer;
        this.documentationDtoSerializer = documentationDtoSerializer;
        this.accountService = accountService;
        this.customerService = customerService;
    }

    /**
     * Method to convert from Dto object to entity object
     * @param from ExportCollectionAdvanceRequestDto Object
     * @see ExportCollectionRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return ExportCollectionRequest object
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    public ExportCollectionRequest toModel(ExportCollectionRequestDto from) throws DateFormatException {
        if (from == null) {
            return null;
        }
        ExportCollectionRequest to = new ExportCollectionRequest();
        injectFields(from, to);
        return to;
    }

    /**
     * Method to inject fields from Dto object to entity object
     * @param from ExportCollectionRequestDto Object
     * @param to ExportCollectionRequest Object
     * @see ExportCollectionRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    protected void injectFields(ExportCollectionRequestDto from,
                                ExportCollectionRequest to)  throws DateFormatException {
        to.setCode(from.getCode());
        to.setContractReference(from.getContractReference());
        if(Strings.isNotBlank(from.getSlaEnd())) {
            try {
                to.setSlaEnd(dateTimeFormat.parse(from.getSlaEnd()));
            } catch (ParseException e) {
                throw new DateFormatException(from.getSlaEnd(), e);
            }
        }
        to.setSavedStep(from.getSavedStep());
        to.setCustomer(customerDtoSerializer.toModel(from.getCustomer()));
        if (from.getDocumentation() != null) {
            injectDocumentation(to, from.getDocumentation());
        }
        if (from.getAdvance() != null) {
            injectFinance(to, from.getAdvance());
        }
        if (from.getOperationDetails() != null) {
            injectDetails(to, from.getOperationDetails());
        }
    }

    /**
     * Method to inject documents from Dto class to entity object
     * @param to ExportCollectionRequest Object
     * @param documentation DocumentationDto with documents
     * @see com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    private void injectDocumentation(ExportCollectionRequest to,
                                     DocumentationDto documentation) throws DateFormatException {
        List<Document> documents = documentationDtoSerializer.mapDocumentation(documentation);
        to.setDocuments(documents);
        to.setPriority(documentation.getPriority());
        to.setClientAcceptance(documentation.isClientAcceptance());
    }

    /**
     * Method to inject finance into entity object
     * @param finance AdvanceDto Object
     * @param to ExportCollectionRequest Object
     * @see com.pagonxt.onetradefinance.external.backend.api.model.AdvanceDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     */
    private void injectFinance(ExportCollectionRequest to, AdvanceDto finance) throws DateFormatException {
        if(finance.isNotEmpty()) {
            to.setApplyingForAdvance(true);
        }
        to.setAdvanceRiskLine(riskLineDtoSerializer.toModel(finance.getRiskLine()));
        to.setAdvanceAmount(finance.getAdvanceAmount());
        if (finance.getAdvanceCurrency() != null) {
            to.setAdvanceCurrency(finance.getAdvanceCurrency().getId());
        }
        if(Strings.isNotBlank(finance.getAdvanceExpiration())) {
            try {
                to.setAdvanceExpiration(dateTimeFormat.parse(finance.getAdvanceExpiration()));
            } catch (ParseException e) {
                throw new DateFormatException(finance.getAdvanceExpiration(), e);
            }
        }
    }

    /**
     * Method to inject details into entity object
     * @param details OperationDetailsDto Object
     * @param to ExportCollectionRequest Object
     * @see OperationDetailsDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     */
    private void injectDetails(ExportCollectionRequest to, OperationDetailsDto details) {
        to.setAmount(details.getCollectionAmount());
        to.setCustomerHasAccount(details.isHasAccount());
        to.setNominalAccount(accountDtoSerializer.toModel(details.getClientAccount()));
        to.setCommissionAccount(accountDtoSerializer.toModel(details.getCommissionAccount()));
        if (details.getCollectionCurrency() != null) {
            to.setCurrency(details.getCollectionCurrency().getId());
        }
        to.setClientReference(details.getClientReference());
        to.setDebtorName(details.getDebtorName());
        to.setDebtorBank(details.getDebtorBank());
        to.setCollectionType(details.getCollectionType());
        to.setOffice(details.getOffice());
        to.setComment(details.getComments());
    }

    /**
     * Method to convert from entity object to dto object
     * @param from ExportCollectionAdvanceRequest Object
     * @see ExportCollectionRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @return ExportCollectionRequestDto object
     */
    public ExportCollectionRequestDto toDto(ExportCollectionRequest from) {
        if (from == null) {
            return null;
        }
        ExportCollectionRequestDto to = new ExportCollectionRequestDto();
        injectFields(from, to);
        return to;
    }

    /**
     * Method to inject fields from entity object to Dto object
     * @param to ExportCollectionRequestDto Object
     * @param from ExportCollectionRequest Object
     * @see ExportCollectionRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    protected void injectFields(ExportCollectionRequest from, ExportCollectionRequestDto to) {
        to.setCode(from.getCode());
        to.setContractReference(from.getContractReference());
        if(from.getSlaEnd() != null) {
            to.setSlaEnd(dateTimeFormat.format(from.getSlaEnd()));
        }
        to.setSavedStep(from.getSavedStep());
        if(from.getCustomer() != null) {
            to.setCustomer(customerDtoSerializer.toDto(customerService.
                    getCustomerByPersonNumber(from.getCustomer().getPersonNumber())));
        }
        to.setDocumentation(documentationDtoSerializer.mapDocumentationDto(from));
        to.getDocumentation().setClientAcceptance(from.isClientAcceptance());
        to.setAdvance(new AdvanceDto());
        if (from.isApplyingForAdvance()) {
            to.getAdvance().setRiskLine(riskLineDtoSerializer.toDto(from.getAdvanceRiskLine()));
            to.getAdvance().setAdvanceAmount(from.getAdvanceAmount());
            if (from.getAdvanceCurrency() != null) {
                to.getAdvance().setAdvanceCurrency(new CurrencyDto());
                to.getAdvance().getAdvanceCurrency().setId(from.getAdvanceCurrency());
                to.getAdvance().getAdvanceCurrency().setCurrency(from.getAdvanceCurrency());
            }
            if (from.getAdvanceExpiration() != null) {
                to.getAdvance().setAdvanceExpiration(dateTimeFormat.format(from.getAdvanceExpiration()));
            }
        }
        to.setOperationDetails(new OperationDetailsDto());
        to.getOperationDetails().setCollectionAmount(from.getAmount());
        to.getOperationDetails().setHasAccount(from.isCustomerHasAccount());
        if (from.getNominalAccount() != null) {
            to.getOperationDetails().setClientAccount(accountDtoSerializer.
                    toDto(accountService.getAccountById(from.getNominalAccount().getAccountId())));
        }
        if (from.getCommissionAccount() != null) {
            to.getOperationDetails().setCommissionAccount(accountDtoSerializer.
                    toDto(accountService.getAccountById(from.getCommissionAccount().getAccountId())));
        }
        if (from.getCurrency() != null) {
            to.getOperationDetails().setCollectionCurrency(new CurrencyDto());
            to.getOperationDetails().getCollectionCurrency().setId(from.getCurrency());
            to.getOperationDetails().getCollectionCurrency().setCurrency(from.getCurrency());
        }
        to.getOperationDetails().setClientReference(from.getClientReference());
        to.getOperationDetails().setDebtorName(from.getDebtorName());
        to.getOperationDetails().setDebtorBank(from.getDebtorBank());
        to.getOperationDetails().setCollectionType(from.getCollectionType());
        to.getOperationDetails().setOffice(from.getOffice());
        to.getOperationDetails().setComments(from.getComment());
        to.setDisplayedStatus(from.getDisplayedStatus());
    }

}
