package com.pagonxt.onetradefinance.external.backend.api.serializer.cli;

import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;

public interface TradeRequestDtoSerializer {

    TradeRequest toModel(CommonRequestDto request);
    CommonRequestDto toDto(TradeRequest tradeRequest);
}
