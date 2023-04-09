package com.pagonxt.onetradefinance.work.service;

import com.flowable.template.api.processor.TemplateProcessingResult;
import com.flowable.template.engine.TemplateEngine;
import com.flowable.template.engine.impl.processor.DefaultTemplateProcessingResult;
import com.pagonxt.onetradefinance.work.config.UnitTest;
import com.pagonxt.onetradefinance.work.service.exception.TemplateVariationNotFoundException;
import org.flowable.common.engine.api.FlowableIllegalArgumentException;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@UnitTest
class TemplateVariationServiceTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    TemplateEngine templateEngine;

    @InjectMocks
    private TemplateVariationService templateVariationService;

    @Test
    void testSolveReturnReasonCommentTemplate_ok() {
        // Given
        TemplateProcessingResult templateResult = new DefaultTemplateProcessingResult(null, "result");
        when(templateEngine.getTemplateService().processTemplate(any(), any(), any())).thenReturn(templateResult);

        // When
        String result = templateVariationService.getReturnReasonCommentTemplateVariation("restrictedBank", "es-ES");

        // Then
        assertThat(result).isEqualTo("result");
    }

    @Test
    void testSolveReturnReasonCommentTemplate_variationNotFound() {
        // Given
        Map<String, Object> variation = Map.of("returnReasonKey", "foo", "languageCode", "de-CH");
        when(templateEngine.getTemplateService().processTemplate("PGN_TPL001", variation, null))
                .thenThrow(FlowableIllegalArgumentException.class);

        // When and then
        Exception exception = assertThrows(TemplateVariationNotFoundException.class,
                () -> templateVariationService.getReturnReasonCommentTemplateVariation("foo", "de-CH"));

        // Then
        assertThat(exception.getMessage()).contains("Variation not found for template").contains("PGN_TPL001");
    }
}
