package com.pagonxt.onetradefinance.integrations.configuration;

import com.pagonxt.onetradefinance.integrations.mapper.CountryHolidayMapper;
import com.pagonxt.onetradefinance.integrations.repository.CompanyAddressesRepository;
import com.pagonxt.onetradefinance.integrations.repository.CompanyDataRepository;
import com.pagonxt.onetradefinance.integrations.repository.CurrencyRepository;
import com.pagonxt.onetradefinance.integrations.repository.CountryHolidayRepository;
import com.pagonxt.onetradefinance.integrations.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * Configuration Class to create repository beans.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Configuration
@ConditionalOnProperty(prefix = "one-trade.integrations.repository", name = "mock-enabled", havingValue = "false")
public class RepositoryConfiguration {

    /**
     * Repository to get currency information.
     */
    private final CurrencyRepository currencyRepository;
    private final CountryHolidayRepository countryHolidayRepository;
    private final CountryHolidayMapper countryHolidayMapper;
    private final CompanyAddressesRepository companyAddressesRepository;
    private final CompanyDataRepository companyDataRepository;

    @Autowired
    @Qualifier(value="santanderEntityManagerFactory")
    protected EntityManager entityManager;

    /**
     * Constructor method.
     *
     * @param currencyRepository         : the currency repository
     * @param countryHolidayRepository   : the country holiday repository
     * @param countryHolidayMapper       : the country holiday mapper
     * @param companyAddressesRepository : the company addresses repository
     * @param companyDataRepository      : the company data repository
     */
    public RepositoryConfiguration(CurrencyRepository currencyRepository,
                                   CountryHolidayRepository countryHolidayRepository
            , CountryHolidayMapper countryHolidayMapper
            , CompanyAddressesRepository companyAddressesRepository, CompanyDataRepository companyDataRepository) {
        this.currencyRepository = currencyRepository;
        this.countryHolidayRepository = countryHolidayRepository;
        this.countryHolidayMapper = countryHolidayMapper;
        this.companyAddressesRepository = companyAddressesRepository;
        this.companyDataRepository = companyDataRepository;
    }

    /**
     * Bean to create a new currency service.
     *
     * @return currency service
     */
    @Bean
    CurrencyService defaultCurrencyService() {
        return new SantanderCurrencyService(currencyRepository);
    }

    /**
     * Bean to create a new country holiday service.
     *
     * @return country holiday service
     */
    @Bean
    CountryHolidayService defaultCountryHolidayService() {
        return new SantanderCountryHolidayService(countryHolidayRepository, countryHolidayMapper);
    }

    /**
     * Bean to create a new customer service.
     *
     * @return the customer service
     */
    @Bean
    CustomerService defaultCustomerService(){
        return new SantanderCustomerService(companyAddressesRepository, companyDataRepository);
    }
}
