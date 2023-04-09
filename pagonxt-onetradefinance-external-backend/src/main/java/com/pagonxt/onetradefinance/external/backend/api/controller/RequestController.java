package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.CommonRequestDto;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.PagoNxtRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.InvalidRequestException;
import com.pagonxt.onetradefinance.integrations.model.trade.TradeRequest;

/**
 * Class to create an object with the requester info (office, emails, type...) and inject it in a request
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @see OfficeInfoService
 * @since jdk-11.0.13
 */
public class RequestController implements IRequestController {

    public static final String PARAM_TASK_ID = "task_id";
    public static final String PARAM_CASE_ID = "case_id";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_FOR_OPERATION = "for_operation";
    public static final String NO_IMPLEMENTATION_MESSAGE = "There is no implementation for this operation";

    /**
     * Class attributes
     */
    private final OfficeInfoService officeInfoService;
    private final UserInfoService userInfoService;

    /**
     * Request Controller constructor
     * @param officeInfoService Service that provides necessary functionality with office
     * @param userInfoService Service that provides necessary functionality with userInfo
     */
    public RequestController(OfficeInfoService officeInfoService, UserInfoService userInfoService) {
        this.officeInfoService = officeInfoService;
        this.userInfoService = userInfoService;
    }

    public UserInfo getUserInfo() {
        return userInfoService.getUserInfo();
    }

    public void injectRequestInfo(PagoNxtRequest request) {
        request.setRequester(userInfoService.getUserInfo());
        request.setCountry(request.getRequester().getCountry());
        request.setMiddleOffice(officeInfoService.getMiddleOffice(request.getOffice()));
    }

    public void injectTradeRequestInfo(TradeRequest request) {
        request.setRequester(userInfoService.getUserInfo());
        request.setCountry(request.getRequester().getCountry());
        request.setMiddleOffice(officeInfoService.getMiddleOffice(request.getOffice()));
    }

    /**
     * Method to select the put action according to the type.
     *
     * @param controller    : the interface
     * @param requestDto    : the payload
     * @param type          : the type
     * @param taskId        : the task id
     *
     * @return the response
     */
    public ControllerResponse putSwitch(IRequestController controller,
                                 CommonRequestDto requestDto, String type, String taskId) {
        validateParamNotNull(type, PARAM_TYPE);
        switch (type) {
            case "confirm":
                validateRequestBodyNotNull(requestDto);
                return controller.confirm(requestDto);
            case "complete-info-complete":
                validateRequestBodyNotNull(requestDto);
                validateParamNotNull(taskId, PARAM_TASK_ID);
                return controller.completeCompleteInfo(taskId, requestDto);
            case "complete-info-cancel":
                validateParamNotNull(taskId, PARAM_TASK_ID);
                return controller.cancelCompleteInfo(taskId);
            default:
                throw new InvalidRequestException("Param type is incorrect", "paramError");
        }
    }

    /**
     * Method to select the get action according to the type.
     *
     * @param controller    : the interface
     * @param type          : the type
     * @param caseId        : the case id
     * @param taskId        : the task id
     * @param forOperation  : the flag forOperation
     *
     * @return the response
     */
    public ControllerResponse getSwitch(IRequestController controller,
                                 String type, String caseId, String taskId, boolean forOperation) {
        validateParamNotNull(type, PARAM_TYPE);
        switch (type) {
            case "draft":
                validateParamNotNull(caseId, PARAM_CASE_ID);
                return controller.getDraft(caseId);
            case "complete-info":
                validateParamNotNull(taskId, PARAM_TASK_ID);
                return controller.getCompleteInfo(taskId, forOperation);
            case "details":
                validateParamNotNull(caseId, PARAM_CASE_ID);
                return controller.getCaseDetails(caseId);
            default:
                throw new InvalidRequestException("Param type is incorrect");
        }
    }

    /**
     * Method to validate that a param is not null
     *
     * @param param     : the paraam value
     * @param paramName : the param name
     */
    public void validateParamNotNull(String param, String paramName) {
        if (param == null) {
            throw new InvalidRequestException(paramName);
        }
    }

    /**
     * Method to validate that the request body is not null
     *
     * @param requestBody   : the request body
     */
    public void validateRequestBodyNotNull(CommonRequestDto requestBody) {
        if (requestBody == null) {
            throw new InvalidRequestException("The body of the request cannot be null", "payloadError");
        }
    }

    /**
     * Confirm a request.
     *
     * @param requestDto : the request body
     * @return the response
     */
    @Override
    public ControllerResponse confirm(CommonRequestDto requestDto) {
        throw new UnsupportedOperationException(NO_IMPLEMENTATION_MESSAGE);
    }

    /**
     * Complete a complete info task.
     *
     * @param taskId        : the task id
     * @param requestDto    : the request body
     * @return the response
     */
    @Override
    public ControllerResponse completeCompleteInfo(String taskId, CommonRequestDto requestDto) {
        throw new UnsupportedOperationException(NO_IMPLEMENTATION_MESSAGE);
    }

    /**
     * Cancel a complete info task.
     *
     * @param taskId    : the task id
     * @return the response
     */
    @Override
    public ControllerResponse cancelCompleteInfo(String taskId) {
        throw new UnsupportedOperationException(NO_IMPLEMENTATION_MESSAGE);
    }

    /**
     * Get the draft.
     *
     * @param caseId    : the case id
     * @return the response
     */
    @Override
    public ControllerResponse getDraft(String caseId) {
        throw new UnsupportedOperationException(NO_IMPLEMENTATION_MESSAGE);
    }

    /**
     * Get the complete info task data
     *
     * @param taskId        : the task id
     * @param forOperation  : the flag forOperation
     * @return the response
     */
    @Override
    public ControllerResponse getCompleteInfo(String taskId, boolean forOperation) {
        throw new UnsupportedOperationException(NO_IMPLEMENTATION_MESSAGE);
    }

    /**
     * Get the case detail data
     *
     * @param caseId    : the case id
     * @return the response
     */
    @Override
    public ControllerResponse getCaseDetails(String caseId) {
        throw new UnsupportedOperationException(NO_IMPLEMENTATION_MESSAGE);
    }
}
