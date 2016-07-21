package org.retrotime.configuration.spring;

import org.retrotime.service.ContentService;
import org.retrotime.service.ContentServiceImpl;
import org.retrotime.service.RetroService;
import org.retrotime.service.RetroServiceImpl;
import org.retrotime.service.TeamService;
import org.retrotime.service.TeamServiceImpl;
import org.retrotime.service.UserService;
import org.retrotime.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@EnableWebSecurity
@Configuration
@ComponentScan(basePackages = "org.retrotime.*")
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    @Bean
    public Md5PasswordEncoder passwordEncoder() {
        return new Md5PasswordEncoder();
    }
    
    // ====== Beans for the tests ========
    
    @Bean
    public TeamService teamService() {
    	return new TeamServiceImpl();
    }
    
    @Bean
    public UserService userService() {
    	return new UserServiceImpl();
    }
    
    
    @Bean
    public RetroService retroService() {
    	return new RetroServiceImpl();
    }
    
    @Bean
    public ContentService contentService() {
    	return new ContentServiceImpl();
    }
}
