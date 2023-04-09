package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.CustomerQuery;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.repository.CompanyAddressesRepository;
import com.pagonxt.onetradefinance.integrations.repository.CompanyDataRepository;
import com.pagonxt.onetradefinance.integrations.repository.CompanyDataSpecification;
import com.pagonxt.onetradefinance.integrations.repository.entity.*;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import io.jsonwebtoken.lang.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SantanderCustomerService implements CustomerService{
    private static final Logger LOG = LoggerFactory.getLogger(SantanderCustomerService.class);
    public static final String CONNECTION_FAILED = "Connection with repository failed";
    public static final String ERROR_REPOSITORY = "errorRepository";
    private final CompanyAddressesRepository companyAddressesRepository;
    private final CompanyDataRepository companyDataRepository;
    public SantanderCustomerService(CompanyAddressesRepository companyAddressesRepository, CompanyDataRepository companyDataRepository) {
        this.companyAddressesRepository = companyAddressesRepository;
        this.companyDataRepository = companyDataRepository;
    }

    /**
     * This method allows to obtain a list of customers
     *
     * @param search a string with the customer id
     * @return a list of customers
     * @see CustomerQuery
     */
    @Override
    public List<Customer> searchCustomers(CustomerQuery search) {
        CompanyDataSpecification companyDataSpecification = new CompanyDataSpecification(search);
        // Hardcoded pagination
        Pageable pageable = PageRequest.of(0, 10, Sort.by("companyName"));
        Page<CompanyDataDAO> companyDataDAOList;
        try {
            companyDataDAOList = companyDataRepository.findAll(companyDataSpecification, pageable);
        } catch (Exception e) {
            LOG.error(CONNECTION_FAILED, e);
            throw new ServiceException(CONNECTION_FAILED, ERROR_REPOSITORY);
        }
        return companyDataDAOList.stream().map(this::mapToCustomer).collect(Collectors.toList());
    }

    /**
     * This method allows to obtain a customer through a person number
     *
     * @param personNumber a string with the person number
     * @return a Customer object with the customer
     */
    @Override
    public Customer getCustomerByPersonNumber(String personNumber) {
        List<CompanyDataDAO> companyDataList;
        try {
            companyDataList = companyDataRepository.findByCustomerLegalId(personNumber);
        } catch (Exception e) {
            LOG.error(CONNECTION_FAILED, e);
            throw new ServiceException(CONNECTION_FAILED, ERROR_REPOSITORY);
        }
        return (companyDataList != null && !companyDataList.isEmpty()) ? this.mapToCustomer(companyDataList.get(0)) : null;
    }

    /**
     * Method to search a company address by ID.
     *
     * @param companyAddressesId : the id
     *
     * @return the found company address
     */
    @Override
    public CompanyAddressesDAO getCustomerAddressByCompanyAddressesId(CompanyAddressesId companyAddressesId) {
        try{
            Optional<CompanyAddressesDAO> address = companyAddressesRepository.findById(companyAddressesId);
            if(address.isEmpty()) {
                LOG.info("Company address not found. companyAddressesId : [{}]", companyAddressesId);
                return null;
            }
            return address.get();
        } catch(Exception e) {
            LOG.error(CONNECTION_FAILED, e);
            throw new ServiceException(CONNECTION_FAILED, ERROR_REPOSITORY);
        }
    }
    private Customer mapToCustomer(CompanyDataDAO companyDataDAO){
        if (companyDataDAO == null){
            return null;
        }
        Customer customer = new Customer();
        customer.setCustomerId(companyDataDAO.getCompanyDataId().getCompanyGlobalId());
        customer.setCountryISO(companyDataDAO.getCompanyDataId().getCountryIsoCode());
        customer.setEmail("");
        customer.setName(Strings.emptyIfNull(companyDataDAO.getCompanyName()));
        customer.setSegment(Strings.emptyIfNull(companyDataDAO.getEmoneyCustomerSegment()));
        customer.setPersonNumber(Strings.emptyIfNull(companyDataDAO.getCustomerLegalId()));
        customer.setOffice("");
        if (!Collections.isEmpty(companyDataDAO.getCompanyFiscalDocumentsDAOS())) {
            customer.setTaxId(companyDataDAO.getCompanyFiscalDocumentsDAOS().get(0).getCompanyFiscalDocumentsId().getDocumentNumber());
        }
        return customer;
    }
}
