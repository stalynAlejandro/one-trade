package com.pagonxt.onetradefinance.external.backend.service.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;

import java.io.IOException;
import java.text.DateFormat;

import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeDateFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeNumberFieldOrNull;
import static com.pagonxt.onetradefinance.external.backend.utils.SerializerUtils.writeStringFieldOrNull;

/**
 * Class to serialize a request of export collections
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest
 * @see com.fasterxml.jackson.databind.ser.std.StdSerializer
 * @since jdk-11.0.13
 */
public class ExportCollectionRequestStdSerializer extends StdSerializer<ExportCollectionRequest> {

    /**
     * Class attributes
     */
    private final DateFormatProperties dateFormatProperties;

    private final DateFormat dateTimeFormat;

    private final DateFormat dateFormat;

    /**
     * Class constructor
     * @param dateFormatProperties object to serialize dateTime and TimeZone
     */
    public ExportCollectionRequestStdSerializer(DateFormatProperties dateFormatProperties) {
        super((Class<ExportCollectionRequest>) null);
        this.dateFormatProperties = dateFormatProperties;
        this.dateTimeFormat = dateFormatProperties.getDateFormatInstance();
        this.dateFormat = dateFormatProperties.getDateFormatInstance("yyyy-MM-dd");
    }

    /**
     * Method to serialize data
     * @param request an ExportCollectionRequest object
     * @param generator Writes JSON data to an output source in a streaming way.
     * @param serializerProvider defines API used by ObjectMapper and JsonSerializers to obtain serializers
     *                           capable of serializing instances of specific types; as well as the default
     *                           implementation of the functionality.
     * @throws IOException handles IO exceptions
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.databind.SerializerProvider
     */
    @Override
    public void serialize(ExportCollectionRequest request,
                          JsonGenerator generator,
                          SerializerProvider serializerProvider) throws IOException {
        PagoNxtRequestStdSerializer pagoNxtRequestStdSerializer = new PagoNxtRequestStdSerializer(dateFormatProperties);
        pagoNxtRequestStdSerializer.serialize(request, generator, serializerProvider);
        writeDateFieldOrNull(generator, "slaEnd", request.getSlaEnd(), dateTimeFormat);
        writeNumberFieldOrNull(generator, "savedStep", request.getSavedStep());
        generator.writeBooleanField("clientAcceptance", request.isClientAcceptance());
        writeStringFieldOrNull(generator, "clientReference", request.getClientReference());
        writeStringFieldOrNull(generator, "debtorName", request.getDebtorName());
        writeStringFieldOrNull(generator, "debtorBank", request.getDebtorBank());
        writeStringFieldOrNull(generator, "collectionType", request.getCollectionType());
        writeStringFieldOrNull(generator, "currency", request.getCurrency());
        writeNumberFieldOrNull(generator, "amount", request.getAmount());
        generator.writeBooleanField("customerHasAccount", request.isCustomerHasAccount());
        AccountStdSerializer accountStdSerializer = new AccountStdSerializer();
        generator.writeFieldName("nominalAccount");
        accountStdSerializer.serialize(request.getNominalAccount(), generator, serializerProvider);
        generator.writeFieldName("commissionAccount");
        accountStdSerializer.serialize(request.getCommissionAccount(), generator, serializerProvider);
        generator.writeBooleanField("applyingForAdvance", request.isApplyingForAdvance());
        generator.writeFieldName("advanceRiskLine");
        RiskLineStdSerializer riskLineStdSerializer = new RiskLineStdSerializer(dateFormatProperties);
        riskLineStdSerializer.serialize(request.getAdvanceRiskLine(), generator, serializerProvider);
        writeStringFieldOrNull(generator, "advanceCurrency", request.getAdvanceCurrency());
        writeNumberFieldOrNull(generator, "advanceAmount", request.getAdvanceAmount());
        writeDateFieldOrNull(generator, "advanceExpiration", request.getAdvanceExpiration(), dateFormat);
        generator.writeFieldName("customer");
        CustomerStdSerializer customerStdSerializer = new CustomerStdSerializer();
        customerStdSerializer.serialize(request.getCustomer(), generator, serializerProvider);
        generator.writeEndObject();
    }
}
