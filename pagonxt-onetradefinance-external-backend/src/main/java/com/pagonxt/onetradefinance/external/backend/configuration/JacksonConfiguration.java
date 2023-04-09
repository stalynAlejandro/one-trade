package com.pagonxt.onetradefinance.external.backend.configuration;

import com.pagonxt.onetradefinance.external.backend.service.serializer.DocumentStdSerializer;
import com.pagonxt.onetradefinance.external.backend.service.serializer.ExchangeInsuranceStdSerializer;
import com.pagonxt.onetradefinance.external.backend.service.serializer.ExportCollectionAdvanceCancellationRequestStdSerializer;
import com.pagonxt.onetradefinance.external.backend.service.serializer.ExportCollectionAdvanceModificationRequestStdSerializer;
import com.pagonxt.onetradefinance.external.backend.service.serializer.ExportCollectionAdvanceRequestStdSerializer;
import com.pagonxt.onetradefinance.external.backend.service.serializer.ExportCollectionModificationRequestStdSerializer;
import com.pagonxt.onetradefinance.external.backend.service.serializer.ExportCollectionOtherOperationsRequestStdSerializer;
import com.pagonxt.onetradefinance.external.backend.service.serializer.ExportCollectionRequestStdSerializer;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.ExchangeInsurance;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceCancellationRequest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionAdvanceRequest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionModificationRequest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionOtherOperationsRequest;
import com.pagonxt.onetradefinance.integrations.model.ExportCollectionRequest;
import com.pagonxt.onetradefinance.integrations.model.document.Document;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @see org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
 * @since jdk-11.0.13
 */
@Configuration
public class JacksonConfiguration implements Jackson2ObjectMapperBuilderCustomizer {

    /**
     * Class attribute
     */
    private DateFormatProperties dateFormatProperties;

    /**
     * Class constructor
     * @param dateFormatProperties Object used to serialize and deserialize the date time and the time zone
     */
    public JacksonConfiguration(DateFormatProperties dateFormatProperties) {
        this.dateFormatProperties = dateFormatProperties;
    }

    /**
     * Method used to serialize and deserialize the date time and the time zone
     * of the diferents cases (its serializers), document and exchangeInsurance
     * @param jacksonObjectMapperBuilder object
     */
    @Override
    public void customize(Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        jacksonObjectMapperBuilder
                .dateFormat(dateFormat)
                .serializerByType(Document.class, new DocumentStdSerializer(dateFormatProperties))
                .serializerByType(ExportCollectionRequest.class,
                        new ExportCollectionRequestStdSerializer(dateFormatProperties))
                .serializerByType(ExportCollectionModificationRequest.class,
                        new ExportCollectionModificationRequestStdSerializer(dateFormatProperties))
                .serializerByType(ExportCollectionAdvanceRequest.class,
                        new ExportCollectionAdvanceRequestStdSerializer(dateFormatProperties))
                .serializerByType(ExchangeInsurance.class, new ExchangeInsuranceStdSerializer(dateFormatProperties))
                .serializerByType(ExportCollectionAdvanceModificationRequest.class,
                        new ExportCollectionAdvanceModificationRequestStdSerializer(dateFormatProperties))
                .serializerByType(ExportCollectionAdvanceCancellationRequest.class,
                        new ExportCollectionAdvanceCancellationRequestStdSerializer(dateFormatProperties))
                .serializerByType(ExportCollectionOtherOperationsRequest.class,
                        new ExportCollectionOtherOperationsRequestStdSerializer(dateFormatProperties));
    }
}
