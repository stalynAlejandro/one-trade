package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery;
import com.pagonxt.onetradefinance.work.service.HistoricTasksService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for historic tasks
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.work.service.HistoricTasksService
 * @since jdk-11.0.13
 */

@RestController
@RequestMapping(value = "/backend/historic-tasks", produces = "application/json")
public class HistoricTasksController {

    //class attribute
    private final HistoricTasksService historicTasksService;

    /**
     * constructor method
     * @param historicTasksService Service that provides necessary functionality to this controller
     */
    public HistoricTasksController(HistoricTasksService historicTasksService) {
        this.historicTasksService = historicTasksService;
    }

    /**
     * method to obtain a list of historic tasks
     * @param query : a HistoricTasksQuery object
     * @see com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery
     * @see com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList
     * @return a HistoricTasksList object with a list of historic tasks
     */
    @PostMapping
    public HistoricTasksList getHistoricTasks(@RequestBody HistoricTasksQuery query) {
        return historicTasksService.getHistoricTasks(query);
    }
}
