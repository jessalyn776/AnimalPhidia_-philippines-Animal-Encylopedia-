package com.animalphidia.My_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve HTML files from static folder
        registry.addResourceHandler("/*.html")
                .addResourceLocations("classpath:/static/");

        // Serve CSS files
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");

        // Serve JS files
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");

        // Serve root path as index.html
        registry.addResourceHandler("/")
                .addResourceLocations("classpath:/static/index.html");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Redirect root to index.html
        registry.addViewController("/").setViewName("forward:/index.html");
    }
}