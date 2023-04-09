package com.pagonxt.onetradefinance.work.service;

import com.flowable.template.api.processor.TemplateProcessingResult;
import com.flowable.template.engine.TemplateEngine;
import com.pagonxt.onetradefinance.work.service.exception.TemplateVariationNotFoundException;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.pagonxt.onetradefinance.integrations.util.LogUtils.sanitizeLog;

/**
 * service class for templates
 * @author -
 * @version jdk-11.0.13
 * @see  com.flowable.template.engine.TemplateEngine
 * @since jdk-11.0.13
 */
@Service
public class TemplateVariationService {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(TemplateVariationService.class);

    private static final String RETURN_REASON_COMMENT_TEMPLATE_KEY = "PGN_TPL001";

    //class attribute
    private final TemplateEngine templateEngine;

    /**
     * constructor method
     * @param templateEngine : a TemplateEngine object
     */
    public TemplateVariationService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Method to get the template with the comments about return reason
     * @param returnReasonKey : a string object with the key of the return reason
     * @param languageCode    : a string with the language code
     * @return  a string object with the template
     */
    public String getReturnReasonCommentTemplateVariation(String returnReasonKey, String languageCode) {
        Map<String, Object> variant = Map.of("returnReasonKey", returnReasonKey, "languageCode", languageCode);
        try {
            TemplateProcessingResult result = templateEngine.getTemplateService().processTemplate(RETURN_REASON_COMMENT_TEMPLATE_KEY, variant, null);
            if(LOG.isDebugEnabled()) {
                LOG.debug("getReturnReasonCommentTemplate(returnReasonKey: {}, languageCode: {}) Returning processed content for template {}",
                        sanitizeLog(returnReasonKey), sanitizeLog(languageCode), RETURN_REASON_COMMENT_TEMPLATE_KEY);
            }
            return result.getProcessedContent();
        } catch(FlowableIllegalArgumentException e) {
            LOG.error("Error processing template", e);
            throw new TemplateVariationNotFoundException(RETURN_REASON_COMMENT_TEMPLATE_KEY, variant);
        }
    }
}
