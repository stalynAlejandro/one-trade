package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;

public interface IRequestController {
    ControllerResponse confirm(CommonRequestDto requestDto);
    ControllerResponse completeCompleteInfo(String taskId, CommonRequestDto requestDto);
    ControllerResponse cancelCompleteInfo(String taskId);
    ControllerResponse getDraft(String caseId);
    ControllerResponse getCompleteInfo(String taskId, boolean forOperation);
    ControllerResponse getCaseDetails(String caseId);
}
