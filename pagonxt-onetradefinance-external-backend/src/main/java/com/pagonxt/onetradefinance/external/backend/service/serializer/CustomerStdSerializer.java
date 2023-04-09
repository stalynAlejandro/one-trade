package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.model.Customer;

import java.io.IOException;

import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeStringFieldOrNull;

/**
 * Class to serialize customers
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @see com.pagonxt.onetradefinance.integrations.model.Customer
 * @since jdk-11.0.13
 */
public class CustomerStdSerializer extends StdSerializer<Customer> {

    /**
     * Class constructor
     */
    public CustomerStdSerializer() {
        super((Class<Customer>) null);
    }

    /**
     * Method class to serialize data
     * @param customer a customer object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(Customer customer,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        writeStringFieldOrNull(generator, "customerId", customer.getCustomerId());
        writeStringFieldOrNull(generator, "name", customer.getName());
        writeStringFieldOrNull(generator, "taxId", customer.getTaxId());
        writeStringFieldOrNull(generator, "office", customer.getOffice());
        writeStringFieldOrNull(generator, "personNumber", customer.getPersonNumber());
        writeStringFieldOrNull(generator, "segment", customer.getSegment());
        writeStringFieldOrNull(generator, "email", customer.getEmail());
        generator.writeEndObject();
    }

}
