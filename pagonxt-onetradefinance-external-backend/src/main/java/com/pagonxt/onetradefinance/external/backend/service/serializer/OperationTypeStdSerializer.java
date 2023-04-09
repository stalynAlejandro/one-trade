package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.model.OperationType;

import java.io.IOException;

import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeStringFieldOrNull;

/**
 * Class to serialize accounts
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @see com.pagonxt.onetradefinance.integrations.model.OperationType
 * @since jdk-11.0.13
 */
public class OperationTypeStdSerializer extends StdSerializer<OperationType> {

    /**
     * Class constructor
     */
    public OperationTypeStdSerializer(){super((Class<OperationType>) null);}

    /**
     * Method to serialize data
     * @param operationType an operationType object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(OperationType operationType,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        writeStringFieldOrNull(generator, "id", operationType.getId());
        writeStringFieldOrNull(generator, "key", operationType.getKey());
        writeStringFieldOrNull(generator, "name", operationType.getName());
        writeStringFieldOrNull(generator, "product", operationType.getProduct());
        generator.writeEndObject();
    }

}
