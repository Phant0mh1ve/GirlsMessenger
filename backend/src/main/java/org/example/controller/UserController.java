package org.example.controller;

import org.example.dto.userDto.*;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> index() {
        return userService.index();
    }

    @GetMapping("/{id}")
    public UserDTO get(@PathVariable Long id) {
        return userService.get(id);
    }

    @PostMapping
    public UserDTO save(@RequestBody UserCreateDTO dto) {
        return userService.save(dto);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id, @RequestBody UserUpdateDTO dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}

