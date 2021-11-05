package scc.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import scc.application.dto.UserLoginDto;
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
        UserLoginDto loginDto = users.getById(username).orElseThrow(() -> new UsernameNotFoundException(username));
        System.out.println("dffgiojdgdgdf");
        return User.withUsername(loginDto.getName())
                .password(loginDto.getPwd())
                .roles("USER")
                .build();
    }
}
