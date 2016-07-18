package org.retrotime.configuration.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * Created by vzhemevko on 04.10.15.
 */
public class AuthUser extends User {

    /**
     */
    private static final long serialVersionUID = 2865653316391759556L;

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
}
