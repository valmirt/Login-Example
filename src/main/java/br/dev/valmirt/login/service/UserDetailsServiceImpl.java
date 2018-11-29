package br.dev.valmirt.login.service;

import br.dev.valmirt.login.model.User;
import br.dev.valmirt.login.security.UserSpringSecurity;
import br.dev.valmirt.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);

        if (user == null) throw new UsernameNotFoundException(email);

        return new UserSpringSecurity(user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getAdmAuth() ?
                        AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN") :
                        AuthorityUtils.createAuthorityList("ROLE_USER"));
    }

    public UserDetails loadUserByToken(String email, String token) {
        User user = userRepository.findByEmail(email);

        if (user == null) return null;

        if (!user.getToken().equals(token)) return null;

        return new UserSpringSecurity(user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getAdmAuth() ?
                        AuthorityUtils.createAuthorityList("ROLE_USER", "ROLE_ADMIN") :
                        AuthorityUtils.createAuthorityList("ROLE_USER"));

    }

    public void saveNewToken(String email, String token) {
        User user = userRepository.findByEmail(email);

        if (user != null) {
            user.setToken(token);
            userRepository.save(user);
        }
    }
}
