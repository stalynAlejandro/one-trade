package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.ListRequest;
import com.pagonxt.onetradefinance.external.backend.service.HistoricTasksService;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller with the endpoints to get a tasks historic
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @see com.pagonxt.onetradefinance.external.backend.service.HistoricTasksService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("${controller.path}/retrieve-historic-tasks")
public class HistoricTasksController {

    /**
     * Class attributes
     */
    private final UserInfoService userInfoService;

    private final HistoricTasksService historicTasksService;

    /**
     * Historic Tasks Controller Constructor
     * @param userInfoService Service that provides necessary functionality with userInfo
     * @param historicTasksService Service that provides necessary functionality to this controller
     */
    public HistoricTasksController(UserInfoService userInfoService, HistoricTasksService historicTasksService) {
        this.userInfoService = userInfoService;
        this.historicTasksService = historicTasksService;
    }

    /**
     * This method get a tasks historic
     * @param request List Request object
     * @param code a string with code
     * @param locale a string with locale
     * @return a tasks historic
     */
    @PostMapping
    @Secured("ROLE_USER")
    public HistoricTasksList getHistoricTasks(
            @RequestBody ListRequest request,
            @RequestParam(name = "case_id") String code, @RequestParam(required = false) String locale) {
        return historicTasksService.getHistoricTasks(request, code, locale, userInfoService.getUserInfo());
    }
}
