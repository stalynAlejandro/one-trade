package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList;
import com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery;
import com.pagonxt.onetradefinance.work.service.MyTasksService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for tasks
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.MyTasksService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "/backend/my-tasks", produces = "application/json")
public class MyTasksController {

    //class attribute
    private final MyTasksService myTasksService;

    /**
     * constructor method
     * @param myTasksService Service that provides necessary functionality to this controller
     */
    public MyTasksController(MyTasksService myTasksService) {
        this.myTasksService = myTasksService;
    }

    /**
     * method to obtain a list of tasks
     * @param query : a MyTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksQuery
     * @see com.pagonxt.onetradefinance.integrations.model.tasks.MyTasksList
     * @return a MyTaskList object with a list tasks
     */
    @PostMapping
    public MyTasksList getMyTasks(@RequestBody MyTasksQuery query) {
        return myTasksService.getMyTasks(query);
    }
}
