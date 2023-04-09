package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.RiskLineDto;
import com.pagonxt.onetradefinance.external.backend.api.model.RiskLinesList;
import com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.utils.QueryUtils;
import com.pagonxt.onetradefinance.integrations.service.RiskLineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.util.LogUtils.sanitizeLog;

/**
 * Controller with the endpoints to get a list of risk lines by client
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.RiskLineDtoSerializer
 * @see com.pagonxt.onetradefinance.integrations.service.RiskLineService
 * @see com.pagonxt.onetradefinance.external.backend.utils.QueryUtils
 * @since jdk-11.0.13
 */
@RestController
@Validated
@RequestMapping("${controller.path}/risk-lines")
public class RiskLineController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(RiskLineController.class);

    /**
     * Class attributes
     */
    private final RiskLineService riskLineService;
    private final RiskLineDtoSerializer riskLineDtoSerializer;
    private final QueryUtils queryUtils;

    /**
     * Risk Line Controller Constructor
     * @param riskLineService Service that provides necessary functionality to this controller
     * @param riskLineDtoSerializer Component for the conversion and adaptation of the data structure
     * @param queryUtils queryUtils
     */
    public RiskLineController(RiskLineService riskLineService, RiskLineDtoSerializer riskLineDtoSerializer,
                              QueryUtils queryUtils) {
        this.riskLineService = riskLineService;
        this.riskLineDtoSerializer = riskLineDtoSerializer;
        this.queryUtils = queryUtils;
    }

    /**
     * This method returns a list with risk lines by client
     * @param client a String with client id
     * @param productId a String with product id (for example, CLE)
     * @param expirationDate a String with expiration date
     * @param operationAmount a double value with the operation amount
     * @param operationCurrency a String with the operation currency
     * @return a list with risk lines
     */
    @GetMapping
    @Secured("ROLE_USER")
    public RiskLinesList getRiskLinesByClientId(@RequestParam @NotEmpty String client,
                                                @RequestParam (name = "product_id") @NotEmpty String productId,
                                                @RequestParam(name = "expiration_date",required = false) String expirationDate,
                                                @RequestParam(name = "operation_amount",required = false) Double operationAmount,
                                                @RequestParam(name = "operation_currency",required = false) String operationCurrency) {
        if(expirationDate == null || operationAmount == null || operationCurrency == null) {
            return new RiskLinesList(new ArrayList<>());
        }
        List<RiskLineDto> riskLines = riskLineService.getCustomerRiskLines(
                queryUtils.generateRiskLineQuery(client, productId, expirationDate, operationAmount, operationCurrency))
                .stream().map(riskLineDtoSerializer::toDto).collect(Collectors.toList());
        RiskLinesList result = new RiskLinesList(riskLines);
        if (LOG.isDebugEnabled()) {
            LOG.debug("getRiskLinesByClientId(client: {}) returned: {}", sanitizeLog(client), result);
        }
        return result;
    }

}
