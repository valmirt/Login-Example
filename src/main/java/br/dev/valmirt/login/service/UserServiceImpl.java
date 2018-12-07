package br.dev.valmirt.login.service;

import br.dev.valmirt.login.model.ResponseList;
import br.dev.valmirt.login.model.Situation;
import br.dev.valmirt.login.model.User;
import br.dev.valmirt.login.repository.UserRepository;
import br.dev.valmirt.login.security.UserSpringSecurity;
import br.dev.valmirt.login.system.exception.AuthorizationException;
import br.dev.valmirt.login.system.exception.DataException;
import br.dev.valmirt.login.system.exception.NotFoundException;
import br.dev.valmirt.login.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public ResponseList<User> getUsers(int page, int size) {
        List<User> current = userRepository.findAllActive();
        ResponseList<User> temp = new ResponseList<>();

        int totalPages = (current.size() / size);

        List<User> aux;
        if (page == totalPages) {
            aux = current.subList(page * size, (page * size) + current.size() % size);
        } else {
            aux = current.subList(page * size, ((page * size) + (size)));
        }

        temp.setCurrentPage(page);
        temp.setTotalPages(totalPages);
        temp.setTotalResults((long) current.size());
        temp.setResults(aux);

        return temp;
    }

    @Override
    public User getUser(Long id) {
        UserSpringSecurity temp = getUserAuthenticated();

        if (Utils.isNullOrEmpty(temp)) {
            throw new AuthorizationException("Access denied!");
        } else {
            if (!temp.hasRole("ROLE_ADMIN")) {
                if (!id.equals(temp.getId())) {
                    throw new AuthorizationException("Access denied!");
                }
                return userRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("User not found - id: " + id));
            }
            return userRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("User not found - id: " + id));
        }
    }

    @Override
    public User saveUser(User user) {
        if (user.getId() == 0) return userRepository.save(checkUserData(user));
        else return userRepository.save(updateUserData(user));
    }

    @Override
    public void deleteUser(Long id) {
        User temp = getUser(id);
        temp.setSituation(Situation.INACTIVE);
        userRepository.save(temp);
    }

    @Override
    public UserSpringSecurity getUserAuthenticated() {
        try {
            return (UserSpringSecurity) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void changeAuthorities(Long id) {
        UserSpringSecurity temp = getUserAuthenticated();
        if (!Utils.isNullOrEmpty(temp) && temp.getId().equals(id)){
            throw new DataException("Error! Impossible change self authority.");
        }
        User temp2 = getUser(id);

        if (temp2.getSituation() == Situation.ACTIVE) {
            temp2.setAdmAuth(!temp2.getAdmAuth());
            userRepository.save(temp2);
        } else throw new DataException("Error! Attempt to change authority of an inactive user");
    }

    @Override
    public void changeSituation(Long id) {
        UserSpringSecurity temp = getUserAuthenticated();
        if (!Utils.isNullOrEmpty(temp) && temp.getId().equals(id)){
            throw new DataException("Error! Impossible change self situation.");
        }
        User temp2 = getUser(id);

        temp2.setSituation(temp2.getSituation().equals(Situation.ACTIVE) ?
                Situation.INACTIVE :
                Situation.ACTIVE);
        userRepository.save(temp2);
    }

    private User updateUserData(User user) {
        User temp = getUser(user.getId());

        if (!Utils.isNullOrEmpty(user.getNickname()) &&
                !temp.getNickname().equals(user.getNickname()))
            temp.setNickname(user.getNickname());
        if (!Utils.isNullOrEmpty(user.getName()) &&
                !temp.getName().equals(user.getName()))
            temp.setName(user.getName());
        if (!Utils.isNullOrEmpty(user.getEmail()) &&
                !Utils.isValidEmail(user.getEmail()) &&
                !temp.getEmail().equals(user.getEmail()))
            temp.setEmail(user.getEmail());

        return temp;
    }

    private User checkUserData(User user) {
        if (Utils.isNullOrEmpty(user.getNickname())) throw new DataException("Error field nickname");
        if (!Utils.isValidEmail(user.getEmail())) throw new DataException("Invalid email");
        if (Utils.isValidPassword(user.getPassword()))
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        else throw new DataException("Password must contain letters and numbers and contain 6 to 15 characters");

        return user;
    }
}
