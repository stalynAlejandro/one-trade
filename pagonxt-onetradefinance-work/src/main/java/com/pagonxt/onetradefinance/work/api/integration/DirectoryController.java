package com.pagonxt.onetradefinance.work.api.integration;

import com.pagonxt.onetradefinance.integrations.service.DirectoryService;
import com.pagonxt.onetradefinance.work.api.model.RecipientsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for directory
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.integrations.service.DirectoryService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping("/integrations/directory")
public class DirectoryController {

    //class attribute
    private final DirectoryService directoryService;

    /**
     * constructor method
     * @param directoryService Service that provides necessary functionality to this controller
     */
    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    /**
     * This method allows getting recipients for notifications
     * @return a ResponseEntity object
     */
    @GetMapping(value = "/recipients")
    public ResponseEntity<RecipientsResponse> getRecipientsForNotification() {
        String recipients = directoryService.getRecipientsForNotification("initiatorId", "foo");

        return ResponseEntity.ok(new RecipientsResponse(recipients));
    }

}
