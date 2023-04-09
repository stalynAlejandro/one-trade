package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollection;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.service.AccountService;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;

/**
 * This class has methods to convert DTO's into entities and viceversa, for Export Collection
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.AccountDtoSerializer
 * @see ExportCollectionDtoSerializer
 * @see com.pagonxt.onetradefinance.integrations.service.CustomerService
 * @see com.pagonxt.onetradefinance.integrations.service.AccountService
 * @since jdk-11.0.13
 */
@Component
public class ExportCollectionDtoSerializer {

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
    public ExportCollectionDtoSerializer(DateFormatProperties dateFormatProperties,
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
     * Method to convert from Dto object to entity object
     * @param exportCollectionDto ExportCollectionDto object
     * @see ExportCollectionDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollection
     * @return ExportCollection object
     */
    public ExportCollection toModel(ExportCollectionDto exportCollectionDto) {
        if (exportCollectionDto == null) {
            return new ExportCollection();
        }
        ExportCollection exportCollection = new ExportCollection();
        exportCollection.setCode(exportCollectionDto.getCode());
        exportCollection.setCustomer(customerDtoSerializer.toModel(exportCollectionDto.getCustomer()));
        if (Strings.isNotBlank(exportCollectionDto.getCreationDate())) {
            try {
                exportCollection.setCreationDate(dateTimeFormat.parse(exportCollectionDto.getCreationDate()));
            } catch (ParseException e) {
                throw new DateFormatException(exportCollectionDto.getCreationDate(), e);
            }
        }
        if (Strings.isNotBlank(exportCollectionDto.getApprovalDate())) {
            try {
                exportCollection.setApprovalDate(dateTimeFormat.parse(exportCollectionDto.getApprovalDate()));
            } catch (ParseException e) {
                throw new DateFormatException(exportCollectionDto.getApprovalDate(), e);
            }
        }
        exportCollection.setContractReference(exportCollectionDto.getContractReference());
        if (exportCollectionDto.getAmount() != null) {
            exportCollection.setAmount(Double.parseDouble(exportCollectionDto.getAmount()));
        }
        exportCollection.setCurrency(exportCollectionDto.getCurrency());
        exportCollection.setNominalAccount(accountDtoSerializer.toModel(exportCollectionDto.getNominalAccount()));
        return exportCollection;
    }

    /**
     * Method to convert from entity object to dto object
     * @param exportCollection exportCollectionAdvance object
     * @see ExportCollectionDto
     * @see com.pagonxt.onetradefinance.integrations.model.ExportCollection
     * @return exportCollectionDto object
     */
    public ExportCollectionDto toDto(ExportCollection exportCollection) {
        if(exportCollection == null) {
            return null;
        }
        ExportCollectionDto exportCollectionDto = new ExportCollectionDto();
        exportCollectionDto.setCode(exportCollection.getCode());
        if(exportCollection.getCustomer() != null) {
            exportCollectionDto.setCustomer(customerDtoSerializer.toDto(
                    customerService.getCustomerByPersonNumber(exportCollection.getCustomer().getPersonNumber())));
        }
        if (exportCollection.getCreationDate() != null) {
            exportCollectionDto.setCreationDate(dateTimeFormat.format(exportCollection.getCreationDate()));
        }
        if (exportCollection.getApprovalDate() != null) {
            exportCollectionDto.setApprovalDate(dateTimeFormat.format(exportCollection.getApprovalDate()));
        }
        exportCollectionDto.setContractReference(exportCollection.getContractReference());
        if (exportCollection.getAmount() != null) {
            exportCollectionDto.setAmount(exportCollection.getAmount().toString());
        }
        exportCollectionDto.setCurrency(exportCollection.getCurrency());
        if(exportCollection.getNominalAccount() != null) {
            exportCollectionDto.setNominalAccount(accountDtoSerializer.toDto
                    (accountService.getAccountById(exportCollection.getNominalAccount().getAccountId())));
        }
        return exportCollectionDto;
    }
}
