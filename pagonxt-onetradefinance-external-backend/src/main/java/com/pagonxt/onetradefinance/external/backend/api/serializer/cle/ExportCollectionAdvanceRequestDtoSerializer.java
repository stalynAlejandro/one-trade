package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.CurrencyDto;
import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceRequestDetailsDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.ExchangeInsuranceDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class has methods to convert DTO's into entities and viceversa, for Export Collection Advance Request(CLE_C003)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.ExchangeInsuranceDtoSerializer
 * @see ExportCollectionDtoSerializer
 * @see com.pagonxt.onetradefinance.integrations.service.RiskLineService
 * @see com.pagonxt.onetradefinance.integrations.service.CustomerService
 * @see java.text.DateFormat
 * @since jdk-11.0.13
 */
@Component
public class ExportCollectionAdvanceRequestDtoSerializer {

    private final DateFormat dateTimeFormat;

    /**
     * Class attributes
     */
    private final RiskLineDtoSerializer riskLineDtoSerializer;
    private final CustomerDtoSerializer customerDtoSerializer;
    private final DocumentationDtoSerializer documentationDtoSerializer;
    private final ExportCollectionDtoSerializer exportCollectionDtoSerializer;
    private final ExchangeInsuranceDtoSerializer exchangeInsuranceDtoSerializer;
    private final CustomerService customerService;

    /**
     * Class constructor
     * @param dateFormatProperties In this case, we use this object to get the date format
     * @param riskLineDtoSerializer to convert riskLineDto into an entity and viceversa
     * @param customerDtoSerializer to convert customerDto into an entity and viceversa
     * @param documentationDtoSerializer to convert documentationDto into an entity and viceversa
     * @param exportCollectionDtoSerializer to convert exportCollectioneDto into an entity and viceversa
     * @param exchangeInsuranceDtoSerializer to convert exhangeInsuranceDto into an entity and viceversa
     * @param customerService Service that provides necessary functionality with customers
     */
    @SuppressWarnings("java:S107") // Constructor has more than 7 parameters. This is necessary.
    public ExportCollectionAdvanceRequestDtoSerializer(DateFormatProperties dateFormatProperties,
                                                       RiskLineDtoSerializer riskLineDtoSerializer,
                                                       CustomerDtoSerializer customerDtoSerializer,
                                                       DocumentationDtoSerializer documentationDtoSerializer,
                                                       ExportCollectionDtoSerializer exportCollectionDtoSerializer,
                                                       ExchangeInsuranceDtoSerializer exchangeInsuranceDtoSerializer,
                                                       CustomerService customerService) {
        dateTimeFormat = dateFormatProperties.getDateFormatInstance();
        this.riskLineDtoSerializer = riskLineDtoSerializer;
        this.customerDtoSerializer = customerDtoSerializer;
        this.documentationDtoSerializer = documentationDtoSerializer;
        this.exportCollectionDtoSerializer = exportCollectionDtoSerializer;
        this.exchangeInsuranceDtoSerializer = exchangeInsuranceDtoSerializer;
        this.customerService = customerService;
    }

