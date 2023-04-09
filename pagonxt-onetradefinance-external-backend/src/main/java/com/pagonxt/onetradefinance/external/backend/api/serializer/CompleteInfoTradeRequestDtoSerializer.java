package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.cli.CompleteInfoTradeRequestDto;
import com.pagonxt.onetradefinance.integrations.configuration.DateFormatProperties;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;
import org.springframework.stereotype.Component;

import java.text.DateFormat;

/**
 * This class has methods to convert a DTO class into an entity class and viceversa
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class CompleteInfoTradeRequestDtoSerializer {

    private final DateFormat dateTimeFormat;

    /**
     * Class constructor
     * @param dateFormatProperties To get the timezone a dateformat for serialize and deserialize data
     */
    public CompleteInfoTradeRequestDtoSerializer(DateFormatProperties dateFormatProperties) {
        dateTimeFormat = dateFormatProperties.getDateFormatInstance();
    }

    /**
     * Converts data from a completeInfoRequest to a completeInfoRequestDto (complete info task)
     * @param completeInfoRequest an entity object
     * @param completeInfoRequestDto a DTO object
     * @return null if any received parameter has no data or a DTO object
     */
    public CompleteInfoTradeRequestDto toDto(TradeExternalTaskRequest completeInfoRequest,
                                             CompleteInfoTradeRequestDto completeInfoRequestDto) {
        if (completeInfoRequestDto == null) {
            return null;
        }
        if (completeInfoRequest == null) {
            return completeInfoRequestDto;
        }
        completeInfoRequestDto.setReturnReason(completeInfoRequest.getReturnReason());
        completeInfoRequestDto.setReturnComment(completeInfoRequest.getReturnComment());
        if (completeInfoRequest.getRequestCreationTime() != null) {
            completeInfoRequestDto.setRequestCreationTime(dateTimeFormat.
                    format(completeInfoRequest.getRequestCreationTime()));
        }
        completeInfoRequestDto.setRequestCreatorName(completeInfoRequest.getRequestCreatorName());
        if (completeInfoRequest.getTaskCreationTime() != null) {
            completeInfoRequestDto.setTaskCreationTime(dateTimeFormat.
                    format(completeInfoRequest.getTaskCreationTime()));
        }
        return completeInfoRequestDto;
    }
}
