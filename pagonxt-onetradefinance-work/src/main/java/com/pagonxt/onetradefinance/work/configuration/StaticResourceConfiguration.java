package com.pagonxt.onetradefinance.work.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class
 * This can make development faster and easier by eliminating the need
 * to define certain beans included in the auto-configuration classes.
 * @author -
 * @version jdk-11.0.13
 * @see org.springframework.web.servlet.config.annotation.WebMvcConfigurer
 * @see org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
 * Stores registrations of resource handlers for serving static resources such as images,
 * css files and others through Spring MVC including setting cache headers optimized for
 * efficient loading in a web browser. Resources can be served out of locations under web
 * application root, from the classpath, and others.
 * To create a resource handler, use addResourceHandler(String...) providing the URL path patterns
 * for which the handler should be invoked to serve static resources (e.g. "/resources/**").
 *
 * Then use additional methods on the returned ResourceHandlerRegistration to add one or more
 * locations from which to serve static content from (e.g. {"/", "classpath:/META-INF/public-web-resources/"})
 * or to specify a cache period for served resources.
 * @since jdk-11.0.13
 */
@Configuration(proxyBeanMethods = false)
public class StaticResourceConfiguration implements WebMvcConfigurer {

    public static final String PUBLIC = "classpath:/public/";

    /**
     * class method
     * @param registry a ResourceHandlerRegistry object
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.setOrder(10)
                .addResourceHandler("/ext/*.js", "/*/ext/*.js")
                .addResourceLocations("classpath:/static/ext/", "classpath:/public/ext/")
                .setCacheControl(CacheControl.noCache());

        registry.setOrder(20)
                .addResourceHandler("/ext/*.css", "/*/ext/*.css")
                .addResourceLocations("classpath:/static/ext/", "classpath:/public/ext/")
                .setCacheControl(CacheControl.noCache());

        registry.setOrder(30)
                .addResourceHandler("/*.js")
                .addResourceLocations(PUBLIC)
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(40)
                .addResourceHandler("/*.css")
                .addResourceLocations(PUBLIC)
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(50)
                .addResourceHandler("/*.woff")
                .addResourceLocations(PUBLIC)
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(60)
                .addResourceHandler("/*.woff2")
                .addResourceLocations(PUBLIC)
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(70)
                .addResourceHandler("/*.svg")
                .addResourceLocations(PUBLIC, "classpath:/public/twemoji/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(80)
                .addResourceHandler("/*.map")
                .addResourceLocations(PUBLIC)
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(90)
                .addResourceHandler("/*.png")
                .addResourceLocations(PUBLIC)
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));

        registry.setOrder(100)
                .addResourceHandler("/*.ico")
                .addResourceLocations(PUBLIC)
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS));
    }
}