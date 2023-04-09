package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pagonxt.onetradefinance.external.backend.api.model.FiltersDefinition;
import com.pagonxt.onetradefinance.external.backend.api.model.ListRequest;
import com.pagonxt.onetradefinance.external.backend.service.MyRequestsService;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.model.requests.MyRequestsList;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;

/**
 * Controller with the endpoints to get a list of requests.
 * We can see this list in external app(consultation of requests)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.UserService
 * @see com.pagonxt.onetradefinance.external.backend.service.MyRequestsService
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see com.pagonxt.onetradefinance.external.backend.api.model.FiltersDefinition
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/retrieve-my-requests")
public class MyRequestsController {

    /**
     * Class attributes
     */
    private final ObjectMapper objectMapper;

    private final UserService userService;

    private final MyRequestsService myRequestsService;

    private FiltersDefinition filtersDefinition;

    /**
     * My Requests Controller Constructor
     * @param myRequestsService Service that provides necessary functionality to this controller
     * @param userService Service that provides necessary functionality with user
     * @param objectMapper We can use this object(class) to parse or deserialize JSON content into a Java object
     */
    public MyRequestsController(MyRequestsService myRequestsService,
                                UserService userService,
                                ObjectMapper objectMapper) {
        this.myRequestsService = myRequestsService;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    /**
     * This method returns a list with the requests of the user who is logged in to the external application
     * @param request a ListRequest Object
     * @return a list with the requests
     */
    @PostMapping
    @Secured("ROLE_USER")
    public MyRequestsList getMyRequests(@RequestBody ListRequest request) {
        return myRequestsService.getMyRequests(request, userService.getCurrentUser());
    }

    /**
     * This method get the selected filters by the user in the external application
     * @return a list of filters to apply in the list of requests
     */
    @GetMapping("/filters")
    @Secured("ROLE_USER")
    public FiltersDefinition getFilters() {
        if (filtersDefinition == null) {
            try (InputStream resourceStream = this.getClass().getClassLoader()
                    .getResourceAsStream("filters/my-requests.json")) {
                filtersDefinition = objectMapper.readValue(resourceStream, new TypeReference<>() {});
            } catch (Exception e) {
                throw new ServiceException("Error getting my requests filters", "getFiltersError");
            }
        }
        return filtersDefinition;
    }
}
