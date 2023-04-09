package com.pagonxt.onetradefinance.work.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flowable.action.api.bot.BaseBotActionResult;
import com.flowable.action.api.bot.BotActionResult;
import com.flowable.action.api.bot.BotService;
import com.flowable.action.api.history.HistoricActionInstance;
import com.flowable.action.api.intents.Intent;
import com.flowable.action.api.repository.ActionDefinition;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class to set the visibility of users on open requests
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
public class OpenRequestsUsersVisibilityBot implements BotService {
    private static final String BOT_KEY = "open-requests-users-visibility-bot";
    private static final String BOT_NAME = "Open Requests Users Visibility Bot";
    private static final String BOT_DESCRIPTION = "Assign the task";

    private static final String UNDERSCORE = "_";

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
        List<String> resolutionGroups = (List<String>) payload.get("userResolutionGroups");
        Map<String, Object> productsAndEvents = getProductAndEvents(resolutionGroups);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responsePayload = objectMapper.createObjectNode();
        responsePayload.set("productsAndEvents", objectMapper.valueToTree(productsAndEvents));

        return new BaseBotActionResult(responsePayload, Intent.NOOP);
    }

    /**
     * Method to get product and events
     * @param resolutionGroups : a list of resolution groups
     * @return a collection of products and events
     */
    private Map<String, Object> getProductAndEvents(List<String> resolutionGroups) {
        Map<String, Object> productsAndEvents = new HashMap<>();
        List<String> products = new ArrayList<>();
        List<String> events = new ArrayList<>();
        for (String resolutionGroup : resolutionGroups) {
            String product = getProductAndEventSubstring(resolutionGroup);
            products.add(product);

            String resolutionGroupNoProduct = resolutionGroup.replace(product.concat(UNDERSCORE), "");
            events.add(getProductAndEventSubstring(resolutionGroupNoProduct));
        }

        productsAndEvents.put("products", deleteIdenticalProductsOrEvents(products));
        productsAndEvents.put("events", deleteIdenticalProductsOrEvents(events));

        return productsAndEvents;
    }

    /**
     * Method to get a substring
     * @param resolutionGroup : a string with the resolution group
     * @return : a substring
     */
    private String getProductAndEventSubstring(String resolutionGroup) {

        return resolutionGroup.substring(0, resolutionGroup.indexOf(UNDERSCORE));
    }

    /**
     * Method to delete identical product or event
     * @param productOrEvent : a list with products and events
     * @return : a collection with products and events
     */
    private Object deleteIdenticalProductsOrEvents(List<String> productOrEvent) {

        return productOrEvent.stream().distinct().collect(Collectors.toList());
    }
}
