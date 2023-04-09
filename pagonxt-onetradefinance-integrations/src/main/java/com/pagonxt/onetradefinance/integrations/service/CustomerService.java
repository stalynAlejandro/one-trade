package com.pagonxt.onetradefinance.integrations.service;

import com.pagonxt.onetradefinance.integrations.model.Customer;
import com.pagonxt.onetradefinance.integrations.model.CustomerQuery;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesDAO;
import com.pagonxt.onetradefinance.integrations.repository.entity.CompanyAddressesId;

import java.util.List;

/**
 * This interface provides a way of a client to interact with some functionality in the application.
 * In this case provides two method to get customers
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.Customer
 * @since jdk-11.0.13
 */
public interface CustomerService {

    /**
     * This method allows to obtain a list of customers
     * @param search a string with the customer id
     * @see com.pagonxt.onetradefinance.integrations.model.CustomerQuery
     * @return a list of customers
     */
    List<Customer> searchCustomers(CustomerQuery search);

    /**
     * This method allows to obtain a customer through a person number
     * @param personNumber a string with the person number
     * @return a Customer object with the customer
     */
    Customer getCustomerByPersonNumber(String personNumber);

    /**
     * Method to find the address of a customer by its id.
     *
     * @param companyAddressesId    : the id of the address
     * @return the address
     */
    CompanyAddressesDAO getCustomerAddressByCompanyAddressesId(CompanyAddressesId companyAddressesId);
}
