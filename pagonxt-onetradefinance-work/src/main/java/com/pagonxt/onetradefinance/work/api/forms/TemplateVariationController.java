package com.pagonxt.onetradefinance.work.api.forms;

import com.pagonxt.onetradefinance.work.api.model.ContentStringResponse;
import com.pagonxt.onetradefinance.work.service.TemplateVariationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for templates
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.TemplateVariationService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("/forms/template")
public class TemplateVariationController {

    //class attribute
    private final TemplateVariationService templateVariationService;

    /**
     * constructor method
     * @param templateVariationService Service that provides necessary functionality to this controller
     */
    public TemplateVariationController(TemplateVariationService templateVariationService) {
        this.templateVariationService = templateVariationService;
    }

    /**
     * This method gets a template with a return reason comment
     * @param returnReasonKey : a string with the key of return reason
     * @param languageCode    : a string with the language code
     * @return a ResponseEntity object
     */
    @GetMapping("/PGN_TPL001")
    public ResponseEntity<ContentStringResponse> getReturnReasonCommentTemplate(@RequestParam String returnReasonKey,
                                                                                @RequestParam String languageCode) {
        return ResponseEntity.ok(new ContentStringResponse(templateVariationService
                .getReturnReasonCommentTemplateVariation(returnReasonKey, languageCode)));
    }
}
