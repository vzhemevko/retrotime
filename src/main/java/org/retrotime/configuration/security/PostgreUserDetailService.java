package org.retrotime.configuration.security;

import org.apache.log4j.Logger;
import org.retrotime.model.User;
import org.retrotime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by vzhemevko on 04.10.15.
 */
@Component
public class PostgreUserDetailService implements UserDetailsService {

    public final static Logger logger = Logger.getLogger(PostgreUserDetailService.class);

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        checkIsAdminCreated();
        User user = userService.getUserByName(username).orElseThrow(() -> new UsernameNotFoundException (username));

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(UserService.Roles.USER_ROLE.toString()));
        return new AuthUser(user.getName(), user.getPasswd(), authorities);
    }

    private void checkIsAdminCreated() {
        String username = "admin";
        String personalName = "Admin Admin";
        String email = "admin@mail.com";
        String passwd = "admin";
        int role = UserService.Roles.SCRUM_MASTER_ROLE.intVal();

        if (userService.getUserByName(username).isPresent()) {
            return;
        }
        else {
            User user = new User();
            user.setName(username);
            user.setPersonalName(personalName);
            user.setEmail(email);
            user.setPasswd(passwd);
            user.setRole(role);
            userService.saveUser(user);
            logger.info("'admin' user was created.");
        }
    }
}
