package br.dev.valmirt.login.service;

import br.dev.valmirt.login.model.ResponseList;
import br.dev.valmirt.login.model.User;
import br.dev.valmirt.login.security.UserSpringSecurity;

public interface UserService {

    ResponseList<User> getUsers(int page, int size);

    User getUser(Long id);

    User saveUser(User user);

    void deleteUser(Long id);

    UserSpringSecurity getUserAuthenticated();

    void changeAuthorities(Long id);

    void changeSituation(Long id);
}
