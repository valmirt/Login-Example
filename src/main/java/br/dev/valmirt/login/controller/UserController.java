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
    ResponseList<User> getUsers(@RequestParam(value = "page", defaultValue = "0")
                                int page,
                                @RequestParam(value = "size", defaultValue = "20")
                                int size){
        return userService.getUsers(page, size);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("find/{userId}")
    User getUser (@PathVariable Long userId) {
        return userService.getUser(userId);
    }

    @PostMapping("/save")
    User saveUser(@Valid @RequestBody User user){
        user.setId(0L);
        return userService.saveUser(user);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/update")
    User updateUser(@RequestBody User user){
        return userService.saveUser(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/save-adm")
    String saveAdm(@RequestBody User user) {
        userService.saveAdm(user);

        return "MS01";
    }

    @PreAuthorize("hasAnyRole('USER')")
    @DeleteMapping("delete/{userId}")
    String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);

        return "MS01";
    }

}
