package com.pagonxt.onetradefinance.work.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.bot.BotService;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.intents.Intent;
import com.flowable.action.api.repository.ActionDefinition;
import com.flowable.template.api.processor.TemplateProcessingResult;
import com.flowable.template.engine.TemplateEngine;
import com.pagonxt.onetradefinance.work.expression.common.LanguageCodeExpressions;
import com.pagonxt.onetradefinance.work.expression.common.MasterDataExpressions;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Class for the template of the email body
 * @author -
 * @version jdk-11.0.13
 * @see com.flowable.template.engine.TemplateEngine
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.pagonxt.onetradefinance.work.expression.common.MasterDataExpressions
 * @see com.pagonxt.onetradefinance.work.expression.common.LanguageCodeExpressions
 * @see com.flowable.action.api.bot.BotService
 * @since jdk-11.0.13
 */
@Component
public class EmailBodyTemplateBot implements BotService {

    private static final String BOT_KEY = "email-body-template";
    private static final String BOT_NAME = "Email body template";
    private static final String BOT_DESCRIPTION = "Get email body template";


    private static final String EMAIL_BODY_TEMPLATE_KEY = "PGN_TPL003";
    private static final String COUNTRY = "country";
    private static final String PRODUCT = "product";
    private static final String NOTIFICATION_TYPE = "notificationType";
    private static final String COMMON_ADHOC_NOTIFICATION_KEY = "commonAdHocNotification";
    private static final String LANGUAGE_CODE = "languageCode";
    private static final String MASTER_DATA_PRODUCT_TYPE = "md-product-type";
    private static final String MASTER_DATA_PRODUCT_NAME = "masterDataProductName";

    //class attributes
    protected final ObjectMapper objectMapper;
    private final TemplateEngine templateEngine;
    private final MasterDataExpressions masterDataExpressions;
    private final LanguageCodeExpressions languageCodeExpressions;

    /**
     * Constructor method
     * @param objectMapper              : an ObjectMapper object
     * @param templateEngine            : a TemplateEngine object
     * @param masterDataExpressions     : a MasterDataExpressions object
     * @param languageCodeExpressions   : a LanguageCodeExpressions object
     */
    public EmailBodyTemplateBot(ObjectMapper objectMapper,
                                TemplateEngine templateEngine,
                                MasterDataExpressions masterDataExpressions,
                                LanguageCodeExpressions languageCodeExpressions) {
        this.objectMapper = objectMapper;
        this.templateEngine = templateEngine;
        this.masterDataExpressions = masterDataExpressions;
        this.languageCodeExpressions = languageCodeExpressions;
    }

    /**
     * class method
     * @return a string with the value of BOT_KEY
     */
    @Override
    public String getKey() {
        return BOT_KEY;
    }

    /**
     * class method
     * @return a string with the value of BOT_NAME
     */
    @Override
    public String getName() {
        return BOT_NAME;
    }

    /**
     * class method
     * @return a string with the value of BOT_DESCRIPTION
     */
    @Override
    public String getDescription() {
        return BOT_DESCRIPTION;
    }

    /**
     * class method
     * @param actionInstance   : an HistoricActionInstance object
     * @param actionDefinition : an ActionDefinition object
     * @param payload          : payload
     * @see com.flowable.action.api.history.HistoricActionInstance
     * @see com.flowable.action.api.repository.ActionDefinition
     * @return a BotActionResult object
     */
    @Override
    public BotActionResult invokeBot(HistoricActionInstance actionInstance,
                                     ActionDefinition actionDefinition,
                                     Map<String, Object> payload) {

        String languageCode = languageCodeExpressions.getByCountry((String) payload.get(COUNTRY));

        Map<String, Object> variant = Map.of(NOTIFICATION_TYPE, COMMON_ADHOC_NOTIFICATION_KEY,
                LANGUAGE_CODE, languageCode);

        String masterDataProductName = masterDataExpressions
                .getInstanceName(MASTER_DATA_PRODUCT_TYPE, (String) payload.get(PRODUCT), languageCode);

        payload.put(MASTER_DATA_PRODUCT_NAME, masterDataProductName);

        TemplateProcessingResult result = templateEngine
                .getTemplateService().processTemplate(EMAIL_BODY_TEMPLATE_KEY, variant, payload);
        Map<String, Object> response = Map.of("emailBody", result.getProcessedContent());

        ObjectNode responsePayload = objectMapper.createObjectNode();
        responsePayload.set("emailBody", objectMapper.valueToTree(response));

        return new BaseBotActionResult(responsePayload, Intent.NOOP);
    }

}
