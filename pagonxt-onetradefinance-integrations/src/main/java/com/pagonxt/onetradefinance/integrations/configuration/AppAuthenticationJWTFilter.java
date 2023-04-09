package com.pagonxt.onetradefinance.integrations.configuration;

import com.pagonxt.onetradefinance.integrations.model.exception.ServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Key;

/**
 * Filter class to authenticate the communication between the external app and work.
 *
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@Component
@Profile("santander | santander_local")
public class AppAuthenticationJWTFilter extends OncePerRequestFilter {

    /**
     * Logger
     */
    private static final Logger LOG = LoggerFactory.getLogger(AppAuthenticationJWTFilter.class);

    /**
     * hmac Key
     */
    private final Key hmacKey;

    /**
     * Constructor.
     *
     * @param hmacKey : the hmac key
     */
    public AppAuthenticationJWTFilter(Key hmacKey) {
        this.hmacKey = hmacKey;
    }

    /**
     * Method to authenticate the communication between the external app and work.
     *
     * @param request     : the request
     * @param response    : the response
     * @param filterChain : the filter chain
     * @throws ServletException the servlet exception
     * @throws IOException      the IO exception
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String internalAppHeader = request.getHeader("internalAppHeader");
        if (internalAppHeader != null) {
            Jws<Claims> jwt;
            try {
                jwt = Jwts.parserBuilder()
                        .setSigningKey(hmacKey)
                        .build()
                        .parseClaimsJws(internalAppHeader);
            } catch (Exception e) {
                LOG.error("The internal token jwt is not correct");
                throw new ServiceException("Invalid jwt token", "errorInternalCommunication");
            }
            String username = (String) jwt.getBody().get("name");
            SecurityContext context = SecurityContextHolder.createEmptyContext();
            Authentication authenticationResult = new AppAuthentication(username);
            context.setAuthentication(authenticationResult);
            SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
    }
}