    /**
     * Method to convert from Dto object to entity object
     * @param from ExportCollectionAdvanceRequestDto Object
     * @see ExportCollectionAdvanceRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return ExportCollectionAdvanceRequest object
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    public ExportCollectionAdvanceRequest toModel(ExportCollectionAdvanceRequestDto from) throws DateFormatException {
        if (from == null) {
            return null;
        }
        ExportCollectionAdvanceRequest to = new ExportCollectionAdvanceRequest();
        injectFields(from, to);
        return to;
    }

    /**
     * Method to inject fields from Dto object to entity object
     * @param from ExportCollectionAdvanceRequestDto Object
     * @param to ExportCollectionAdvanceRequest Object
     * @see ExportCollectionAdvanceRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    protected void injectFields(ExportCollectionAdvanceRequestDto from,
                                ExportCollectionAdvanceRequest to)  throws DateFormatException {
        to.setCode(from.getCode());
        to.setSavedStep(from.getSavedStep());
        to.setContractReference(from.getContractReference());
        to.setCustomer(customerDtoSerializer.toModel(from.getCustomer()));
        if (from.getDocumentation() != null) {
            injectDocumentation(to, from.getDocumentation());
        }
        if (from.getRequest() != null) {
            injectDetails(to, from.getRequest());
        }
    }

    /**
     * Method to inject documents from Dto class to entity object
     * @param to ExportCollectionAdvanceRequest Object
     * @param documentation DocumentationDto with documents
     * @see com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    private void injectDocumentation(ExportCollectionAdvanceRequest to,
                                     DocumentationDto documentation) throws DateFormatException {
        List<Document> documents = documentationDtoSerializer.mapDocumentation(documentation);
        to.setDocuments(documents);
        to.setPriority(documentation.getPriority());
    }

    /**
     * Method to inject details into entity object
     * @param details ExportCollectionAdvanceRequestDetailsDto Object
     * @param to ExportCollectionAdvanceRequest Object
     * @see ExportCollectionAdvanceRequestDetailsDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     */
    private void injectDetails(ExportCollectionAdvanceRequest to, ExportCollectionAdvanceRequestDetailsDto details) {
        to.setExportCollection(exportCollectionDtoSerializer.toModel(details.getExportCollection()));
        to.setAmount(details.getAdvanceAmount());
        if (details.getAdvanceCurrency() != null) {
            to.setCurrency(details.getAdvanceCurrency().getId());
        }
        List<ExchangeInsurance> exchangeInsurances = new ArrayList<>();
        if (details.getExchangeInsurances() != null) {
            exchangeInsurances = details.getExchangeInsurances().stream().
                    map(exchangeInsuranceDtoSerializer::toModel).collect(Collectors.toList());
        }
        to.setExchangeInsurances(exchangeInsurances);

        // This fields could be editable by the user in the future
        injectExchangeInsuranceDetails(to, details);

        if (Strings.isNotBlank(details.getRequestExpiration())) {
            try {
                to.setExpiration(dateTimeFormat.parse(details.getRequestExpiration()));
            } catch (ParseException e) {
                throw new DateFormatException(details.getRequestExpiration(), e);
            }
        }
        to.setRiskLine(riskLineDtoSerializer.toModel(details.getRiskLine()));
        to.setOffice(details.getOffice());
        to.setComment(details.getComments());
    }

    /**
     * Method to inject Exchange Insurance details into entity object
     * @param details ExportCollectionAdvanceRequestDetailsDto Object
     * @param to ExportCollectionAdvanceRequest Object
     * @see ExportCollectionAdvanceRequestDetailsDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     */
    private void injectExchangeInsuranceDetails(ExportCollectionAdvanceRequest to,
                                                ExportCollectionAdvanceRequestDetailsDto details) {
        if (Strings.isNotBlank(details.getExchangeInsuranceUseDate())) {
            try {
                to.setExchangeInsuranceUseDate(dateTimeFormat.parse(details.getExchangeInsuranceUseDate()));
            } catch (ParseException e) {
                throw new DateFormatException(details.getExchangeInsuranceUseDate(), e);
            }
        } else {
            to.setExchangeInsuranceUseDate(new Date());
        }
        if(details.getExchangeInsuranceAmountToUse() != null) {
            to.setExchangeInsuranceAmountToUse(Double.parseDouble(details.getExchangeInsuranceAmountToUse()));
        } else {
            to.setExchangeInsuranceAmountToUse(to.getAmount());
        }
        String exchangeInsuranceBuyCurrency = details.getExchangeInsuranceBuyCurrency() != null ? details.
                getExchangeInsuranceBuyCurrency() : to.getCurrency();
        to.setExchangeInsuranceBuyCurrency(exchangeInsuranceBuyCurrency);
        String exchangeInsuranceSellCurrency = null;
        if(details.getExchangeInsuranceSellCurrency() != null) {
            exchangeInsuranceSellCurrency = details.getExchangeInsuranceSellCurrency();
        } else if(to.getExportCollection() != null && to.getExportCollection().getNominalAccount() != null) {
            exchangeInsuranceSellCurrency = to.getExportCollection().getNominalAccount().getCurrency();
        }
        to.setExchangeInsuranceSellCurrency(exchangeInsuranceSellCurrency);
    }

