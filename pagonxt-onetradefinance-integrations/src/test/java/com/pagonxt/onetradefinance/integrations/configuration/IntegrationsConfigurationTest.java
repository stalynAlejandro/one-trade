package com.pagonxt.onetradefinance.integrations.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.account.AccountGateway;
import com.pagonxt.onetradefinance.integrations.apis.account.serializer.SantanderAccountSerializer;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.FxDealGateway;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.serializer.SantanderFxDealSerializer;
import com.pagonxt.onetradefinance.integrations.apis.riskline.RiskLineGateway;
import com.pagonxt.onetradefinance.integrations.apis.riskline.serializer.SantanderRiskLineSerializer;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.TradeSpecialPricesGateway;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.serializer.SantanderTradeSpecialPricesSerializer;
import com.pagonxt.onetradefinance.integrations.config.UnitTest;
import com.pagonxt.onetradefinance.integrations.util.ValidationUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

@UnitTest
class IntegrationsConfigurationTest {

    private static final String TIME_ZONE = "Europe/Madrid";

    @InjectMocks
    IntegrationsConfiguration integrationsConfiguration;
    @Spy
    private static DateFormatProperties dateFormatProperties;
    @Spy
    ObjectMapper mapper;
    @Mock
    ValidationUtil validationUtil;
    @Mock
    AccountGateway accountGateway;
    @Mock
    SantanderAccountSerializer santanderAccountSerializer;
    @Mock
    RiskLineGateway riskLineGateway;
    @Mock
    SantanderRiskLineSerializer santanderRiskLineSerializer;
    @Mock
    FxDealGateway fxDealGateway;
    @Mock
    SantanderFxDealSerializer santanderFxDealSerializer;
    @Mock
    TradeSpecialPricesGateway tradeSpecialPricesGateway;
    @Mock
    SantanderTradeSpecialPricesSerializer santanderTradeSpecialPricesSerializer;

    @BeforeAll
    public static void setup() {
        dateFormatProperties = new DateFormatProperties();
        dateFormatProperties.setTimeZone(TIME_ZONE);
    }

    @Test
    void mockedServices_ok_returnDoesNotThrow() {
        // Given, When and Then
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.mockedAccountService());
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.mockedClientService());
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.mockedDirectoryService());
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.mockedDocumentationService());
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.mockedFxDealService());
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.mockedOfficeService());
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.mockedRiskLineService());
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.mockedTradeSpecialPricesService());
    }

    @Test
    void realServices_ok_returnDoesNotThrow() {
        // Given, When and Then
        // Implemented
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.defaultAccountService());
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.defaultFxDealService());
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.defaultRiskLineService());
        Assertions.assertDoesNotThrow(() -> integrationsConfiguration.defaultTradeSpecialPricesService());
        // Not Implemented
        Assertions.assertThrows(UnsupportedOperationException.class, () -> integrationsConfiguration.defaultDirectoryService());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> integrationsConfiguration.defaultDocumentationService());
        Assertions.assertThrows(UnsupportedOperationException.class, () -> integrationsConfiguration.defaultOfficeService());
    }
}
