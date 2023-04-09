package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.CustomerDto;
import com.pagonxt.onetradefinance.integrations.model.Customer;
import org.springframework.stereotype.Component;

/**
 * This class has methods to convert DTO's into entities and viceversa, for customers
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class CustomerDtoSerializer {

    /**
     * This method converts a customerDto object to a customer object
     * @param customerDto customerDto object
     * @return customer object
     */
    public Customer toModel(CustomerDto customerDto) {
        if (customerDto == null) {
            return new Customer();
        }
        Customer customer = new Customer();
        customer.setCustomerId(customerDto.getCustomerId());
        customer.setName(customerDto.getName());
        customer.setTaxId(customerDto.getTaxId());
        customer.setOffice(customerDto.getOffice());
        customer.setPersonNumber(customerDto.getPersonNumber());
        customer.setEmail(customerDto.getEmail());
        customer.setSegment(customerDto.getSegment());
        return customer;
    }

    /**
     * This method converts a customer to a customerDto object
     * @param customer customer object
     * @return customerDto object
     */
    public CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDto clientDto = new CustomerDto();
        clientDto.setCustomerId(customer.getCustomerId());
        clientDto.setName(customer.getName());
        clientDto.setTaxId(customer.getTaxId());
        clientDto.setOffice(customer.getOffice());
        clientDto.setPersonNumber(customer.getPersonNumber());
        clientDto.setSegment(customer.getSegment());
        clientDto.setEmail(customer.getEmail());
        return clientDto;
    }
}
