package com.pagonxt.onetradefinance.integrations.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.apis.account.AccountGateway;
import com.pagonxt.onetradefinance.integrations.apis.account.serializer.SantanderAccountSerializer;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.FxDealGateway;
import com.pagonxt.onetradefinance.integrations.apis.fxdeal.serializer.SantanderFxDealSerializer;
import com.pagonxt.onetradefinance.integrations.apis.riskline.RiskLineGateway;
import com.pagonxt.onetradefinance.integrations.apis.riskline.serializer.SantanderRiskLineSerializer;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.TradeSpecialPricesGateway;
import com.pagonxt.onetradefinance.integrations.apis.specialprices.serializer.SantanderTradeSpecialPricesSerializer;
import com.pagonxt.onetradefinance.integrations.service.*;
import com.pagonxt.onetradefinance.integrations.util.ValidationUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Configuration class
 * This class generates call services to Santander Api's
 * Every service can use mock data or real data, if the property mockEnabled is false or true
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see ValidationUtil
 * @see com.pagonxt.onetradefinance.integrations.apis.account.AccountGateway
 * @see com.pagonxt.onetradefinance.integrations.apis.account.serializer.SantanderAccountSerializer
 * @see com.pagonxt.onetradefinance.integrations.apis.riskline.RiskLineGateway
 * @see com.pagonxt.onetradefinance.integrations.apis.riskline.serializer.SantanderRiskLineSerializer
 * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.FxDealGateway
 * @see com.pagonxt.onetradefinance.integrations.apis.fxdeal.serializer.SantanderFxDealSerializer
 * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.TradeSpecialPricesGateway
 * @see com.pagonxt.onetradefinance.integrations.apis.specialprices.serializer.SantanderTradeSpecialPricesSerializer
 * @since jdk-11.0.13
 */
@Configuration
public class IntegrationsConfiguration {

    private final ObjectMapper mapper;
    private final ValidationUtil validationUtil;
    private final AccountGateway accountGateway;
    private final SantanderAccountSerializer santanderAccountSerializer;
    private final RiskLineGateway riskLineGateway;
    private final SantanderRiskLineSerializer santanderRiskLineSerializer;
    private final FxDealGateway fxDealGateway;
    private final SantanderFxDealSerializer santanderFxDealSerializer;
    private final TradeSpecialPricesGateway tradeSpecialPricesGateway;
    private final SantanderTradeSpecialPricesSerializer santanderTradeSpecialPricesSerializer;

    /**
     * Constructor method
     *
     * @param dateFormatProperties                  : DateFormatProperties object
     * @param validationUtil                        : ValidationUtil object
     * @param accountGateway                        : AccountGateway object
     * @param santanderAccountSerializer            : SantanderAccountSerializer object
     * @param riskLineGateway                       : RiskLineGateway
     * @param santanderRiskLineSerializer           : SantanderRiskLineSerializer object
     * @param fxDealGateway                         : FxDealGateway object
     * @param santanderFxDealSerializer             : SantanderFxDealSerializer object
     * @param tradeSpecialPricesGateway             : TradeSpecialPricesGateway object
     * @param santanderTradeSpecialPricesSerializer : SantanderTradeSpecialPricesSerializer object
     */
    public IntegrationsConfiguration(DateFormatProperties dateFormatProperties,
                                     ValidationUtil validationUtil,
                                     AccountGateway accountGateway,
                                     SantanderAccountSerializer santanderAccountSerializer,
                                     RiskLineGateway riskLineGateway,
                                     SantanderRiskLineSerializer santanderRiskLineSerializer,
                                     FxDealGateway fxDealGateway,
                                     SantanderFxDealSerializer santanderFxDealSerializer,
                                     TradeSpecialPricesGateway tradeSpecialPricesGateway,
                                     SantanderTradeSpecialPricesSerializer santanderTradeSpecialPricesSerializer) {
        this.validationUtil = validationUtil;
        this.accountGateway = accountGateway;
        this.santanderAccountSerializer = santanderAccountSerializer;
        this.riskLineGateway = riskLineGateway;
        this.santanderRiskLineSerializer = santanderRiskLineSerializer;
        this.fxDealGateway = fxDealGateway;
        this.santanderFxDealSerializer = santanderFxDealSerializer;
        this.tradeSpecialPricesGateway = tradeSpecialPricesGateway;
        this.santanderTradeSpecialPricesSerializer = santanderTradeSpecialPricesSerializer;
        this.mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setDateFormat(new SimpleDateFormat("dd/MM/yyyy"))
                .setTimeZone(TimeZone.getTimeZone(dateFormatProperties.getTimeZone()));
    }



