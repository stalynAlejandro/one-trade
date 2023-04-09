package com.pagonxt.onetradefinance.external.backend.api.serializer;

import com.pagonxt.onetradefinance.external.backend.api.model.UserInfoDto;
import com.pagonxt.onetradefinance.integrations.model.User;
import com.pagonxt.onetradefinance.integrations.model.UserInfo;
import org.springframework.stereotype.Component;

/**
 * This class has methods to convert a DTO class into an entity class and viceversa
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
public class UserInfoDtoSerializer {

    /**
     * This method converts data from an entity object to a DTO object
     * @param userInfo an entity object
     * @see com.pagonxt.onetradefinance.external.backend.api.model.UserInfoDto
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return a DTO object
     */
    public UserInfoDto toDto(UserInfo userInfo) {
        if(userInfo == null) {
            return null;
        }
        UserInfoDto userInfoDto = new UserInfoDto();
        User user = userInfo.getUser();
        if(user != null) {
            userInfoDto.setUserId(user.getUserId());
            userInfoDto.setUserDisplayedName(user.getUserDisplayedName());
            userInfoDto.setUserType(user.getUserType());
        }
        userInfoDto.setCountry(userInfo.getCountry());
        userInfoDto.setOffice(userInfo.getOffice());
        userInfoDto.setMiddleOffice(userInfo.getMiddleOffice());
        userInfoDto.setMail(userInfo.getMail());
        return userInfoDto;
    }

    /**
     * This method converts data from a DTO object to an entity object
     * @param userInfoDto a DTO object
     * @see com.pagonxt.onetradefinance.external.backend.api.model.UserInfoDto
     * @see com.pagonxt.onetradefinance.integrations.model.UserInfo
     * @return an entity object
     */
    public UserInfo toModel(UserInfoDto userInfoDto) {
        if (userInfoDto == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        User user = new User();
        user.setUserId(userInfoDto.getUserId());
        user.setUserDisplayedName(userInfoDto.getUserDisplayedName());
        user.setUserType(userInfoDto.getUserType());
        userInfo.setUser(user);
        userInfo.setCountry(userInfoDto.getCountry());
        userInfo.setOffice(userInfoDto.getOffice());
        userInfo.setMiddleOffice(userInfoDto.getMiddleOffice());
        userInfo.setMail(userInfoDto.getMail());
        return userInfo;
    }
}
