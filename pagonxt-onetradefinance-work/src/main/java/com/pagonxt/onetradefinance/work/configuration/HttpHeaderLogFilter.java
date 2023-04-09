package com.pagonxt.onetradefinance.work.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @see org.springframework.web.filter.GenericFilterBean
 * @since jdk-11.0.13
 */
public class HttpHeaderLogFilter extends GenericFilterBean {

    //A Logger object is used to log messages for a specific system or application component
    private static final Logger LOG = LoggerFactory.getLogger(HttpHeaderLogFilter.class);

    /**
     * Class method
     * @param servletRequest    : a ServletRequest object
     * @param servletResponse   : a ServletResponse object
     * @param filterChain       : a FilterChain object
     * @throws IOException      : handles IO exceptions
     * @throws ServletException : handles Servlet exceptions
     * @see javax.servlet.ServletRequest
     * @see javax.servlet.ServletResponse
     * @see javax.servlet.FilterChain
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException,
            ServletException {
        LOG.debug("Executing LogFilter");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        Enumeration<String> requestHeaders = httpServletRequest.getHeaderNames();
        try {
            while (requestHeaders.hasMoreElements()) {
                String header = requestHeaders.nextElement();
                if (LOG.isDebugEnabled()) {
                    LOG.debug(String.format("Request header: %s => %s", header, httpServletRequest.getHeader(header)));
                }
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            Collection<String> responseHeaders = httpServletResponse.getHeaderNames();
            responseHeaders.forEach(header -> LOG.debug(
                    String.format("Response header: %s => %s", header, httpServletResponse.getHeader(header))
            ));
        }
    }
}
