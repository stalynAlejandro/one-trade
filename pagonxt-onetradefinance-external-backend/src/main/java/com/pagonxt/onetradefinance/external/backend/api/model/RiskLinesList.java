package com.pagonxt.onetradefinance.external.backend.api.model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines an object with a list of risk lines(dto)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto
 * @since jdk-11.0.13
 */
public class RiskLinesList extends ArrayList<RiskLineDto> {

    /**
     * Class constructor
     * @param riskLines list of risk lines
     */
    public RiskLinesList(List<RiskLineDto> riskLines) {
        super(riskLines);
    }
}
