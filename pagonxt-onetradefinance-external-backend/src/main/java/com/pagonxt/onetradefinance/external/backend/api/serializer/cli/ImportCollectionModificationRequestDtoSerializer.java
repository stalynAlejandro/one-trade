package com.pagonxt.onetradefinance.external.backend.api.serializer.cli;

import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cli.ImportCollectionModificationRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.DocumentationDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.constants.FieldConstants.IMPORT_COLLECTION;
import static com.pagonxt.onetradefinance.integrations.constants.TradeConstants.EVENT_MODIFICATION;
import static com.pagonxt.onetradefinance.integrations.constants.TradeConstants.PRODUCT_IMPORT_COLLECTION;

/**
 * This class has methods to convert DTO's into entities and viceversa,
 * for Import Collection Modification Request
 * @author -
 * @version jdk-11.0.13
 * @see CustomerDtoSerializer
 * @see DocumentationDtoSerializer
 * @see ImportCollectionDtoSerializer
 * @see CustomerService
 * @see java.text.DateFormat
 * @since jdk-11.0.13
 */
@Component
public class ImportCollectionModificationRequestDtoSerializer implements TradeRequestDtoSerializer {

    /**
     * Class attributes
     */
    private final CustomerDtoSerializer customerDtoSerializer;

    private final CustomerService customerService;

    private final ImportCollectionDtoSerializer importCollectionDtoSerializer;

    private final DocumentationDtoSerializer documentationDtoSerializer;

    /**
     * Class constructor
     * @param customerDtoSerializer to convert customerDto into an entity and viceversa
     * @param documentationDtoSerializer to convert documentationDto into an entity and viceversa
     * @param importCollectionDtoSerializer to convert importCollectionAdvanceDto into an entity and viceversa
     * @param customerService Service that provides necessary functionality with customers
     */
    public ImportCollectionModificationRequestDtoSerializer(CustomerDtoSerializer customerDtoSerializer,
                                                            CustomerService customerService,
                                                            ImportCollectionDtoSerializer importCollectionDtoSerializer,
                                                            DocumentationDtoSerializer documentationDtoSerializer) {
        this.customerDtoSerializer = customerDtoSerializer;
        this.customerService = customerService;
        this.importCollectionDtoSerializer = importCollectionDtoSerializer;
        this.documentationDtoSerializer = documentationDtoSerializer;
    }

    /**
     * Method to convert from Dto object to entity object
     * @param from ImportCollectionModificationRequestDto Object
     * @see ImportCollectionModificationRequestDto
     * @see TradeRequest
     * @return ImportCollectionModificationRequest object
     * @throws DateFormatException Signals that a data format error has occurred.
     */
    public TradeRequest toModel(CommonRequestDto from) {
        if(from == null) {
            return null;
        }
        TradeRequest to = new TradeRequest();
        to.setCode(from.getCode());
        to.setProduct(PRODUCT_IMPORT_COLLECTION);
        to.setEvent(EVENT_MODIFICATION);
        to.setCustomer(customerDtoSerializer.toModel(from.getCustomer()));
        if (from.getDocumentation() != null) {
            List<Document> documents = documentationDtoSerializer.mapDocumentation(from.getDocumentation());
            to.setDocuments(documents);
            to.setPriority(from.getDocumentation().getPriority());
        }
        ImportCollectionModificationRequestDto castedFrom = (ImportCollectionModificationRequestDto) from;
        if (castedFrom.getImportCollection() != null) {
            to.setDetails(Map.of(IMPORT_COLLECTION, castedFrom.getImportCollection().getCode()));
        }
        to.setOffice(castedFrom.getOffice());
        to.setComment(castedFrom.getComments());
        return to;
    }

    /**
     * Method to convert from entity object to dto object
     * @param from ImportCollectionModificationRequest Object
     * @see ImportCollectionModificationRequestDto
     * @see TradeRequest
     * @return ImportCollectionModificationRequest object
     */
    public ImportCollectionModificationRequestDto toDto(TradeRequest from) {
        if(from == null) {
            return null;
        }
        ImportCollectionModificationRequestDto to = new ImportCollectionModificationRequestDto();
        to.setCode(from.getCode());
        if(from.getCustomer() != null) {
            to.setCustomer(customerDtoSerializer.toDto(
                    customerService.getCustomerByPersonNumber(from.getCustomer().getPersonNumber())));
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> importCollectionMap = (Map<String, Object>) from.getDetails().get(IMPORT_COLLECTION);
        to.setImportCollection(importCollectionDtoSerializer.toDto(importCollectionMap));
        to.setDocumentation(documentationDtoSerializer.mapDocumentationTradeDto(from));
        to.setOffice(from.getOffice());
        to.setComments(from.getComment());
        to.setDisplayedStatus(from.getDisplayedStatus());
        return to;
    }
}
