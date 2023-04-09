package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollection;

import java.io.IOException;
import java.text.DateFormat;

import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeDateFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeNumberFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeStringFieldOrNull;

/**
 * Class to serialize a export collection
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollection
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @since jdk-11.0.13
 */
public class ExportCollectionStdSerializer extends StdSerializer<ExportCollection> {

    /**
     * Class attributes
     */
    private final DateFormat dateTimeFormat;

    /**
     * Class constructor
     * @param dateFormatProperties object to serialize dateTime and TimeZone
     */
    public ExportCollectionStdSerializer(DateFormatProperties dateFormatProperties) {
        super((Class<ExportCollection>) null);
        this.dateTimeFormat = dateFormatProperties.getDateFormatInstance();
    }

    /**
     * Method to serialize data
     * @param request an ExportCollection object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(ExportCollection request,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        writeStringFieldOrNull(generator, "code", request.getCode());
        writeDateFieldOrNull(generator, "creationDate", request.getCreationDate(), dateTimeFormat);
        writeStringFieldOrNull(generator, "contractReference", request.getContractReference());
        writeNumberFieldOrNull(generator, "amount", request.getAmount());
        writeStringFieldOrNull(generator, "currency", request.getCurrency());
        CustomerStdSerializer customerStdSerializer = new CustomerStdSerializer();
        generator.writeFieldName("customer");
        customerStdSerializer.serialize(request.getCustomer(), generator, serializerProvider);
        AccountStdSerializer accountStdSerializer = new AccountStdSerializer();
        generator.writeFieldName("nominalAccount");
        accountStdSerializer.serialize(request.getNominalAccount(), generator, serializerProvider);
        generator.writeEndObject();
    }
}
