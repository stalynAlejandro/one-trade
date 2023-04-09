package com.pagonxt.onetradefinance.integrations.apis.riskline.serializer;

import com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineList;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineListResponse;
import com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineResponse;
import com.pagonxt.onetradefinance.integrations.model.RiskLine;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Class to serialize Santander risk lines data
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class SantanderRiskLineSerializer {

    /**
     * Class method to serialize a list of risk lines
     * @param santanderRiskLineResponse a SantanderRiskLineListResponse object
     * @see com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineListResponse
     * @see com.pagonxt.onetradefinance.integrations.model.RiskLine
     * @return an empty list or a list of risk lines
     */
    public List<RiskLine> toModel(SantanderRiskLineListResponse santanderRiskLineResponse) {
        if(santanderRiskLineResponse == null || santanderRiskLineResponse.getRiskLines() == null) {
            return Collections.emptyList();
        }

        List<RiskLine> riskLine = santanderRiskLineResponse.getRiskLines().stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        riskLine.forEach(line -> line.setClient(santanderRiskLineResponse.getCustomerId()));

        return riskLine;
    }

    /**
     * class method to serialize a risk line
     * @param santanderRiskLine a RiskLineList object
     * @see com.pagonxt.onetradefinance.integrations.apis.riskline.model.RiskLineList
     * @see com.pagonxt.onetradefinance.integrations.model.RiskLine
     * @return a RiskLine object
     */
    public RiskLine toModel(RiskLineList santanderRiskLine) {
        if (santanderRiskLine == null) {
            return new RiskLine();
        }

        RiskLine riskLine = new RiskLine();
        riskLine.setRiskLineId(santanderRiskLine.getRiskLineId());
        riskLine.setCurrency(santanderRiskLine.getAvailableAmount().getCurrency());
        riskLine.setIban(santanderRiskLine.getRiskLineId());
        riskLine.setExpires(toDate(santanderRiskLine.getMaturityDate()));
        riskLine.setAvailableAmount(String.format(Locale.ROOT, "%.5f", santanderRiskLine.getAvailableAmount()
                .getAmount()));
        riskLine.setLimitAmount(String.format(Locale.ROOT, "%.5f", santanderRiskLine.getLineAmount().getAmount()));
        riskLine.setStatus(santanderRiskLine.getRiskLineStatus());
        return riskLine;
    }

    /**
     * class method to serialize a risk line
     * @param santanderRiskLine a SantanderRiskLineResponse object
     * @see com.pagonxt.onetradefinance.integrations.apis.riskline.model.SantanderRiskLineListResponse
     * @see com.pagonxt.onetradefinance.integrations.model.RiskLine
     * @return a RiskLine object
     */
    public RiskLine toModel(SantanderRiskLineResponse santanderRiskLine) {
        if (santanderRiskLine == null) {
            return new RiskLine();
        }

        RiskLine riskLine = new RiskLine();
        riskLine.setRiskLineId(santanderRiskLine.getRiskLineId());
        riskLine.setIban(santanderRiskLine.getRiskLineId());
        riskLine.setCurrency(santanderRiskLine.getAvailableAmount().getCurrency());
        riskLine.setExpires(toDate(santanderRiskLine.getMaturityDate()));
        riskLine.setAvailableAmount(String.format(Locale.ROOT, "%.5f", santanderRiskLine.getAvailableAmount()
                .getAmount()));
        riskLine.setLimitAmount(String.format(Locale.ROOT, "%.5f", santanderRiskLine.getLineAmount().getAmount()));
        riskLine.setStatus(santanderRiskLine.getRiskLineStatus());
        riskLine.setClient(santanderRiskLine.getCustomerId());
        return riskLine;

    }

    private Date toDate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        return Date.from(localDate.atStartOfDay(ZoneId.of("Europe/Madrid")).toInstant());
    }
}
