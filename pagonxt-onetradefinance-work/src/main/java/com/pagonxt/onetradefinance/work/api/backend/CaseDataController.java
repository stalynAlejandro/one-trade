package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest;
import com.pagonxt.onetradefinance.integrations.model.Comment;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.work.service.CaseDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for case data
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.CaseDataService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/case-data", produces = "application/json")
public class CaseDataController {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(CaseDataController.class);

    //class attribute
    private final CaseDataService caseDataService;

    /**
     * constructor method
     * @param caseDataService a CaseDataService object. Service that provides necessary functionality to this controller
     */
    public CaseDataController(CaseDataService caseDataService) {
        this.caseDataService = caseDataService;
    }

    /**
     * method to obtain comments from a case
     * @param requestId : a string with the request id
     * @param request   : an AuthenticatedRequest object
     * @see com.pagonxt.onetradefinance.integrations.model.AuthenticatedRequest
     * @return a ControllerResponse object
     */
    @PostMapping("/get/{requestId}/comments")
    public ControllerResponse findComments(@PathVariable String requestId, @RequestBody AuthenticatedRequest request) {
        List<Comment> foundComments = caseDataService.findComments(request, requestId);
        LOG.debug("findComments(requestId: {}) returned: {}", requestId, foundComments);
        return ControllerResponse.success("findComments", foundComments);
    }

}
