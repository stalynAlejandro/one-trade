package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest;

import java.io.IOException;

/**
 * Class to serialize an advance cancellation' request of export collections
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceCancellationRequestStdSerializer
        extends StdSerializer<ExportCollectionAdvanceCancellationRequest> {

    /**
     * class attributes
     */
    private final DateFormatProperties dateFormatProperties;

    /**
     * Class constructor
     * @param dateFormatProperties object to serialize dateTime and TimeZone
     */
    public ExportCollectionAdvanceCancellationRequestStdSerializer(DateFormatProperties dateFormatProperties) {
        super((Class<ExportCollectionAdvanceCancellationRequest>) null);
        this.dateFormatProperties = dateFormatProperties;
    }

    /**
     * Method class to serialize data
     * @param request an ExportCollectionAdvanceCancellationRequest object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(ExportCollectionAdvanceCancellationRequest request,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        PagoNxtRequestStdSerializer pagoNxtRequestStdSerializer = new PagoNxtRequestStdSerializer(dateFormatProperties);
        pagoNxtRequestStdSerializer.serialize(request, generator, serializerProvider);
        generator.writeFieldName("customer");
        CustomerStdSerializer customerStdSerializer = new CustomerStdSerializer();
        customerStdSerializer.serialize(request.getCustomer(), generator, serializerProvider);
        generator.writeFieldName("exportCollectionAdvance");
        ExportCollectionAdvanceStdSerializer exportCollectionAdvanceStdSerializer =
                new ExportCollectionAdvanceStdSerializer(dateFormatProperties);
        exportCollectionAdvanceStdSerializer.serialize
                (request.getExportCollectionAdvance(),generator, serializerProvider);
        generator.writeEndObject();
    }
}
