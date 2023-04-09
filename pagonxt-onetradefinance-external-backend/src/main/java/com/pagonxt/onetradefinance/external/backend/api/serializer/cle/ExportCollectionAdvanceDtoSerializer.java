package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;

/**
 * This class has methods to convert DTO's into entities and viceversa, for Export Collection Advance
 * for Export Collection Advance Cancellation Request(CLE_C005)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer
 * @see ExportCollectionDtoSerializer
 * @see com.pagonxt.onetradefinance.integrations.service.CustomerService
 * @see java.text.DateFormat
 * @since jdk-11.0.13
 */
@Component
public class ExportCollectionAdvanceDtoSerializer {

    /**
     * Class attributes
     */
    private final CustomerDtoSerializer customerDtoSerializer;

    private final CustomerService customerService;

    private final ExportCollectionDtoSerializer exportCollectionDtoSerializer;

    private final DateFormat dateTimeFormat;

    /**
     * Class constructor
     * @param dateFormatProperties In this case, we use this object to get the date format
     * @param customerDtoSerializer to convert customerDto into an entity and viceversa
     * @param customerService ervice that provides necessary functionality with customers
     * @param exportCollectionDtoSerializer to convert exportCollectionDto into an entity and viceversa
     */
    public ExportCollectionAdvanceDtoSerializer(DateFormatProperties dateFormatProperties,
                                                CustomerDtoSerializer customerDtoSerializer,
                                                CustomerService customerService,
                                                ExportCollectionDtoSerializer exportCollectionDtoSerializer) {
        dateTimeFormat = dateFormatProperties.getDateFormatInstance();
        this.customerDtoSerializer = customerDtoSerializer;
        this.customerService = customerService;
        this.exportCollectionDtoSerializer = exportCollectionDtoSerializer;
    }

    /**
     * Method to convert from Dto object to entity object
     * @param exportCollectionAdvanceDto ExportCollectionAdvanceDto object
     * @see ExportCollectionAdvanceDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance
     * @return ExportCollectionAdvance object
     */
    public ExportCollectionAdvance toModel(ExportCollectionAdvanceDto exportCollectionAdvanceDto) {
        if(exportCollectionAdvanceDto == null) {
            return new ExportCollectionAdvance();
        }
        ExportCollectionAdvance exportCollectionAdvance = new ExportCollectionAdvance();
        exportCollectionAdvance.setCode(exportCollectionAdvanceDto.getCode());
        exportCollectionAdvance.setCustomer(customerDtoSerializer.toModel(exportCollectionAdvanceDto.getCustomer()));
        if (Strings.isNotBlank(exportCollectionAdvanceDto.getCreationDate())) {
            try {
                exportCollectionAdvance.setCreationDate(dateTimeFormat.
                        parse(exportCollectionAdvanceDto.getCreationDate()));
            } catch (ParseException e) {
                throw new DateFormatException(exportCollectionAdvanceDto.getCreationDate(), e);
            }
        }
        if (Strings.isNotBlank(exportCollectionAdvanceDto.getApprovalDate())) {
            try {
                exportCollectionAdvance.setApprovalDate(dateTimeFormat.
                        parse(exportCollectionAdvanceDto.getApprovalDate()));
            } catch (ParseException e) {
                throw new DateFormatException(exportCollectionAdvanceDto.getApprovalDate(), e);
            }
        }
        if (Strings.isNotBlank(exportCollectionAdvanceDto.getRequestExpiration())) {
            try {
                exportCollectionAdvance.setExpirationDate(dateTimeFormat.
                        parse(exportCollectionAdvanceDto.getRequestExpiration()));
            } catch (ParseException e) {
                throw new DateFormatException(exportCollectionAdvanceDto.getRequestExpiration(), e);
            }
        }
        exportCollectionAdvance.setContractReference(exportCollectionAdvanceDto.getContractReference());
        if (exportCollectionAdvanceDto.getAmount() != null) {
            exportCollectionAdvance.setAmount(Double.parseDouble(exportCollectionAdvanceDto.getAmount()));
        }
        exportCollectionAdvance.setCurrency(exportCollectionAdvanceDto.getCurrency());
        exportCollectionAdvance.setExportCollection(exportCollectionDtoSerializer.
                toModel(exportCollectionAdvanceDto.getExportCollection()));
        return exportCollectionAdvance;
    }

    /**
     * Method to convert from entity object to dto object
     * @param exportCollectionAdvance exportCollectionAdvance object
     * @see ExportCollectionAdvanceDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance
     * @return exportCollectionAdvanceDto object
     */
    public ExportCollectionAdvanceDto toDto(ExportCollectionAdvance exportCollectionAdvance) {
        if(exportCollectionAdvance == null) {
            return null;
        }
        ExportCollectionAdvanceDto exportCollectionAdvanceDto = new ExportCollectionAdvanceDto();
        exportCollectionAdvanceDto.setCode(exportCollectionAdvance.getCode());
        if(exportCollectionAdvance.getCustomer() != null) {
            exportCollectionAdvanceDto.setCustomer(customerDtoSerializer.toDto
                    (customerService.getCustomerByPersonNumber
                            (exportCollectionAdvance.getCustomer().getPersonNumber())));
        }
        if (exportCollectionAdvance.getCreationDate() != null) {
            exportCollectionAdvanceDto.setCreationDate(dateTimeFormat.
                    format(exportCollectionAdvance.getCreationDate()));
        }
        if (exportCollectionAdvance.getApprovalDate() != null) {
            exportCollectionAdvanceDto.setApprovalDate(dateTimeFormat.
                    format(exportCollectionAdvance.getApprovalDate()));
        }
        exportCollectionAdvanceDto.setContractReference(exportCollectionAdvance.getContractReference());
        if (exportCollectionAdvance.getAmount() != null) {
            exportCollectionAdvanceDto.setAmount(exportCollectionAdvance.getAmount().toString());
        }
        exportCollectionAdvanceDto.setCurrency(exportCollectionAdvance.getCurrency());
        if(exportCollectionAdvance.getExportCollection() != null) {
            exportCollectionAdvanceDto.setExportCollection(exportCollectionDtoSerializer.
                    toDto(exportCollectionAdvance.getExportCollection()));
        }
        if(exportCollectionAdvance.getExpirationDate() != null) {
            exportCollectionAdvanceDto.setRequestExpiration(dateTimeFormat.
                    format(exportCollectionAdvance.getExpirationDate()));
        }
        return exportCollectionAdvanceDto;
    }
}
