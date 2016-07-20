package org.retrotime.configuration.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

/**
 * Created by vzhemevko on 02.09.15.
 */
@EnableWebSecurity
@Component
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = Logger.getLogger(SecurityConfig.class);

    @Autowired
    UserDetailsService userDetailsService;


    @Autowired
    private Md5PasswordEncoder md5PasswordEncoder;


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        LOGGER.info("Setting up users.");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(md5PasswordEncoder);
        authProvider.setUserDetailsService(userDetailsService);
        ReflectionSaltSource saltSource = new ReflectionSaltSource();
        saltSource.setUserPropertyToUse("username");
        authProvider.setSaltSource(saltSource);
        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
            httpBasic()
                .and()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                    .httpBasic()
                .and()
                    .csrf().disable();/*.csrfTokenRepository(csrfTokenRepository())
                .and()
                    .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);*/
    }

    @Override
    public void configure(WebSecurity webSecurity) {
      webSecurity.ignoring().antMatchers(HttpMethod.POST, "/api/users/registration");
    }


    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-XSRF-TOKEN");
        return repository;
    }

    private class CsrfHeaderFilter extends OncePerRequestFilter {
        @Override
        protected void doFilterInternal(HttpServletRequest request,
                                        HttpServletResponse response, FilterChain filterChain)
                throws ServletException, IOException {
            CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
                    .getName());
            if (csrf != null) {
                Cookie cookie = WebUtils.getCookie(request, "X-XSRF-TOKEN");
                String token = csrf.getToken();
                if (cookie==null || token!=null && !token.equals(cookie.getValue())) {
                    cookie = new Cookie("X-XSRF-TOKEN", token);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
