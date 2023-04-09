package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;

import java.io.IOException;

import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeStringFieldOrNull;

/**
 * Class to serialize the user info
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @since jdk-11.0.13
 */
public class UserInfoStdSerializer extends StdSerializer<UserInfo> {

    /**
     * Class constructor
     */
    public UserInfoStdSerializer() {
        super((Class<UserInfo>) null);
    }

    /**
     * Method to serialize data
     * @param userInfo an UserInfo object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(UserInfo userInfo,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        generator.writeFieldName("user");
        UserStdSerializer userStdSerializer = new UserStdSerializer();
        userStdSerializer.serialize(userInfo.getUser(), generator, serializerProvider);
        writeStringFieldOrNull(generator, "country", userInfo.getCountry());
        writeStringFieldOrNull(generator, "middleOffice", userInfo.getMiddleOffice());
        writeStringFieldOrNull(generator, "office", userInfo.getOffice());
        writeStringFieldOrNull(generator, "mail", userInfo.getMail());
        generator.writeEndObject();
    }
}
