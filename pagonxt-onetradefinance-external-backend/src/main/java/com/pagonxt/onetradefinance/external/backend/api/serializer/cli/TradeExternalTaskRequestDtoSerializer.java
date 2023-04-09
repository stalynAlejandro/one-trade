package com.pagonxt.onetradefinance.external.backend.api.serializer.cli;

import com.pagonxt.onetradefinance.external.backend.api.model.cli.CompleteInfoTradeRequestDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CompleteInfoTradeRequestDtoSerializer;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeExternalTaskRequest;

public class TradeExternalTaskRequestDtoSerializer {

    /**
     * class attributes
     */
    private final TradeRequestDtoSerializer serializer;
    private final CompleteInfoTradeRequestDtoSerializer completeInfoTradeRequestDtoSerializer;

    public TradeExternalTaskRequestDtoSerializer(TradeRequestDtoSerializer serializer,
                                                 CompleteInfoTradeRequestDtoSerializer completeInfoTradeRequestDtoSerializer) {
        this.serializer = serializer;
        this.completeInfoTradeRequestDtoSerializer = completeInfoTradeRequestDtoSerializer;
    }

    /**
     * This method converts data from a entity class to a DTO class
     * @param completeInfoRequest a entity class
     * @see TradeExternalTaskRequest
     * @return null, if completeInfoRequest doesn't have data, or a DTO object
     */
    public CompleteInfoTradeRequestDto toDto(TradeExternalTaskRequest completeInfoRequest) {
        if(completeInfoRequest == null) {
            return null;
        }
        CompleteInfoTradeRequestDto completeInfoRequestDto = new CompleteInfoTradeRequestDto();
        completeInfoRequestDto.setRequest(serializer.toDto(completeInfoRequest.getRequest()));
        completeInfoTradeRequestDtoSerializer.toDto(completeInfoRequest, completeInfoRequestDto);
        return completeInfoRequestDto;
    }
}
