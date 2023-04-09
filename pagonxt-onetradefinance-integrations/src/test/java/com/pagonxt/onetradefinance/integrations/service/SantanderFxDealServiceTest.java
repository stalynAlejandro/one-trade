package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.FxDealGateway;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.FxDealQuery;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.model.SantanderFxDealResponse;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.serializer.SantanderFxDealSerializer;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@UnitTest
class SantanderFxDealServiceTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @InjectMocks
    private SantanderFxDealService santanderFxDealService;
    @Mock
    private FxDealGateway fxDealGateway;
    @Mock
    private SantanderFxDealSerializer santanderFxDealSerializer;

    @Test
    void searchFxDeals_ok_callFxDealGateway() throws IOException {
        // Given
        SantanderFxDealResponse santanderFxDealResponse = mapper.readValue(new ClassPathResource("api-data/santander/fx-deal-response.json").getFile(), SantanderFxDealResponse.class);
        List<ExchangeInsurance> expectedExchangeInsurances = mapper.readValue(new ClassPathResource("api-data/model/exchange-insurances.json").getFile(), List.class);
        FxDealQuery fxDealQuery = new FxDealQuery();
        when(fxDealGateway.searchFxDeals(fxDealQuery)).thenReturn(santanderFxDealResponse);
        when(santanderFxDealSerializer.toModel(santanderFxDealResponse)).thenReturn(expectedExchangeInsurances);
        // When
        List<ExchangeInsurance> result = santanderFxDealService.searchFxDeals(fxDealQuery);
        // Then
        assertEquals(expectedExchangeInsurances, result);
    }
}
