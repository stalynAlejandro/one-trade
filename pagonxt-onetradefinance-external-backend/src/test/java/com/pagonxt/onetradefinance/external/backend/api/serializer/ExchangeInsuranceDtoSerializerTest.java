package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.ExchangeInsuranceDto;
import com.pagonxt.onetradefinance.external.backend.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import com.pagonxt.onetradefinance.integrations.model.exception.DateFormatException;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
class ExchangeInsuranceDtoSerializerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private ExchangeInsuranceDtoSerializer serializer;
    @Spy
    private static DateFormatProperties dateFormatProperties;

    @BeforeAll
    static void setup() {
        dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone("Europe/Madrid");
        dateFormatProperties.setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getDefault());
        mapper.setDateFormat(df);
    }

    @Test
    void toDto_ok_returnsValidModel() throws Exception {
        // Given
        ExchangeInsurance exchangeInsurance = mapper.readValue(new ClassPathResource("data/model/exchange-insurance.json").getFile(), ExchangeInsurance.class);
        ExchangeInsuranceDto expectedExchangeInsuranceDto = mapper.readValue(new ClassPathResource("data/dto/exchange-insurance-dto.json").getFile(), ExchangeInsuranceDto.class);
        // When
        ExchangeInsuranceDto result = serializer.toDto(exchangeInsurance);

        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedExchangeInsuranceDto);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toDto_whenNull_thenReturnsNull() {
        // Given
        // When
        ExchangeInsuranceDto result = serializer.toDto(null);
        // Then
        assertNull(result);
    }


    @Test
    void toDto_whenEmpty_thenReturnsEmpty() {
        // Given
        ExchangeInsuranceDto expectedResult = new ExchangeInsuranceDto();
        // When
        ExchangeInsuranceDto result = serializer.toDto(new ExchangeInsurance());
        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    void toModel_ok_returnsValidModel() throws Exception {
        // Given
        ExchangeInsurance expectedExchangeInsurance = mapper.readValue(new ClassPathResource("data/model/exchange-insurance.json").getFile(), ExchangeInsurance.class);
        ExchangeInsuranceDto exchangeInsuranceDto = mapper.readValue(new ClassPathResource("data/dto/exchange-insurance-dto.json").getFile(), ExchangeInsuranceDto.class);
        // When
        ExchangeInsurance result = serializer.toModel(exchangeInsuranceDto);
        // Then
        JsonNode resultJsonNode = mapper.valueToTree(result);
        JsonNode expectedJsonNode = mapper.valueToTree(expectedExchangeInsurance);
        assertEquals(expectedJsonNode, resultJsonNode, "Should return a valid model");
    }

    @Test
    void toModel_wrongDateFormat_throwsDateFormatException() throws Exception {
        // Given
        ExchangeInsuranceDto exchangeInsuranceDto = mapper.readValue(new ClassPathResource("data/dto/exchange-insurance-dto.json").getFile(), ExchangeInsuranceDto.class);
        exchangeInsuranceDto.setUseDate("wrong_format");
        // When
        ServiceException exception = assertThrows(DateFormatException.class, () -> serializer.toModel(exchangeInsuranceDto));
        // Then
        assertEquals("unableToParseDate", exception.getKey());
        assertEquals("Unable to parse date", exception.getMessage());

    }

    @Test
    void toModel_whenNull_thenReturnsEmpty() {
        // Given
        ExchangeInsurance expectedResult = new ExchangeInsurance();
        // When
        ExchangeInsurance result = serializer.toModel(null);
        // Then
        assertEquals(expectedResult, result);
    }

    @Test
    void toModel_whenEmpty_thenReturnsEmpty() {
        // Given
        ExchangeInsurance expectedResult = new ExchangeInsurance();
        // When
        ExchangeInsurance result = serializer.toModel(new ExchangeInsuranceDto());
        // Then
        assertEquals(expectedResult, result);
    }
}
