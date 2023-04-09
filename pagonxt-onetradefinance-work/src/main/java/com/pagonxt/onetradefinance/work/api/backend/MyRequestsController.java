package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery;
import com.pagonxt.onetradefinance.work.service.MyRequestsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for requests
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.MyRequestsService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/my-requests", produces = "application/json")
public class MyRequestsController {

    //class attribute
    private final MyRequestsService myRequestsService;

    /**
     * constructor method
     * @param myRequestsService Service that provides necessary functionality to this controller
     */
    public MyRequestsController(MyRequestsService myRequestsService) {
        this.myRequestsService = myRequestsService;
    }

    /**
     * method to obtain a list of requests
     * @param query : a MyRequestsQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsQuery
     * @see com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList
     * @return a MyRequestList object with a list requests
     */
    @PostMapping
    public MyRequestsList getMyRequests(@RequestBody MyRequestsQuery query) {
        return myRequestsService.getMyRequests(query);
    }
}
