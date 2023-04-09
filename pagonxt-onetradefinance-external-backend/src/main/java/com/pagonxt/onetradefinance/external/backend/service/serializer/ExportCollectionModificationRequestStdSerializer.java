package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest;

import java.io.IOException;

/**
 * Class to serialize a modification request of export collections
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @since jdk-11.0.13
 */
public class ExportCollectionModificationRequestStdSerializer extends
        StdSerializer<ExportCollectionModificationRequest> {

    /**
     * Class attributes
     */
    private final DateFormatProperties dateFormatProperties;

    /**
     * Class constructor
     * @param dateFormatProperties object to serialize dateTime and TimeZone
     */
    public ExportCollectionModificationRequestStdSerializer(DateFormatProperties dateFormatProperties) {
        super((Class<ExportCollectionModificationRequest>) null);
        this.dateFormatProperties = dateFormatProperties;
    }

    /**
     * Method to serialize data
     * @param request an ExportCollectionModificationRequest object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(ExportCollectionModificationRequest request,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        PagoNxtRequestStdSerializer pagoNxtRequestStdSerializer = new PagoNxtRequestStdSerializer(dateFormatProperties);
        pagoNxtRequestStdSerializer.serialize(request, generator, serializerProvider);
        generator.writeFieldName("customer");
        CustomerStdSerializer customerStdSerializer = new CustomerStdSerializer();
        customerStdSerializer.serialize(request.getCustomer(), generator, serializerProvider);
        generator.writeFieldName("exportCollection");
        ExportCollectionStdSerializer exportCollectionStdSerializer =
                new ExportCollectionStdSerializer(dateFormatProperties);
        exportCollectionStdSerializer.serialize(request.getExportCollection(), generator, serializerProvider);
        generator.writeEndObject();
    }
}
