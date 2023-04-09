package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;

import java.io.IOException;
import java.text.DateFormat;
import java.util.List;

import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeDateFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeNumberFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeStringFieldOrNull;

/**
 * Class to serialize an advance request of export collections
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @since jdk-11.0.13
 */
public class ExportCollectionAdvanceRequestStdSerializer extends StdSerializer<ExportCollectionAdvanceRequest> {

    /**
     * Class attributes
     */
    private final DateFormatProperties dateFormatProperties;
    private final DateFormat dateFormat;

    /**
     * Class constructor
     * @param dateFormatProperties object to serialize dateTime and TimeZone
     */
    public ExportCollectionAdvanceRequestStdSerializer(DateFormatProperties dateFormatProperties) {
        super((Class<ExportCollectionAdvanceRequest>) null);
        this.dateFormatProperties = dateFormatProperties;
        this.dateFormat = dateFormatProperties.getDateFormatInstance("yyyy-MM-dd");
    }

    /**
     * Method class to serialize data
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
    public void serialize(ExportCollectionAdvanceRequest request,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        PagoNxtRequestStdSerializer pagoNxtRequestStdSerializer = new PagoNxtRequestStdSerializer(dateFormatProperties);
        pagoNxtRequestStdSerializer.serialize(request, generator, serializerProvider);
        writeNumberFieldOrNull(generator, "savedStep", request.getSavedStep());
        generator.writeFieldName("customer");
        CustomerStdSerializer customerStdSerializer = new CustomerStdSerializer();
        customerStdSerializer.serialize(request.getCustomer(), generator, serializerProvider);
        generator.writeFieldName("exportCollection");
        ExportCollectionStdSerializer exportCollectionStdSerializer =
                new ExportCollectionStdSerializer(dateFormatProperties);
        exportCollectionStdSerializer.serialize(request.getExportCollection(), generator, serializerProvider);
        writeNumberFieldOrNull(generator, "amount", request.getAmount());
        writeStringFieldOrNull(generator, "currency", request.getCurrency());
        // Exchange Insurances Array
        generator.writeFieldName("exchangeInsurances");
        generator.writeStartArray();
        List<ExchangeInsurance> exchangeInsurances = request.getExchangeInsurances();
        ExchangeInsuranceStdSerializer exchangeInsuranceDtoSerializer =
                new ExchangeInsuranceStdSerializer(dateFormatProperties);
        for (ExchangeInsurance exchangeInsurance : exchangeInsurances) {
            exchangeInsuranceDtoSerializer.serialize(exchangeInsurance, generator, serializerProvider);
        }
        generator.writeEndArray();
        writeDateFieldOrNull(generator, "exchangeInsuranceUseDate", request.getExchangeInsuranceUseDate(), dateFormat);
        writeNumberFieldOrNull(generator, "exchangeInsuranceAmountToUse", request.getExchangeInsuranceAmountToUse());
        writeStringFieldOrNull(generator, "exchangeInsuranceBuyCurrency", request.getExchangeInsuranceBuyCurrency());
        writeStringFieldOrNull(generator, "exchangeInsuranceSellCurrency", request.getExchangeInsuranceSellCurrency());
        generator.writeFieldName("riskLine");
        RiskLineStdSerializer riskLineStdSerializer = new RiskLineStdSerializer(dateFormatProperties);
        riskLineStdSerializer.serialize(request.getRiskLine(), generator, serializerProvider);
        writeDateFieldOrNull(generator, "expiration", request.getExpiration(), dateFormat);
        generator.writeEndObject();
    }
}
