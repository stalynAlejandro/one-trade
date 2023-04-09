package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.DocumentationDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionOtherOperationsRequestDetailsDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionOtherOperationsRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class has methods to convert DTO's into entities and viceversa,
 * for Export Collection Other Operations Request(CLE_C006)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer
 * @see ExportCollectionDtoSerializer
 * @see com.pagonxt.onetradefinance.integrations.service.CustomerService
 * @see java.text.DateFormat
 * @since jdk-11.0.13
 */
@Component
public class ExportCollectionOtherOperationsRequestDtoSerializer {

    /**
     * Class attributes
     */
    private final CustomerDtoSerializer customerDtoSerializer;

    private final CustomerService customerService;

    private final ExportCollectionDtoSerializer exportCollectionDtoSerializer;

    private final DocumentationDtoSerializer documentationDtoSerializer;

    /**
     * Class constructor
     * @param customerDtoSerializer to convert customerDto into an entity and viceversa
     * @param documentationDtoSerializer to convert documentationDto into an entity and viceversa
     * @param exportCollectionDtoSerializer to convert exportCollectionAdvanceDto into an entity and viceversa
     * @param customerService Service that provides necessary functionality with customers
     */
    public ExportCollectionOtherOperationsRequestDtoSerializer
    (CustomerDtoSerializer customerDtoSerializer,
     CustomerService customerService,
     ExportCollectionDtoSerializer exportCollectionDtoSerializer,
     DocumentationDtoSerializer documentationDtoSerializer) {
        this.customerDtoSerializer = customerDtoSerializer;
        this.customerService = customerService;
        this.exportCollectionDtoSerializer = exportCollectionDtoSerializer;
        this.documentationDtoSerializer = documentationDtoSerializer;
    }

    /**
     * Method to convert from Dto object to entity object
     * @param from ExportCollectionOtherOperationsRequestDto Object
     * @see ExportCollectionOtherOperationsRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest
     * @return ExportCollectionOtherOperationsRequest object
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    public ExportCollectionOtherOperationsRequest toModel
    (ExportCollectionOtherOperationsRequestDto from) throws DateFormatException {
        if (from == null) {
            return null;
        }
        ExportCollectionOtherOperationsRequest to = new ExportCollectionOtherOperationsRequest();
        injectFields(from, to);
        return to;
    }

    protected void injectFields(ExportCollectionOtherOperationsRequestDto from,
                                ExportCollectionOtherOperationsRequest to)  throws DateFormatException {
        to.setCode(from.getCode());
        to.setCustomer(customerDtoSerializer.toModel(from.getCustomer()));
        if (from.getDocumentation() != null) {
            injectDocumentation(to, from.getDocumentation());
        }
        if (from.getRequest() != null) {
            injectDetails(to, from.getRequest());
        }
    }

    private void injectDocumentation(ExportCollectionOtherOperationsRequest to,
                                     DocumentationDto documentation) throws DateFormatException {
        List<Document> documents = documentationDtoSerializer.mapDocumentation(documentation);
        to.setDocuments(documents);
        to.setPriority(documentation.getPriority());
    }

    private void injectDetails(ExportCollectionOtherOperationsRequest to,
                               ExportCollectionOtherOperationsRequestDetailsDto details) {
        to.setExportCollection(exportCollectionDtoSerializer.toModel(details.getExportCollection()));
        to.setOperationType(details.getOperationType());
        to.setOffice(details.getOffice());
        to.setComment(details.getComments());
    }

    /**
     * Method to convert from entity object to dto object
     * @param from ExportCollectionOtherOperationsRequest Object
     * @see ExportCollectionOtherOperationsRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest
     * @return ExportCollectionOtherOperationsRequest object
     */
    public ExportCollectionOtherOperationsRequestDto toDto(ExportCollectionOtherOperationsRequest from) {
        if (from == null) {
            return null;
        }
        ExportCollectionOtherOperationsRequestDto to = new ExportCollectionOtherOperationsRequestDto();
        injectFields(from, to);
        return to;
    }

    protected void injectFields(ExportCollectionOtherOperationsRequest from,
                                ExportCollectionOtherOperationsRequestDto to) {
        to.setCode(from.getCode());
        if(from.getCustomer() != null) {
            to.setCustomer(customerDtoSerializer.toDto(customerService.
                    getCustomerByPersonNumber(from.getCustomer().getPersonNumber())));
        }
        to.setDocumentation(documentationDtoSerializer.mapDocumentationDto(from));
        to.setRequest(new ExportCollectionOtherOperationsRequestDetailsDto());
        to.getRequest().setExportCollection(exportCollectionDtoSerializer.toDto(from.getExportCollection()));
        to.getRequest().setOffice(from.getOffice());
        to.getRequest().setComments(from.getComment());
        to.getRequest().setOperationType(from.getOperationType());
    }

}
