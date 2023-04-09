package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceCancellationRequestDetailsDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceCancellationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class has methods to convert DTO's into entities and viceversa,
 * for Export Collection Advance Cancellation Request(CLE_C005)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer
 * @see ExportCollectionAdvanceDtoSerializer
 * @see com.pagonxt.onetradefinance.integrations.service.CustomerService
 * @since jdk-11.0.13
 */
@Component
public class ExportCollectionAdvanceCancellationRequestDtoSerializer {

    /**
     * Class attributes
     */
    private final CustomerDtoSerializer customerDtoSerializer;

    private final DocumentationDtoSerializer documentationDtoSerializer;

    private final ExportCollectionAdvanceDtoSerializer exportCollectionAdvanceDtoSerializer;
    private final CustomerService customerService;

    /**
     * Class constructor
     * @param customerDtoSerializer to convert customerDto into an entity and viceversa
     * @param documentationDtoSerializer to convert documentationDto into an entity and viceversa
     * @param exportCollectionAdvanceDtoSerializer to convert exportCollectionAdvanceDto into an entity and viceversa
     * @param customerService Service that provides necessary functionality with customers
     */
    public ExportCollectionAdvanceCancellationRequestDtoSerializer
    (CustomerDtoSerializer customerDtoSerializer,
     DocumentationDtoSerializer documentationDtoSerializer,
     ExportCollectionAdvanceDtoSerializer exportCollectionAdvanceDtoSerializer,
     CustomerService customerService) {
        this.customerDtoSerializer = customerDtoSerializer;
        this.documentationDtoSerializer = documentationDtoSerializer;
        this.exportCollectionAdvanceDtoSerializer = exportCollectionAdvanceDtoSerializer;
        this.customerService = customerService;
    }

    /**
     * Method to convert from Dto object to entity object
     * @param from ExportCollectionAdvanceCancellationRequestDto Object
     * @see ExportCollectionAdvanceCancellationRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest
     * @return ExportCollectionAdvanceCancellationRequest object
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    public ExportCollectionAdvanceCancellationRequest toModel
    (ExportCollectionAdvanceCancellationRequestDto from) throws DateFormatException {
        if (from == null) {
            return null;
        }
        ExportCollectionAdvanceCancellationRequest to = new ExportCollectionAdvanceCancellationRequest();
        injectFields(from, to);
        return to;
    }

    protected void injectFields(ExportCollectionAdvanceCancellationRequestDto from,
                                ExportCollectionAdvanceCancellationRequest to)  throws DateFormatException {
        to.setCode(from.getCode());
        to.setCustomer(customerDtoSerializer.toModel(from.getCustomer()));
        if (from.getDocumentation() != null) {
            injectDocumentation(to, from.getDocumentation());
        }
        if (from.getRequest() != null) {
            injectDetails(to, from.getRequest());
        }
    }

    private void injectDocumentation(ExportCollectionAdvanceCancellationRequest to,
                                     DocumentationDto documentation) throws DateFormatException {
        List<Document> documents = documentationDtoSerializer.mapDocumentation(documentation);
        to.setDocuments(documents);
        to.setPriority(documentation.getPriority());
    }

    private void injectDetails(ExportCollectionAdvanceCancellationRequest to,
                               ExportCollectionAdvanceCancellationRequestDetailsDto details) {
        to.setExportCollectionAdvance(exportCollectionAdvanceDtoSerializer.
                toModel(details.getExportCollectionAdvance()));
        to.setOffice(details.getOffice());
        to.setComment(details.getComments());
    }

    /**
     * Method to convert from entity object to dto object
     * @param from ExportCollectionAdvanceCancellationRequest Object
     * @see ExportCollectionAdvanceCancellationRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest
     * @return ExportCollectionAdvanceCancellationRequest object
     */
    public ExportCollectionAdvanceCancellationRequestDto toDto(ExportCollectionAdvanceCancellationRequest from) {
        if (from == null) {
            return null;
        }
        ExportCollectionAdvanceCancellationRequestDto to = new ExportCollectionAdvanceCancellationRequestDto();
        injectFields(from, to);
        return to;
    }

    protected void injectFields(ExportCollectionAdvanceCancellationRequest from,
                                ExportCollectionAdvanceCancellationRequestDto to) {
        to.setCode(from.getCode());
        if(from.getCustomer() != null) {
            to.setCustomer(customerDtoSerializer.toDto(customerService.
                    getCustomerByPersonNumber(from.getCustomer().getPersonNumber())));
        }
        to.setDocumentation(documentationDtoSerializer.mapDocumentationDto(from));
        to.setRequest(new ExportCollectionAdvanceCancellationRequestDetailsDto());
        to.getRequest().setExportCollectionAdvance(exportCollectionAdvanceDtoSerializer.
                toDto(from.getExportCollectionAdvance()));
        to.getRequest().setOffice(from.getOffice());
        to.getRequest().setComments(from.getComment());
        to.setDisplayedStatus(from.getDisplayedStatus());
    }
}
