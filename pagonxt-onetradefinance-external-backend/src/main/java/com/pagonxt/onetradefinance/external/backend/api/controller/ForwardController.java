package com.pagonxt.onetradefinance.external.backend.api.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This controller has two functions:
 * Refresh the browser page without losing the context and allow the URL to be accessed directly.
 * @author -
 * @version jdk-11.0.13
 * @since jdk-11.0.13
 */
@RestController
public class ForwardController {

    /**
     * forward controller method
     * @param req httpServlet request
     * @param resp httpServlet response
     * @throws ServletException servlet exception
     * @throws IOException io exception
     */
    @GetMapping("${view.path}/**")
    @Secured("ROLE_USER")
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/").forward(req, resp);
    }
}
