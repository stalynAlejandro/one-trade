package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest;

import java.io.IOException;

/**
 * Class to serialize an advance modification' request of export collections
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceModificationRequestStdSerializer
        extends StdSerializer<ExportCollectionAdvanceModificationRequest> {

    /**
     * Class attribute
     */
    private final DateFormatProperties dateFormatProperties;

    /**
     * Class constructor
     * @param dateFormatProperties object to serialize dateTime and TimeZone
     */
    public ExportCollectionAdvanceModificationRequestStdSerializer(DateFormatProperties dateFormatProperties) {
        super((Class<ExportCollectionAdvanceModificationRequest>) null);
        this.dateFormatProperties = dateFormatProperties;
    }

    /**
     * Method class to serialize data
     * @param request an ExportCollectionAdvanceModificationRequest object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(ExportCollectionAdvanceModificationRequest request,
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
        exportCollectionAdvanceStdSerializer
                .serialize(request.getExportCollectionAdvance(), generator, serializerProvider);
        generator.writeFieldName("riskLine");
        RiskLineStdSerializer riskLineStdSerializer = new RiskLineStdSerializer(dateFormatProperties);
        riskLineStdSerializer.serialize(request.getRiskLine(), generator, serializerProvider);
        generator.writeEndObject();
    }
}
