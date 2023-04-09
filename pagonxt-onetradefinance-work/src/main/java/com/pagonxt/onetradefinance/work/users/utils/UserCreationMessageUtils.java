package com.pagonxt.onetradefinance.work.users.utils;

import com.pagonxt.onetradefinance.work.users.bot.Response;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * class with user creation message utils
 * @author -
 * @version jdk-11.0.13
 * @see java.util.ResourceBundle
 * @since jdk-11.0.13
 */
@Component
public class UserCreationMessageUtils {

    private static final String EN = "messageEN";
    private static final String ES = "messageESP";
    private static final String LOGIN_NAME_EXISTS = "login-name-exists";
    private static final String USER_CREATED_SUCCESSFULLY = "user-created-successfully";

    //class attribute
    private final Map<String, ResourceBundle> bundles;

    /**
     * constructor method
     */
    public UserCreationMessageUtils() {
        this.bundles = new HashMap<>();
        this.bundles.put(ES, ResourceBundle.getBundle("i18n.Response_message", new Locale("es")));
        this.bundles.put(EN, ResourceBundle.getBundle("i18n.Response_message", new Locale("en")));
    }

    /**
     * class method
     * @param id : a string object with the id
     * @see com.pagonxt.onetradefinance.work.users.bot.Response
     * @return a Response object
     */
    public Response allLanguagesLoginMsgError(String id) {

       return getResponse(LOGIN_NAME_EXISTS, false, id);
    }

    /**
     * class method
     * @see com.pagonxt.onetradefinance.work.users.bot.Response
     * @return a Response object
     */
    public Response userCreated() {

        return getResponse(USER_CREATED_SUCCESSFULLY, true);
    }

    /**
     * class method
     * @param key       : a string object with the key
     * @param success   : a Boolean value
     * @param arguments : a string object with arguments
     * @see com.pagonxt.onetradefinance.work.users.bot.Response
     * @return a Response object
     */
    private Response getResponse(String key, boolean success, String... arguments) {
        Response response = new Response();
        response.setResponse(EN, String.format(getBundle(EN).getString(key), (Object[]) arguments));
        response.setResponse(ES,  String.format(getBundle(ES).getString(key), (Object[]) arguments));
        response.setSuccess(success);

        return response;
    }

    /**
     * class method
     * @param language : a string with the language
     * @see java.util.ResourceBundle
     * @return a ResourceBundle object
     */
    private ResourceBundle getBundle(String language) {

        return this.bundles.get(language);
    }

}
