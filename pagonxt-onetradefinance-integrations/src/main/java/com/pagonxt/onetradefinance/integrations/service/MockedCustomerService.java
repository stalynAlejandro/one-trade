package com.pagonxt.onetradefinance.integrations.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.CustomerQuery;
import com.pagonxt.onetradefinance.integrations.model.ValidationError;
import com.pagonxt.onetradefinance.integrations.model.exception.IntegrationException;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesDAO;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesId;
import com.pagonxt.onetradefinance.integrations.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class provides a way of a client to interact with some functionality in the application.
 * In this case provides some methods to get mocked customers
 * @author -
 * @version jdk-11.0.13
 * @see CustomerService
 * @see com.pagonxt.onetradefinance.integrations.model.Customer
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see ValidationUtil
 * @since jdk-11.0.13
 */
public class MockedCustomerService implements CustomerService {

    /**
     * A Logger object is used to log messages for a specific system or application component
     */
    private static final Logger LOG = LoggerFactory.getLogger(MockedCustomerService.class);
    private final List<Customer> customers = new ArrayList<>();

    private final ValidationUtil validationUtils;

    /**
     * constructor method
     * @param mapper an ObjectMapper object, that provides functionality for reading and writing JSON
     * @param validationUtils a Validation utils object to validate some data
     */
    public MockedCustomerService(ObjectMapper mapper, ValidationUtil validationUtils) {
        this.validationUtils = validationUtils;
        try (InputStream resourceStream = this.getClass().getClassLoader()
                .getResourceAsStream("mock-data/customers.json")) {
            customers.addAll(mapper.readValue(resourceStream, new TypeReference<>() {}));
            LOG.debug("Mock customer repository initialized with {} items", customers.size());
        } catch (Exception e) {
            LOG.warn("Error reading customer file, mock repository will be empty", e);
        }
    }

    /**
     * This method allows searching customers
     * @param search a CustomerQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.CustomerQuery
     * @return a list of costumers
     */
    public List<Customer> searchCustomers(CustomerQuery search) {
        validateSearch(search);
        Stream<Customer> result = customers.parallelStream();
        if (StringUtils.hasText(search.getName())) {
            if ("Pepsi".equals(search.getName())) {
                throw new IntegrationException();
            }
            result = result.filter(customer -> contains(customer.getName(), search.getName()));
        }
        if (StringUtils.hasText(search.getTaxId())) {
            result = result.filter(customer -> contains(customer.getTaxId(), search.getTaxId()));
        }
        if (search.getOffice() != null) {
            result = result.filter(customer -> search.getOffice().equals(customer.getOffice()));
        }
        if (StringUtils.hasText(search.getPersonNumber())) {
            result = result.filter(customer -> contains(customer.getPersonNumber(), search.getPersonNumber()));
        }
        return result.collect(Collectors.toList());
    }

    /**
     * This method allows to obtain a customer through a person number
     * @param personNumber a string with the person number
     * @return a Customer object with the customer
     */
    @Override
    public Customer getCustomerByPersonNumber(String personNumber) {
        return customers.parallelStream()
                .filter(customer -> customer.getPersonNumber().equals(personNumber))
                .findFirst().orElse(null);
    }

    /**
     * Method to return a mocked company address.
     *
     * @param companyAddressesId : the id
     *
     * @return the mocked company address
     */
    @Override
    public CompanyAddressesDAO getCustomerAddressByCompanyAddressesId(CompanyAddressesId companyAddressesId) {
        CompanyAddressesDAO companyAddressesDAO = new CompanyAddressesDAO();
        companyAddressesDAO.setCompanyAddressesId(companyAddressesId);
        companyAddressesDAO.setCountry(companyAddressesId.getAccess());
        companyAddressesDAO.setCounty(companyAddressesId.getAccess());
        companyAddressesDAO.setStreet("CL SERRANO");
        companyAddressesDAO.setStreetBuilding("32");
        final String MADRID = "MADRID";
        companyAddressesDAO.setDistrict(MADRID);
        companyAddressesDAO.setPostCode("28001");
        companyAddressesDAO.setTown(MADRID);
        companyAddressesDAO.setProvince(MADRID);
        companyAddressesDAO.setRegion(MADRID);

        return companyAddressesDAO;
    }

    /**
     * This method allows checking if the entered text has any matches
     * @param text   : a string with the text
     * @param search : a string with the search
     * @return true or false if they are any match
     */
    private boolean contains(String text, String search) {
        return text!=null && text.toLowerCase().contains(search.trim().toLowerCase());
    }

    /**
     * This method allows to validate a search
     * @param search a CustomerQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.CustomerQuery
     */
    private void validateSearch(CustomerQuery search) {
        List<ValidationError> validationErrors = new ArrayList<>();
        validationErrors = validationUtils.runValidations(search, "customerSearch", validationErrors);
        if(!validationErrors.isEmpty()) {
            throw new InvalidRequestException("There are validation errors",
                    "validationError", new ArrayList<>(validationErrors));
        }
    }
}
