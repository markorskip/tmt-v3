package io.efficientsoftware.tmt_v3.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class ActuatorUserDetailsService implements UserDetailsService {

    public static final String ACTUATOR_ADMIN = "ACTUATOR_ADMIN";

    @Override
    public UserDetails loadUserByUsername(final String username) {
        if ("actuator".equals(username)) {
            return User.withUsername(username)
                    .password("password")
                    .authorities(ACTUATOR_ADMIN)
                    .build();
        }
        throw new UsernameNotFoundException("User " + username + " not found");
    }

}
