package com.adastragrp.iqc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@Configuration
@ComponentScan
@EnableAutoConfiguration
@Controller
@EnableZuulProxy
@EnableRedisHttpSession
public class GatewayApplication {

    @Value("${default.path}")
    private String defaultPath = "forward:/user_ui";


    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @RequestMapping("/")
    public String defaultPath() {
        return defaultPath;
    }

    @RequestMapping("/user")
    @ResponseBody
    public Map<String, Object> user(Principal user) {
        return Collections.<String, Object> singletonMap("name", user.getName());
    }


    @Configuration
    @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
    protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {

            auth.inMemoryAuthentication()
                    .withUser("user").password("password").roles("USER")
                        .and()
                    .withUser("admin").password("password").roles("ADMIN");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .httpBasic()
                    .and()
                    .authorizeRequests()
                    .antMatchers("/user_ui/**", "/admin_ui/**", "/").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .csrf().disable();
        }


    }
}