    /**
     * Bean for directory service with mocked data
     * @see DirectoryService
     * @return DirectoryService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.directory",
            name = "mock-enabled", havingValue = "true", matchIfMissing = true)
    DirectoryService mockedDirectoryService() {
        return new MockedDirectoryService();
    }

    /**
     * Bean for directory service with real data (not implemented yet)
     * @see DirectoryService
     * @return DirectoryService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.directory", name = "mock-enabled", havingValue = "false")
    DirectoryService defaultDirectoryService() {
        throw new UnsupportedOperationException("Default directory service implementation not available yet");
    }

    /**
     * Bean for client service with mocked data
     * @return CustomerService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.repository",
            name = "mock-enabled", havingValue = "true", matchIfMissing = true)
    CustomerService mockedClientService() {
        return new MockedCustomerService(mapper, validationUtil);
    }


    /**
     * Bean for account service with mocked data
     * @return AccountService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.account",
            name = "mock-enabled", havingValue = "true", matchIfMissing = true)
    AccountService mockedAccountService() {
        return new MockedAccountService(mapper);
    }

    /**
     * Bean for account service with real data
     * @return AccountService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.account", name = "mock-enabled", havingValue = "false")
    AccountService defaultAccountService() {
        return new SantanderAccountService(accountGateway, santanderAccountSerializer);
    }

    /**
     * Bean for Risk line service with mocked data
     * @return RiskLineService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.risk-line",
            name = "mock-enabled", havingValue = "true", matchIfMissing = true)
    RiskLineService mockedRiskLineService() {
        return new MockedRiskLineService(mapper);
    }

    /**
     * Bean for Risk line service with real data
     * @return RiskLineService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.risk-line", name = "mock-enabled", havingValue = "false")
    RiskLineService defaultRiskLineService() {
        return new SantanderRiskLineService(riskLineGateway, santanderRiskLineSerializer);
    }

    /**
     * Bean for documentation service with mocked data
     * @return DocumentationService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.documentation",
            name = "mock-enabled", havingValue = "true", matchIfMissing = true)
    DocumentationService mockedDocumentationService() {
        return new MockedDocumentationService(mapper);
    }

    /**
     * Bean for documentation service with real data (not implemented yet)
     * @return DocumentationService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.documentation",
            name = "mock-enabled", havingValue = "false")
    DocumentationService defaultDocumentationService() {
        throw new UnsupportedOperationException("Default documentation service implementation not available yet");
    }

    /**
     * Bean for office service with mocked data
     * @return OfficeService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.office",
            name = "mock-enabled", havingValue = "true", matchIfMissing = true)
    OfficeService mockedOfficeService() {
        return new MockedOfficeService();
    }

    /**
     * Bean for office service with real data (not implemented yet)
     * @return OfficeService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.office", name = "mock-enabled", havingValue = "false")
    OfficeService defaultOfficeService() {
        throw new UnsupportedOperationException("Default office service implementation not available yet");
    }

    /**
     * Bean for fx deals service with mocked data
     * @return FxDealService
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.fx-deal",
            name = "mock-enabled", havingValue = "true", matchIfMissing = true)
    FxDealService mockedFxDealService() {
        return new MockedFxDealService();
    }

    /**
     * Bean for fx deals service with real data
     * @return FxDealService
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.fx-deal", name = "mock-enabled", havingValue = "false")
    FxDealService defaultFxDealService() {
        return new SantanderFxDealService(fxDealGateway, santanderFxDealSerializer);
    }

    /**
     * Bean for Trade Special prices service with mocked data
     * @return  TradeSpecialPricesService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.special-prices",
            name = "mock-enabled", havingValue = "true", matchIfMissing = true)
    TradeSpecialPricesService mockedTradeSpecialPricesService() {
        return new MockedTradeSpecialPricesService();
    }

    /**
     * Bean for Trade Special prices service with real data
     * @return  TradeSpecialPricesService object
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.special-prices",
            name = "mock-enabled", havingValue = "false")
    TradeSpecialPricesService defaultTradeSpecialPricesService() {
        return new SantanderTradeSpecialPricesService(tradeSpecialPricesGateway, santanderTradeSpecialPricesSerializer);
    }

    /**
     * Bean to create a new currency service.
     *
     * @return currency service
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.repository", name = "mock-enabled", havingValue = "true", matchIfMissing = true)
    CurrencyService mockedCurrencyService() {
        return new MockedCurrencyService(mapper);
    }

    /**
     * Bean to create a new Country Holidays service.
     *
     * @return Country Holidays service
     */
    @Bean
    @ConditionalOnProperty(prefix = "one-trade.integrations.repository", name = "mock-enabled", havingValue = "true", matchIfMissing = true)
    CountryHolidayService mockedCountryHolidayService() {
        return new MockedCountryHolidayService();
    }
}
