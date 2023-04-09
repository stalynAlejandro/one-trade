package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;

import java.io.IOException;
import java.text.DateFormat;

import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeDateFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeNumberFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeStringFieldOrNull;

/**
 * Class to serialize exchanges insurances
 * @author -
 * @version jdk-11.0.13
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @see com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance
 * @since jdk-11.0.13
 */
public class ExchangeInsuranceStdSerializer extends StdSerializer<ExchangeInsurance> {

    /**
     * Class attributes
     */
    private final DateFormat dateFormat;

    /**
     * Class constructor
     * @param dateFormatProperties object to serialize dateTime and TimeZone
     */
    public ExchangeInsuranceStdSerializer(DateFormatProperties dateFormatProperties) {
        super((Class<ExchangeInsurance>) null);
        this.dateFormat = dateFormatProperties.getDateFormatInstance("yyyy-MM-dd");
    }

    /**
     * Method class to serialize data
     * @param exchangeInsurance a exchangeInsurance object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(ExchangeInsurance exchangeInsurance,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        generator.writeStartObject();
        writeStringFieldOrNull(generator, "exchangeInsuranceId", exchangeInsurance.getExchangeInsuranceId());
        writeStringFieldOrNull(generator, "type", exchangeInsurance.getType());
        writeDateFieldOrNull(generator, "useDate", exchangeInsurance.getUseDate(), dateFormat);
        writeNumberFieldOrNull(generator, "sellAmount", exchangeInsurance.getSellAmount());
        writeStringFieldOrNull(generator, "sellCurrency", exchangeInsurance.getSellCurrency());
        writeNumberFieldOrNull(generator, "buyAmount", exchangeInsurance.getBuyAmount());
        writeStringFieldOrNull(generator, "buyCurrency", exchangeInsurance.getBuyCurrency());
        writeNumberFieldOrNull(generator, "exchangeRate", exchangeInsurance.getExchangeRate());
        writeNumberFieldOrNull(generator, "useAmount", exchangeInsurance.getUseAmount());
        generator.writeEndObject();
    }

}
