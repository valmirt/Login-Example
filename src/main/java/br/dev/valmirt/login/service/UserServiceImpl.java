package br.dev.valmirt.login.service;

import br.dev.valmirt.login.model.ResponseList;
import br.dev.valmirt.login.model.Situation;
import br.dev.valmirt.login.model.User;
import br.dev.valmirt.login.repository.UserRepository;
import br.dev.valmirt.login.security.UserSpringSecurity;
import br.dev.valmirt.login.system.exception.AuthorizationException;
import br.dev.valmirt.login.system.exception.DataException;
import br.dev.valmirt.login.system.exception.NotFoundException;
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

        int totalPages = (current.size()/size);

        List<User> aux;
        if (page == totalPages) {
            aux = current.subList(page*size, (page*size)+current.size() % size);
        } else {
            aux = current.subList(page*size, ((page*size)+(size)));
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

        if (temp == null){
            throw new AuthorizationException("Acesso Negado");
        } else {
            if (!temp.hasRole("ROLE_ADMIN")) {
                if (!id.equals(temp.getId())) {
                    throw new AuthorizationException("Acesso Negado");
                }
                return userRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("User not found - id: " + id));
            }
            return userRepository.findById(id)
                    .orElseThrow(()-> new NotFoundException("User not found - id: "+id));
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
        try{
            return (UserSpringSecurity) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
        } catch (Exception e) { return null; }
    }

    @Override
    public void saveAdm(User user) {
        User temp = getUser(user.getId());

        if (temp.getSituation() == Situation.ACTIVE){
            temp.setAdmAuth(true);
            userRepository.save(temp);
        } else throw new DataException("Attempt to make inactivated user an administrator");
    }

    private User updateUserData(User user) {
        User temp = getUser(user.getId());

        if (user.getNickname() != null &&
                !temp.getNickname().equals(user.getNickname()))
            temp.setNickname(user.getNickname());
        if (user.getName() != null &&
                !temp.getName().equals(user.getName()))
            temp.setName(user.getName());
        if (user.getEmail() != null &&
                !temp.getEmail().equals(user.getEmail()))
            temp.setEmail(user.getEmail());
        if (user.getCpf() != null &&
                !temp.getCpf().equals(user.getCpf()) &&
                isValidCpf(user.getCpf()))
            temp.setCpf(user.getCpf());

        return temp;
    }

    private User checkUserData(User user) {
        if (user.getNickname().isEmpty()) throw new DataException("ME05");
        if (isValidEmail(user.getEmail())) throw new DataException("ME02");
        if (!isValidCpf(user.getCpf())) throw new DataException("ME05");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));

        return user;
    }

    private boolean isValidEmail(String email) {
        return false;
    }

    private boolean isValidCpf(String cpf) {
        return true;
    }
}
