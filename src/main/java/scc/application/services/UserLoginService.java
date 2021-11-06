package scc.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import scc.application.repositories.UsersRepository;

@Service
public class UserLoginService implements UserDetailsService {

    private final UsersRepository users;

    @Autowired
    public UserLoginService(UsersRepository users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        scc.domain.entities.User login = users.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return User.withUsername(login.getId())
                .password(login.getPwd())
                .roles("USER")
                .build();
    }
}
