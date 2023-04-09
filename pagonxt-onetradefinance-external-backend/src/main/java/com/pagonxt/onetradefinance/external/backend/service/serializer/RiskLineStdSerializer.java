package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;

import java.io.IOException;
import java.text.DateFormat;

import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeDateFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeStringFieldOrNull;

/**
 * Class to serialize a risk line
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.RiskLine
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @since jdk-11.0.13
 */
public class RiskLineStdSerializer extends StdSerializer<RiskLine> {

    /**
     * Class attributes
     */
    private final DateFormat dateFormat;

    /**
     * Class constructor
     * @param dateFormatProperties object to serialize dateTime and TimeZone
     */
    public RiskLineStdSerializer(DateFormatProperties dateFormatProperties) {
        super((Class<RiskLine>) null);
        this.dateFormat = dateFormatProperties.getDateFormatInstance("yyyy-MM-dd");
    }

    /**
     * Method to serialize data
     * @param riskLine A RiskLine object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(RiskLine riskLine,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        writeStringFieldOrNull(generator, "riskLineId", riskLine.getRiskLineId());
        writeStringFieldOrNull(generator, "client", riskLine.getClient());
        writeStringFieldOrNull(generator, "iban", riskLine.getIban());
        writeStringFieldOrNull(generator, "status", riskLine.getStatus());
        writeStringFieldOrNull(generator, "limitAmount", riskLine.getLimitAmount());
        writeStringFieldOrNull(generator, "availableAmount", riskLine.getAvailableAmount());
        writeDateFieldOrNull(generator, "expires", riskLine.getExpires(), dateFormat);
        writeStringFieldOrNull(generator, "currency", riskLine.getCurrency());
        generator.writeEndObject();
    }

}
