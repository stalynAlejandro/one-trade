package com.pagonxt.onetradefinance.external.backend.service;

import com.pagonxt.onetradefinance.external.backend.api.model.ListRequest;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList;
import com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery;
import org.springframework.stereotype.Service;

/**
 * This Service class provides a way of a client to interact with some functionality in the application.
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.service.RestTemplateWorkService
 * @since jdk-11.0.13
 */
@Service
public class HistoricTasksService {

    /**
     * Class attributes
     */
    private final RestTemplateWorkService restTemplateWorkService;

    /**
     * Class constructor
     * @param restTemplateWorkService RestTemplateWorkService object
     */
    public HistoricTasksService(RestTemplateWorkService restTemplateWorkService) {
        this.restTemplateWorkService = restTemplateWorkService;
    }

    /**
     * method to return a list of historic tasks
     * @param request a list of request
     * @param code the code
     * @param locale the locale
     * @param userInfo UserInfo object
     * @see com.pagonxt.onetradefinance.external.backend.api.model.ListRequest
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksList
     * @return a list of historic tasks
     */
    public HistoricTasksList getHistoricTasks(ListRequest request, String code, String locale, UserInfo userInfo) {
        return restTemplateWorkService.postHistoricTasks(parseHistoricTaskQuery(request, code, locale, userInfo));
    }

    /**
     * method to parse the data of the historic tasks
     * @param request a list of request
     * @param code the code
     * @param locale the locale
     * @param userInfo UserInfo object
     * @see com.pagonxt.onetradefinance.external.backend.api.model.ListRequest
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @see com.pagonxt.onetradefinance.integrations.model.historictask.HistoricTasksQuery
     * @return HistoryTasksQuery object
     */
    private HistoricTasksQuery parseHistoricTaskQuery(ListRequest request,
                                                      String code,
                                                      String locale,
                                                      UserInfo userInfo) {
        HistoricTasksQuery result = new HistoricTasksQuery();
        result.setUserInfo(userInfo);
        result.setCode(code);
        result.setLocale(locale);
        result.setSortField(request.getSortField());
        result.setSortOrder(request.getSortOrder());
        result.setFromPage(request.getFromPage());
        result.setPageSize(request.getPageSize());
        return result;
    }

}
