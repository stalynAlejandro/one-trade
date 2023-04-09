package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.CommentList;
import com.pagonxt.onetradefinance.external.backend.api.serializer.CommentDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.CaseDataService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.integrations.model.Comment;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.pagonxt.onetradefinance.integrations.util.LogUtils.sanitizeLog;

/**
 * Controller with the endpoints to get a list of comments by case
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.CaseDataService
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.CommentDtoSerializer
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/case-data")
public class CaseDataController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(CaseDataController.class);

    /**
     * Class attributes
     */
    private final UserInfoService userInfoService;
    private final CaseDataService caseDataService;
    private final CommentDtoSerializer serializer;

    /**
     * Case Data Controller Constructor
     * @param userInfoService Service that provides necessary functionality with userInfo
     * @param caseDataService Service that provides necessary functionality to this controller
     * @param serializer Component for the conversion and adaptation of the data structure
     */
    public CaseDataController(UserInfoService userInfoService,
                              CaseDataService caseDataService,
                              CommentDtoSerializer serializer) {
        this.userInfoService = userInfoService;
        this.caseDataService = caseDataService;
        this.serializer = serializer;
    }

    /**
     * This method returns a list of comments by case
     * @param requestId a string the request id (case)
     * @param locale a string
     * @return a list with comments
     */
    @GetMapping("/{request_id}/comments")
    @Secured("ROLE_USER")
    public ControllerResponse getComments(@PathVariable(name = "request_id") String requestId, @RequestParam String locale) {
        List<Comment> comments = caseDataService.getComments(requestId, userInfoService.getUserInfo(locale));
        if (LOG.isDebugEnabled()) {
            LOG.debug("getComments(requestId: {}, locale: {}): Get comments : {}",
                    sanitizeLog(requestId), sanitizeLog(locale), sanitizeLog(comments));
        }
        CommentList responseBody = new CommentList
                (comments.stream().map(serializer::toDto).collect(Collectors.toList()));
        return ControllerResponse.success("getComments", responseBody);
    }
}
