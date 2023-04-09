package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.FiltersDefinition;
import com.pagonxt.onetradefinance.external.backend.api.model.ListRequest;
import com.pagonxt.onetradefinance.external.backend.service.MyTasksService;
import com.pagonxt.onetradefinance.external.backend.service.UserService;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller with the endpoints to get a list of tasks. We can see this list in external app(pending tasks)
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.UserService
 * @see com.pagonxt.onetradefinance.external.backend.service.MyTasksService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/retrieve-my-tasks")
public class MyTasksController {

    /**
     * Class attributes
     */
    private final UserService userService;

    private final MyTasksService myTasksService;

    /**
     * My Tasks Controller Constructor
     * @param userService Service that provides necessary functionality with user
     * @param myTasksService Service that provides necessary functionality to this controller
     */
    public MyTasksController(UserService userService, MyTasksService myTasksService) {
        this.userService = userService;
        this.myTasksService = myTasksService;
    }

    /**
     * This method returns a list with the requests of the user who is logged in to the external application
     * @param request a ListRequest Object
     * @param scope a string to filter the search of tasks (for example "mine")
     * @return a list with the tasks
     */
    @PostMapping
    @Secured("ROLE_USER")
    public MyTasksList getMyTasks(@RequestBody ListRequest request, @RequestParam(required = false) String scope) {
        return myTasksService.getMyTasks(request, scope, userService.getCurrentUser());
    }

    /**
     * This method get the selected filters by the user in the external application
     * @return a list of filters to apply in the list of tasks
     */
    @GetMapping("/filters")
    @Secured("ROLE_USER")
    public FiltersDefinition getFilters() {
        return myTasksService.getFilters(userService.getCurrentUser().getUserType());
    }
}
