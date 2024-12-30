package com.mibess.loginserver.controller;

import com.mibess.loginserver.dto.UserDTO;
import com.mibess.loginserver.dto.request.UserRequest;
import com.mibess.loginserver.dto.request.UserUpdateRequest;
import com.mibess.loginserver.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDTO register(@Valid @RequestBody UserRequest user) {
        return userService.register(user);
    }

    @PutMapping("{id}")
    public UserDTO update(@PathVariable String id, @Valid  @RequestBody UserUpdateRequest user) {
        return userService.update(id, user);
    }

    @PutMapping("{id}/disable")
    public void disable(@PathVariable String id) {
        userService.disable(id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        userService.delete(id);
    }

    @GetMapping("{id}")
    public UserDTO findById(@PathVariable String id) {
        return userService.findById(id);
    }

    @GetMapping
    public List<UserDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping("email")
    public UserDTO findByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }

}
