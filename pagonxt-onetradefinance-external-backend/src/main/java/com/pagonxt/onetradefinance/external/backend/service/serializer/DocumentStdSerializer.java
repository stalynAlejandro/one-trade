package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.document.Document;

import java.io.IOException;
import java.text.DateFormat;

import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeDateFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeStringFieldOrNull;

/**
 * Class to serialize documents
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @see com.pagonxt.onetradefinance.integrations.model.document.Document
 * @since jdk-11.0.13
 */
public class DocumentStdSerializer extends StdSerializer<Document> {

    /**
     * Class attributes
     */
    private final DateFormat dateTimeFormat;

    /**
     * Class constructor
     * @param dateFormatProperties object to serialize dateTime and TimeZone
     */
    public DocumentStdSerializer(DateFormatProperties dateFormatProperties) {
        super((Class<Document>) null);
        this.dateTimeFormat = dateFormatProperties.getDateFormatInstance();
    }

    /**
     * Method class to serialize data
     * @param document a document object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(Document document,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        writeStringFieldOrNull(generator, "documentId", document.getDocumentId());
        writeStringFieldOrNull(generator, "filename", document.getFilename());
        writeStringFieldOrNull(generator, "mimeType", document.getMimeType());
        writeDateFieldOrNull(generator, "uploadedDate", document.getUploadedDate(), dateTimeFormat);
        writeStringFieldOrNull(generator, "documentType", document.getDocumentType());
        writeStringFieldOrNull(generator, "data", document.getData());
        generator.writeEndObject();
    }

}
