package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceModificationRequestDetailsDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class has methods to convert DTO's into entities and viceversa,
 * for Export Collection Advance Modification Request(CLE_C004)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer
 * @see ExportCollectionAdvanceDtoSerializer
 * @see com.pagonxt.onetradefinance.integrations.service.RiskLineService
 * @see com.pagonxt.onetradefinance.integrations.service.CustomerService
 * @see java.text.DateFormat
 * @since jdk-11.0.13
 */
@Component
public class ExportCollectionAdvanceModificationRequestDtoSerializer {

    /**
     * Class attributes
     */
    private final RiskLineDtoSerializer riskLineDtoSerializer;
    private final CustomerDtoSerializer customerDtoSerializer;
    private final DocumentationDtoSerializer documentationDtoSerializer;
    private final ExportCollectionAdvanceDtoSerializer exportCollectionAdvanceDtoSerializer;
    private final CustomerService customerService;

    /**
     * Class constructor
     * @param riskLineDtoSerializer to convert riskLineDto into an entity and viceversa
     * @param customerDtoSerializer to convert customerDto into an entity and viceversa
     * @param documentationDtoSerializer to convert documentationDto into an entity and viceversa
     * @param exportCollectionAdvanceDtoSerializer to convert exportCollectionAdvanceDto into an entity and viceversa
     * @param customerService Service that provides necessary functionality with customers
     */
    public ExportCollectionAdvanceModificationRequestDtoSerializer
    (RiskLineDtoSerializer riskLineDtoSerializer,
       CustomerDtoSerializer customerDtoSerializer,
       DocumentationDtoSerializer documentationDtoSerializer,
       ExportCollectionAdvanceDtoSerializer exportCollectionAdvanceDtoSerializer,
       CustomerService customerService) {
        this.riskLineDtoSerializer = riskLineDtoSerializer;
        this.customerDtoSerializer = customerDtoSerializer;
        this.documentationDtoSerializer = documentationDtoSerializer;
        this.exportCollectionAdvanceDtoSerializer = exportCollectionAdvanceDtoSerializer;
        this.customerService = customerService;
    }

    /**
     * Method to convert from Dto object to entity object
     * @param from ExportCollectionAdvanceModificationRequestDto Object
     * @see ExportCollectionAdvanceModificationRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest
     * @return ExportCollectionAdvanceModificationRequest object
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    public ExportCollectionAdvanceModificationRequest toModel
    (ExportCollectionAdvanceModificationRequestDto from) throws DateFormatException {
        if (from == null) {
            return null;
        }
        ExportCollectionAdvanceModificationRequest to = new ExportCollectionAdvanceModificationRequest();
        injectFields(from, to);
        return to;
    }

    protected void injectFields(ExportCollectionAdvanceModificationRequestDto from,
                                ExportCollectionAdvanceModificationRequest to)  throws DateFormatException {
        to.setCode(from.getCode());
        to.setCustomer(customerDtoSerializer.toModel(from.getCustomer()));
        if (from.getDocumentation() != null) {
            injectDocumentation(to, from.getDocumentation());
        }
        if (from.getRequest() != null) {
            injectDetails(to, from.getRequest());
        }
    }

    private void injectDocumentation(ExportCollectionAdvanceModificationRequest to,
                                     DocumentationDto documentation) throws DateFormatException {
        List<Document> documents = documentationDtoSerializer.mapDocumentation(documentation);
        to.setDocuments(documents);
        to.setPriority(documentation.getPriority());
    }

    private void injectDetails(ExportCollectionAdvanceModificationRequest to,
                               ExportCollectionAdvanceModificationRequestDetailsDto details) {
                                to.setExportCollectionAdvance(exportCollectionAdvanceDtoSerializer.
                                        toModel(details.getExportCollectionAdvance()));
        to.setRiskLine(riskLineDtoSerializer.toModel(details.getRiskLine()));
        to.setOffice(details.getOffice());
        to.setComment(details.getComments());
    }

    /**
     * Method to convert from entity object to dto object
     * @param from ExportCollectionAdvanceModificationRequest Object
     * @see ExportCollectionAdvanceModificationRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest
     * @return ExportCollectionAdvanceCancellationRequest object
     */
    public ExportCollectionAdvanceModificationRequestDto toDto(ExportCollectionAdvanceModificationRequest from) {
        if (from == null) {
            return null;
        }
        ExportCollectionAdvanceModificationRequestDto to = new ExportCollectionAdvanceModificationRequestDto();
        injectFields(from, to);
        return to;
    }

    protected void injectFields(ExportCollectionAdvanceModificationRequest from,
                                ExportCollectionAdvanceModificationRequestDto to) {
        to.setCode(from.getCode());
        if(from.getCustomer() != null) {
            to.setCustomer(customerDtoSerializer.toDto(customerService.
                    getCustomerByPersonNumber(from.getCustomer().getPersonNumber())));
        }
        to.setDocumentation(documentationDtoSerializer.mapDocumentationDto(from));
        to.setRequest(new ExportCollectionAdvanceModificationRequestDetailsDto());
        to.getRequest().setExportCollectionAdvance(exportCollectionAdvanceDtoSerializer.
                toDto(from.getExportCollectionAdvance()));
        if (from.getRiskLine() != null) {
            to.getRequest().setRiskLine(riskLineDtoSerializer.toDto(from.getRiskLine()));
        }
        to.getRequest().setOffice(from.getOffice());
        to.getRequest().setComments(from.getComment());
        to.setDisplayedStatus(from.getDisplayedStatus());
    }
}
