package br.dev.valmirt.login.controller;

import br.dev.valmirt.login.model.User;
import br.dev.valmirt.login.model.ResponseList;
import br.dev.valmirt.login.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/list")
    public ResponseList<User> getUsers(@RequestParam(value = "page", defaultValue = "0")
                                int page,
                                @RequestParam(value = "size", defaultValue = "20")
                                int size){
        return userService.getUsers(page, size);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("{userId}/details")
    public User getUser (@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping("/save")
    public User saveUser(@Valid @RequestBody User user){
        user.setId(0L);
        return userService.saveUser(user);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/update")
    public User updateUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("{userId}/authority")
    public String changeAuthorities(@PathVariable Long userId) {
        userService.changeAuthorities(userId);

        return "Successful! Changed user authorities with id " + userId;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("{userId}/situation")
    public String changeSituation(@PathVariable Long userId) {
        userService.changeSituation(userId);

        return "Successful! Changed user situation with id " + userId;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("{userId}/delete")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return "User deleted - id:" + userId;
    }

}
