package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineQuery;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

class MockedRiskLineServiceTest {

    private static final String TIMEZONE = "Europe/Madrid";
    private final ObjectMapper mapper = new ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .setDateFormat(new SimpleDateFormat("dd/MM/yyyy"))
            .setTimeZone(TimeZone.getTimeZone(TIMEZONE));

    MockedRiskLineService mockedRiskLineService = new MockedRiskLineService(mapper);

    private static DateFormat dateFormat;

    @BeforeAll
    public static void setup() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
    }

    @Test
    void testGetClientRiskLines() throws ParseException {
        // Given
        Date date1 = dateFormat.parse("26/06/2020");
        Date date2 = dateFormat.parse("22/02/2020");
        List<RiskLine> expectedResult = List.of(
                new RiskLine("001").with("BUC-1234567", "0049 3295 2020 15792", "approved", "1020", "1020", date1, "EUR"),
                new RiskLine("002").with("BUC-1234567", "0049 3295 2020 28222", "waiting", "2120", "1980", date2, "EUR")
        );
        RiskLineQuery query = new RiskLineQuery();
        query.setCustomerId("BUC-1234567");
        // When
        List<RiskLine> result = mockedRiskLineService.getCustomerRiskLines(query);

        // Then
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetRiskLineById() throws ParseException {
        // Given
        Date date1 = dateFormat.parse("26/06/2020");
        RiskLine expectedResult = new RiskLine("001").with("BUC-1234567", "0049 3295 2020 15792", "approved", "1020", "1020", date1, "EUR");
        RiskLineQuery query = new RiskLineQuery();
        query.setRiskLineId("001");
        // When
        RiskLine result = mockedRiskLineService.getRiskLineById(query);
        // Then
        assertThat(result).isEqualTo(expectedResult);
    }
}
