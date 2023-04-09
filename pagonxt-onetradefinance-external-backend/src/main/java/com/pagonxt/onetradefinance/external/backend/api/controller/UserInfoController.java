package com.pagonxt.onetradefinance.external.backend.api.controller;

import com.pagonxt.onetradefinance.external.backend.api.model.UserInfoDto;
import com.pagonxt.onetradefinance.external.backend.api.serializer.UserInfoDtoSerializer;
import com.pagonxt.onetradefinance.external.backend.service.UserInfoService;
import com.pagonxt.onetradefinance.integrations.model.ControllerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller with the endpoints to get user info
 * @author -
 * @version jdk-11.0.13
 * @see com.pagonxt.onetradefinance.external.backend.api.serializer.UserInfoDtoSerializer
 * @see com.pagonxt.onetradefinance.external.backend.service.UserInfoService
 * @since jdk-11.0.13
 */
@RestController
@Validated
@RequestMapping("${controller.path}/user-info")
public class UserInfoController {

    /**
     * Logger object for class logs
     */
    private static final Logger LOG = LoggerFactory.getLogger(UserInfoController.class);

    /**
     * Class attributes
     */
    private final UserInfoService userInfoService;
    private final UserInfoDtoSerializer userInfoDtoSerializer;

    /**
     * User Info Controller Constructor
     * @param userInfoService Service that provides necessary functionality to this controller
     * @param userInfoDtoSerializer Component for the conversion and adaptation of the data structure
     */
    public UserInfoController(UserInfoService userInfoService, UserInfoDtoSerializer userInfoDtoSerializer) {
        this.userInfoService = userInfoService;
        this.userInfoDtoSerializer = userInfoDtoSerializer;
    }

    /**
     * This method returns a user info
     * @return a controller response with user info
     */
    @GetMapping
    @Secured("ROLE_USER")
    public ControllerResponse getUserInfo() {
        UserInfoDto userInfoDto = userInfoDtoSerializer.toDto(userInfoService.getUserInfo());
        LOG.debug("getUserInfo() returned: {}", userInfoDto);
        return ControllerResponse.success("userInfoFound", userInfoDto);
    }
}
