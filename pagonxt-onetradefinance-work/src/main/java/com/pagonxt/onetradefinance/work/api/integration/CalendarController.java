package com.pagonxt.onetradefinance.work.api.integration;

import com.pagonxt.onetradefinance.integrations.service.CalendarService;
import com.pagonxt.onetradefinance.work.api.model.NextWorkDayResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * Controller class for calendar
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.service.CalendarService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("/integrations/directory")
public class CalendarController {

    //class attribute
    private final CalendarService calendarService;

    /**
     * constructor method
     * @param calendarService Service that provides necessary functionality to this controller
     */
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    /**
     * This method allows to get the next workday
     * @return a ResponseEntity object
     */
    @GetMapping("/calendar/next")
    public ResponseEntity<NextWorkDayResponse> getNextWorkDay() {

        LocalDate nextWorkDay = calendarService.nextWorkDay();

        NextWorkDayResponse nextWorkDayResponse = new NextWorkDayResponse(nextWorkDay);

        return ResponseEntity.ok(nextWorkDayResponse);
    }
}
