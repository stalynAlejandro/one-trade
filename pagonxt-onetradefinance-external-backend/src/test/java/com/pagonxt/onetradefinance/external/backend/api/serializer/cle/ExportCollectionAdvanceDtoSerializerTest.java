package com.pagonxt.onetradefinance.external.backend.api.serializer.cle;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionAdvanceDto;
import com.pagonxt.onetradefinance.external.backend.api.model.cle.ExportCollectionDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CustomerDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.ExportCollection;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.service.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@UnitTest
class ExportCollectionAdvanceDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ExportCollectionAdvanceDtoSerializer serializer;
    @Mock
    private CustomerDtoSerializer customerDtoSerializer;
    @Mock
    private CustomerService customerService;
    @Mock
    private ExportCollectionDtoSerializer exportCollectionDtoSerializer;
    @Spy
    private static DateFormatProperties DateFormatProperties;

    @BeforeAll
    static void setup() {
        DateFormatProperties = new DateFormatProperties();
        DateFormatProperties.setTimeZone("Europe/Madrid");
        DateFormatProperties.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionAdvance expectedExportCollectionAdvance = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance.json").getFile(), ExportCollectionAdvance.class);
        ExportCollectionAdvanceDto exportCollectionAdvanceDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-dto.json").getFile(), ExportCollectionAdvanceDto.class);
        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);

        ExportCollection expectedExportCollection = mapper.readValue(new ClassPathResource("data/model/cle/export-collection.json").getFile(), ExportCollection.class);
        ExportCollectionDto expectedExportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);
        when(exportCollectionDtoSerializer.toModel(expectedExportCollectionDto)).thenReturn(expectedExportCollection);
        // When
        ExportCollectionAdvance result = serializer.toModel(exportCollectionAdvanceDto);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedExportCollectionAdvance);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_whenNull_thenReturnsNewExportCollection() {
        // Given
        ExportCollectionAdvance expectedResult = new ExportCollectionAdvance();
        // When
        ExportCollectionAdvance result = serializer.toModel(null);
        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    void toModel_whenCreationDateError_trowsDateFormatException() throws IOException {
        // Given
        ExportCollectionAdvanceDto exportCollectionAdvanceDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-dto.json").getFile(), ExportCollectionAdvanceDto.class);
        exportCollectionAdvanceDto.setCreationDate("non parsable date");
        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);
        // When
        DateFormatException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(exportCollectionAdvanceDto));
        // Then
        assertEquals("Unable to parse date", exception.getMessage());
        assertEquals("unableToParseDate", exception.getKey());
    }

    @Test
    void toModel_whenApprovalDateError_trowsDateFormatException() throws IOException {
        // Given
        ExportCollectionAdvanceDto exportCollectionAdvanceDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-dto.json").getFile(), ExportCollectionAdvanceDto.class);
        exportCollectionAdvanceDto.setApprovalDate("non parsable date");
        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);
        // When
        DateFormatException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(exportCollectionAdvanceDto));
        // Then
        assertEquals("Unable to parse date", exception.getMessage());
        assertEquals("unableToParseDate", exception.getKey());
    }

    @Test
    void toModel_whenRequestExpirationError_trowsDateFormatException() throws IOException {
        // Given
        ExportCollectionAdvanceDto exportCollectionAdvanceDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-dto.json").getFile(), ExportCollectionAdvanceDto.class);
        exportCollectionAdvanceDto.setRequestExpiration("non parsable date");
        Customer expectedCustomer = mapper.readValue(new ClassPathResource("./data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerDtoSerializer.toModel(expectedCustomerDto)).thenReturn(expectedCustomer);
        // When
        DateFormatException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(exportCollectionAdvanceDto));
        // Then
        assertEquals("Unable to parse date", exception.getMessage());
        assertEquals("unableToParseDate", exception.getKey());
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExportCollectionAdvance exportCollectionAdvance = mapper.readValue(new ClassPathResource("data/model/cle/export-collection-advance.json").getFile(), ExportCollectionAdvance.class);
        ExportCollectionAdvanceDto expectedExportCollectionAdvanceDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-advance-dto.json").getFile(), ExportCollectionAdvanceDto.class);

        Customer expectedCustomer = mapper.readValue(new ClassPathResource("data/model/customer.json").getFile(), Customer.class);
        CustomerDto expectedCustomerDto = mapper.readValue(new ClassPathResource("data/dto/customer-dto.json").getFile(), CustomerDto.class);
        when(customerService.getCustomerByPersonNumber("BUC-1234567")).thenReturn(expectedCustomer);
        when(customerDtoSerializer.toDto(expectedCustomer)).thenReturn(expectedCustomerDto);

        ExportCollection expectedExportCollection = mapper.readValue(new ClassPathResource("data/model/cle/export-collection.json").getFile(), ExportCollection.class);
        ExportCollectionDto expectedExportCollectionDto = mapper.readValue(new ClassPathResource("data/dto/cle/export-collection-dto.json").getFile(), ExportCollectionDto.class);
        when(exportCollectionDtoSerializer.toDto(expectedExportCollection)).thenReturn(expectedExportCollectionDto);
        // When
        ExportCollectionAdvanceDto result = serializer.toDto(exportCollectionAdvance);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedExportCollectionAdvanceDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        ExportCollectionAdvanceDto result = serializer.toDto(null);
        // Then
        assertNull(result);
    }
}
