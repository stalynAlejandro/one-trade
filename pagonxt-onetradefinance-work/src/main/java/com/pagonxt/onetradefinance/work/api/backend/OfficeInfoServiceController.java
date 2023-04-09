package com.pagonxt.onetradefinance.work.api.backend;

import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import com.pagonxt.onetradefinance.integrations.model.OfficeInfo;
import com.pagonxt.onetradefinance.work.service.OfficeInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for differents operations with offices
 * @author -
 * @version jdk-11.0.13
 * @see OfficeInfoService
 * @since jdk-11.0.13
 */
@RestController
@RequestMapping(value = "backend/office", produces = "application/json")
public class OfficeInfoServiceController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(OfficeInfoServiceController.class);

    //class attribute
    private final OfficeInfoService officeInfoService;

    /**
     * Constructor method
     * @param officeInfoService : OfficeInfoService object
     */
    public OfficeInfoServiceController(OfficeInfoService officeInfoService) {
        this.officeInfoService = officeInfoService;
    }

    /**
     * Method to check if an office code belongs to a valid office
     * @param officeId  : a string with the office id
     * @return a Controller Response object
     */
    @PostMapping("/getValidOffice/{officeId}")
    @Secured("ROLE_USER")
    public ControllerResponse isValidOffice(@PathVariable String officeId){
        boolean officeExists = officeInfoService.isValidOffice(officeId);
        LOG.debug("officeExists(officeId: {}) returned: {}", officeId, officeExists);
        return ControllerResponse.success("officeExists", officeExists);
    }

    /**
     * Method to check if an office code belongs to a valid middle office
     * @param officeId  : a string with the office id
     * @return a Controller Response object
     */
    @PostMapping("/getValidMiddleOffice/{officeId}")
    @Secured("ROLE_USER")
    public ControllerResponse isValidMiddleOffice(@PathVariable String officeId){
        boolean middleOfficeExists = officeInfoService.isValidMiddleOffice(officeId);
        LOG.debug("MiddleOfficeExists(officeId: {}) returned: {}", officeId, middleOfficeExists);
        return ControllerResponse.success("MiddleOfficeExists", middleOfficeExists);
    }

    /**
     * Method to obtain the middle office code
     * @param officeId  : a string with the office id
     * @return a Controller Response object
     */
    @PostMapping("/getMiddleOffice/{officeId}")
    @Secured("ROLE_USER")
    public ControllerResponse getMiddleOffice(@PathVariable String officeId){
        String middleOffice = officeInfoService.getMiddleOffice(officeId);
        LOG.debug("officeHasMiddleOffice(officeId: {}) returned: {}", officeId, middleOffice);
        return ControllerResponse.success("middleOffice", middleOffice);
    }

    /**
     * Method to build an OfficeInfo object with the office info
     * @param officeId  : a string with the office id
     * @return a Controller Response object
     */
    @PostMapping("/getOfficeInfo/{officeId}")
    @Secured("ROLE_USER")
    public ControllerResponse getOfficeInfo(@PathVariable String officeId){
        OfficeInfo officeInfo = officeInfoService.getOfficeInfo(officeId);
        return ControllerResponse.success("officeInfo", officeInfo);
    }

    /**
     * Method to search the offices of a middle office
     * @param officeId  : a string with the office id
     * @return a Controller Response object
     */
    @PostMapping("/getOffices/{officeId}")
    @Secured("ROLE_USER")
    public ControllerResponse getOffices(@PathVariable String officeId){
        List<String> offices;
        offices = officeInfoService.getOffices(officeId);
        LOG.debug("MiddleOfficeHasOffices(officeId: {}) returned: {}", officeId, offices);
        return ControllerResponse.success("offices", offices);
    }
}
