package com.retailstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.security.Principal;

@SpringBootApplication
@EnableWebSecurity
@EnableDiscoveryClient

@RestController
@EnableResourceServer
public class AuthorizationServiceApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServiceApplication.class, args);
    }

    @RequestMapping("/user")
    @ResponseBody
    public Principal user(Principal user) {
        return user;
    }
    @RequestMapping("/cuser")
    public Object cuser(Principal user) {

        return  ((OAuth2Authentication) user).getPrincipal();
    }
    @RequestMapping("/getPrincipal")
    public Object getPrincipal(Principal user) {
        return ((OAuth2Authentication) user).getPrincipal();
    }
    @RequestMapping("/getDetails")
    public Object getDetails(Principal user) {
        return ((OAuth2Authentication) user).getDetails();
    }


    /**
     * 配置视图跳转
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/oauth/confirm_access").setViewName("authorize");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("js/**")
                .addResourceLocations("classpath:/js/");
        registry.addResourceHandler("css/**")
                .addResourceLocations("classpath:/css/");
    }

}
