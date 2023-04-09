package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance;

import java.io.IOException;
import java.text.DateFormat;

import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeDateFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeNumberFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeStringFieldOrNull;

/**
 * Class to serialize an advance of export collections
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvance
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceStdSerializer extends StdSerializer<ExportCollectionAdvance> {

    /**
     * Class attributes
     */
    private final DateFormatProperties dateFormatProperties;
    private final DateFormat dateTimeFormat;

    /**
     * Class constructor
     * @param dateFormatProperties object to serialize dateTime and TimeZone
     */
    public ExportCollectionAdvanceStdSerializer(DateFormatProperties dateFormatProperties) {
        super((Class<ExportCollectionAdvance>) null);
        this.dateFormatProperties = dateFormatProperties;
        this.dateTimeFormat = dateFormatProperties.getDateFormatInstance();
    }

    /**
     * Method to serialize data
     * @param request an ExportCollectionAdvanceRequest object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(ExportCollectionAdvance request,
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
        ExportCollectionStdSerializer exportCollectionStdSerializer =
                new ExportCollectionStdSerializer(dateFormatProperties);
        generator.writeFieldName("exportCollection");
        exportCollectionStdSerializer.serialize(request.getExportCollection(), generator, serializerProvider);
        generator.writeEndObject();
    }
}
