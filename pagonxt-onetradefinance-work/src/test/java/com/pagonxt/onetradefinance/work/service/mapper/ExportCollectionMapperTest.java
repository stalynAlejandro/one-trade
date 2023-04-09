package com.pagonxt.onetradefinance.work.service.mapper;

import com.flowable.dataobject.api.runtime.DataObjectInstanceVariableContainer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollection;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
class ExportCollectionMapperTest {

    @InjectMocks
    private ExportCollectionMapper exportCollectionMapper;

    @Test
    void mapExportCollection_whenPassingDataObject_returnExportCollection() {
        // Given
        Date date = new Date();
        String code = "code1";
        String customerCode = "customerCode1";
        String contractReference = "contractReference1";
        Double amount = 12.34d;
        String currency = "EUR";
        String nominalAccountId = "nominalAccount1";
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = generateExportCollectionDataObject(date, date, code, customerCode, contractReference, amount, currency, nominalAccountId);
        // When
        ExportCollection result = exportCollectionMapper.mapDataObjectInstanceVariableContainerToExportCollection(dataObjectInstanceVariableContainer);
        // Then
        assertEquals(date, result.getCreationDate());
        assertEquals(date, result.getApprovalDate());
        assertEquals(code, result.getCode());
        assertEquals(customerCode, result.getCustomer().getPersonNumber());
        assertEquals(contractReference, result.getContractReference());
        assertEquals(amount, result.getAmount());
        assertEquals(currency, result.getCurrency());
        assertEquals(nominalAccountId, result.getNominalAccount().getAccountId());
    }

    @Test
    void mapExportCollectionAdvance_whenPassingDataObject_returnExportCollection() {
        // Given
        Date date1 = new Date();
        String code1 = "code1";
        String customerCode1 = "customerCode1";
        String contractReference1 = "contractReference1";
        Double amount1 = 12.34d;
        String currency1 = "EUR";
        String nominalAccountId1 = "nominalAccount1";
        DataObjectInstanceVariableContainer exportCollection = generateExportCollectionDataObject(date1, date1, code1, customerCode1, contractReference1, amount1, currency1, nominalAccountId1);
        Date approvalDate2 = new Date();
        String code2 = "code2";
        String customerCode2 = "customerCode2";
        String contractReference2 = "contractReference2";
        Double amount2= 12.35d;
        String currency2 = "GBP";
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainer.getDate("approvalDate")).thenReturn(approvalDate2);
        when(dataObjectInstanceVariableContainer.getDate("creationDate")).thenReturn(approvalDate2);
        when(dataObjectInstanceVariableContainer.getString("code")).thenReturn(code2);
        when(dataObjectInstanceVariableContainer.getString("customerCode")).thenReturn(customerCode2);
        when(dataObjectInstanceVariableContainer.getString("advanceContractReference")).thenReturn(contractReference2);
        when(dataObjectInstanceVariableContainer.getDouble("advanceAmount")).thenReturn(amount2);
        when(dataObjectInstanceVariableContainer.getString("advanceCurrency")).thenReturn(currency2);
        when(dataObjectInstanceVariableContainer.getVariable("exportCollection")).thenReturn(exportCollection);
        // When
        ExportCollectionAdvance result = exportCollectionMapper.mapDataObjectInstanceVariableContainerToExportCollectionAdvance(dataObjectInstanceVariableContainer);
        // Then
        assertEquals(approvalDate2, result.getCreationDate());
        assertEquals(code2, result.getCode());
        assertEquals(customerCode2, result.getCustomer().getPersonNumber());
        assertEquals(contractReference2, result.getContractReference());
        assertEquals(amount2, result.getAmount());
        assertEquals(currency2, result.getCurrency());
        assertEquals(date1, result.getExportCollection().getCreationDate());
        assertEquals(date1, result.getExportCollection().getCreationDate());
        assertEquals(code1, result.getExportCollection().getCode());
        assertEquals(customerCode1, result.getExportCollection().getCustomer().getPersonNumber());
        assertEquals(contractReference1, result.getExportCollection().getContractReference());
        assertEquals(amount1, result.getExportCollection().getAmount());
        assertEquals(currency1, result.getExportCollection().getCurrency());
        assertEquals(nominalAccountId1, result.getExportCollection().getNominalAccount().getAccountId());
    }

    private static DataObjectInstanceVariableContainer generateExportCollectionDataObject(Date approvalDate, Date creationDate, String code, String customerCode, String contractReference, Double amount, String currency, String nominalAccountId) {
        DataObjectInstanceVariableContainer dataObjectInstanceVariableContainer = mock(DataObjectInstanceVariableContainer.class);
        when(dataObjectInstanceVariableContainer.getDate("approvalDate")).thenReturn(approvalDate);
        when(dataObjectInstanceVariableContainer.getDate("creationDate")).thenReturn(creationDate);
        when(dataObjectInstanceVariableContainer.getString("code")).thenReturn(code);
        when(dataObjectInstanceVariableContainer.getString("customerCode")).thenReturn(customerCode);
        when(dataObjectInstanceVariableContainer.getString("contractReference")).thenReturn(contractReference);
        when(dataObjectInstanceVariableContainer.getDouble("amount")).thenReturn(amount);
        when(dataObjectInstanceVariableContainer.getString("currency")).thenReturn(currency);
        when(dataObjectInstanceVariableContainer.getString("nominalAccountId")).thenReturn(nominalAccountId);
        return dataObjectInstanceVariableContainer;
    }
}