    /**
     * Method to convert from entity object to dto object
     * @param from ExportCollectionAdvanceRequest Object
     * @see ExportCollectionAdvanceRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @return ExportCollectionAdvanceRequestDto object
     */
    public ExportCollectionAdvanceRequestDto toDto(ExportCollectionAdvanceRequest from) {
        if (from == null) {
            return null;
        }
        ExportCollectionAdvanceRequestDto to = new ExportCollectionAdvanceRequestDto();
        injectFields(from, to);
        return to;
    }

    /**
     * Method to inject fields from entity object to Dto object
     * @param to ExportCollectionAdvanceRequestDto Object
     * @param from ExportCollectionAdvanceRequest Object
     * @see ExportCollectionAdvanceRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    protected void injectFields(ExportCollectionAdvanceRequest from, ExportCollectionAdvanceRequestDto to) {
        to.setCode(from.getCode());
        to.setSavedStep(from.getSavedStep());
        to.setContractReference(from.getContractReference());
        if(from.getCustomer() != null) {
            to.setCustomer(customerDtoSerializer.toDto(customerService.
                    getCustomerByPersonNumber(from.getCustomer().getPersonNumber())));
        }
        to.setDocumentation(documentationDtoSerializer.mapDocumentationDto(from));
        to.setRequest(new ExportCollectionAdvanceRequestDetailsDto());
        to.getRequest().setExportCollection(exportCollectionDtoSerializer.toDto(from.getExportCollection()));
        to.getRequest().setAdvanceAmount(from.getAmount());
        if (from.getCurrency() != null) {
            to.getRequest().setAdvanceCurrency(new CurrencyDto());
            to.getRequest().getAdvanceCurrency().setId(from.getCurrency());
            to.getRequest().getAdvanceCurrency().setCurrency(from.getCurrency());
        }
        List<ExchangeInsuranceDto> exchangeInsurances = new ArrayList<>();
        if (from.getExchangeInsurances() != null) {
            exchangeInsurances = from.getExchangeInsurances().stream().
                    map(exchangeInsuranceDtoSerializer::toDto).collect(Collectors.toList());
        }
        to.getRequest().setExchangeInsurances(exchangeInsurances);

        // This fields could be editable by the user in the future
        if (from.getExchangeInsuranceUseDate() != null) {
            to.getRequest().setExchangeInsuranceUseDate(dateTimeFormat.format(from.getExchangeInsuranceUseDate()));
        }
        if(from.getExchangeInsuranceAmountToUse() != null) {
            to.getRequest().setExchangeInsuranceAmountToUse(from.getExchangeInsuranceAmountToUse().toString());
        }
        to.getRequest().setExchangeInsuranceBuyCurrency(from.getExchangeInsuranceBuyCurrency());
        to.getRequest().setExchangeInsuranceSellCurrency(from.getExchangeInsuranceSellCurrency());

        if (from.getExpiration() != null) {
            to.getRequest().setRequestExpiration(dateTimeFormat.format(from.getExpiration()));
        }
        if (from.getRiskLine() != null) {
            to.getRequest().setRiskLine(riskLineDtoSerializer.toDto(from.getRiskLine()));
        }
        to.getRequest().setOffice(from.getOffice());
        to.getRequest().setComments(from.getComment());
        to.setDisplayedStatus(from.getDisplayedStatus());
    }

}
