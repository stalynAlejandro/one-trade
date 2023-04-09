package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class has methods to convert DTO's into entities and viceversa,
 * for Export Collection Modification Request(CLE_C002)
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
public class ExportCollectionModificationRequestDtoSerializer {

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
    public ExportCollectionModificationRequestDtoSerializer(CustomerDtoSerializer customerDtoSerializer,
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
     * @param from ExportCollectionModificationRequestDto Object
     * @see ExportCollectionModificationRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest
     * @return ExportCollectionModificationRequest object
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    public ExportCollectionModificationRequest toModel(ExportCollectionModificationRequestDto from) {
        if(from == null) {
            return null;
        }
        ExportCollectionModificationRequest to = new ExportCollectionModificationRequest();
        to.setCode(from.getCode());
        to.setCustomer(customerDtoSerializer.toModel(from.getCustomer()));
        to.setExportCollection(exportCollectionDtoSerializer.toModel(from.getExportCollection()));
        if (from.getDocumentation() != null) {
            List<Document> documents = documentationDtoSerializer.mapDocumentation(from.getDocumentation());
            to.setDocuments(documents);
            to.setPriority(from.getDocumentation().getPriority());
        }
        to.setOffice(from.getOffice());
        to.setComment(from.getComments());
        return to;
    }

    /**
     * Method to convert from entity object to dto object
     * @param from ExportCollectionModificationRequest Object
     * @see ExportCollectionModificationRequestDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest
     * @return ExportCollectionModificationRequest object
     */
    public ExportCollectionModificationRequestDto toDto(ExportCollectionModificationRequest from) {
        if(from == null) {
            return null;
        }
        ExportCollectionModificationRequestDto to = new ExportCollectionModificationRequestDto();
        to.setCode(from.getCode());
        if(from.getCustomer() != null) {
            to.setCustomer(customerDtoSerializer.toDto(
                    customerService.getCustomerByPersonNumber(from.getCustomer().getPersonNumber())));
        }
        to.setExportCollection(exportCollectionDtoSerializer.toDto(from.getExportCollection()));
        to.setDocumentation(documentationDtoSerializer.mapDocumentationDto(from));
        to.setOffice(from.getOffice());
        to.setComments(from.getComment());
        to.setDisplayedStatus(from.getDisplayedStatus());
        return to;
    }
}
