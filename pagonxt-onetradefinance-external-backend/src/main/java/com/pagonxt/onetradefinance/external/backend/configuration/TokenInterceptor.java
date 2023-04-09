package com.pagonxt.onetradefinance.external.backend.configuration;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.pagonxt.onetradefinance.external.backend.service.OfficeInfoService;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import com.pagonxt.onetradefinance.integrations.util.Strings;
import com.pagonxt.onetradefinance.integrations.validation.JWTValidation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.pagonxt.onetradefinance.integrations.constants.UserConstants.*;

/**
 * Class to process the jwt token sent by apigee.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
public class TokenInterceptor implements HandlerInterceptor {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(TokenInterceptor.class);

    /**
     * The JWT validator
     */
    private final JWTValidation jwtValidation;

    /**
     * The office info service
     */
    private final OfficeInfoService officeInfoService;

    /**
     * Flag to mock user as backOffice
     */
    private final boolean mockBO;

    /**
     * Constructor.
     *
     * @param jwtValidation     : the JWT validator
     * @param officeInfoService : the office info service
     */
    public TokenInterceptor(JWTValidation jwtValidation, OfficeInfoService officeInfoService, boolean mockBO) {
        this.jwtValidation = jwtValidation;
        this.officeInfoService = officeInfoService;
        this.mockBO = mockBO;
    }

    /**
     * Method that intercepts the request, validates the JWT token and saves the information in the user session
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     *
     * @return true if there are no errors
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        LOG.info("Authorization apigee: {}", authorization);
        DecodedJWT decodedJWT = jwtValidation.verify(authorization);
        UserInfo userInfo = generateUserInfo(decodedJWT);
        String country = decodedJWT.getClaim("country").asString();
        if (isBackofficeUser()) {
            userInfo.getUser().setUserType(USER_TYPE_BACKOFFICE);
            injectCountryInfo(userInfo, country);
        } else {
            injectCountryInfo(userInfo, country);
            injectUserTypeAndOfficeInfo(userInfo, country);
        }
        LOG.info("UserInfo: {}", userInfo);
        request.getSession().setAttribute("userInfo", userInfo);
        return true;
    }

    /**
     * Method to create the userInfo object with user id and name data.
     *
     * @param decodedJWT : the decoded JWT
     *
     * @return the user Info object
     */
    private UserInfo generateUserInfo(DecodedJWT decodedJWT) {
        UserInfo userInfo = new UserInfo();
        User user = new User();
        userInfo.setUser(user);
        String userName = decodedJWT.getClaim("sub").asString();
        if (Strings.isBlank(userName)) {
            LOG.error("The jwt token doesn't have correct username information");
            throwAuthorizationException();
        }
        user.setUserId(userName);
        user.setUserDisplayedName(userName);
        return userInfo;
    }

    /**
     * Method to add the country info to the userInfo object.
     *
     * @param userInfo  : the user info
     * @param country   : the country info
     */
    private void injectCountryInfo(UserInfo userInfo, String country) {
        if (country.length() < 2) {
            LOG.error("The jwt token doesn't have correct country information");
            throwAuthorizationException();
        }
        userInfo.setCountry(country.substring(0, 2));
    }

    /**
     * Method to add the office info to the userInfo object.
     *
     * @param userInfo  : the user info
     * @param country   : the country info
     */
    private void injectUserTypeAndOfficeInfo(UserInfo userInfo, String country) {
        String code = country.substring(2);
        if (officeInfoService.isValidOffice(code)) {
            userInfo.setOffice(code);
            userInfo.setMiddleOffice(officeInfoService.getMiddleOffice(code));
            userInfo.getUser().setUserType(USER_TYPE_OFFICE);
        } else if (officeInfoService.isValidMiddleOffice(code)) {
            userInfo.setMiddleOffice(code);
            userInfo.getUser().setUserType(USER_TYPE_MIDDLE_OFFICE);
        } else {
            LOG.error("The jwt token doesn't have correct office code information. Code: [{}]", code);
            throwAuthorizationException();
        }
    }

    /**
     * Method to determine if the user is type BackOffice
     *
     * @return true if the user is type BackOffice, false otherwise
     */
    private boolean isBackofficeUser() {
        return mockBO;
    }

    /**
     * Method to throw a Service exception.
     */
    private void throwAuthorizationException() {
        throw new ServiceException("The authorization header is not correct", "errorToken");
    }
}
