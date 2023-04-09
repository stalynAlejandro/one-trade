package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeDateFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeStringFieldOrNull;

/**
 * Class to serialize a PagoNxt Request
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @see com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest
 * @since jdk-11.0.13
 */
public class PagoNxtRequestStdSerializer extends StdSerializer<PagoNxtRequest> {

    /**
     * Class attributes
     */
    private final DateFormatProperties dateFormatProperties;
    private final DateFormat dateTimeFormat;

    /**
     * Constructor
     * @param dateFormatProperties object to serialize dateTime and TimeZone
     */
    public PagoNxtRequestStdSerializer(DateFormatProperties dateFormatProperties) {
        super((Class<PagoNxtRequest>) null);
        this.dateFormatProperties = dateFormatProperties;
        this.dateTimeFormat = dateFormatProperties.getDateFormatInstance();
    }

    /**
     * Method to serialize data
     * @param request a PagoNxtRequest object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(PagoNxtRequest request,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        generator.writeFieldName("requester");
        UserInfoStdSerializer userInfoStdSerializer = new UserInfoStdSerializer();
        userInfoStdSerializer.serialize(request.getRequester(), generator, serializerProvider);
        writeStringFieldOrNull(generator, "code", request.getCode());
        writeDateFieldOrNull(generator, "creationDate", request.getCreationDate(), dateTimeFormat);
        writeStringFieldOrNull(generator, "product", request.getProduct());
        writeStringFieldOrNull(generator, "event", request.getEvent());
        writeStringFieldOrNull(generator, "country", request.getCountry());
        writeStringFieldOrNull(generator, "priority", request.getPriority());
        writeStringFieldOrNull(generator, "office", request.getOffice());
        writeStringFieldOrNull(generator, "comment", request.getComment());
        // Document Array
        generator.writeFieldName("documents");
        generator.writeStartArray();
        List<Document> documents = request.getDocuments();
        DocumentStdSerializer documentStdSerializer = new DocumentStdSerializer(dateFormatProperties);
        for (Document document : documents) {
            documentStdSerializer.serialize(document, generator, serializerProvider);
        }
        generator.writeEndArray();
    }
}
