package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.userDto.*;
import org.example.entity.User;
import org.example.exception.ResourceAlreadyExistsException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional //все методы транзакционные
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Autowired
    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<UserDTO> index(){
        return mapper.map(repository.findAll());
    }

    public UserDTO get(Long id){
        User user = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + id + " not found!"));
        return mapper.map(user);
    }

    public UserDTO save(UserCreateDTO dto){
        repository.findByUsername(dto.getUsername()).ifPresent(user -> {
            throw new ResourceAlreadyExistsException("User " + user.getUsername() + " already exists!");
        });

        repository.findByEmail(dto.getEmail()).ifPresent(user -> {
            throw new ResourceAlreadyExistsException("User with email " + user.getEmail() + " already exists!");
        });

        User user = mapper.map(dto);
        repository.save(user);

        return mapper.map(user);
    }

    public UserDTO update(Long id, UserUpdateDTO dto){
        User user = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + id + " not found!"));

        if (user.getEmail().equals(dto.getEmail().get())){
            throw new ResourceAlreadyExistsException("Your email is already a" + user.getEmail());
        }

        if (user.getUsername().equals(dto.getUsername().get())){
            throw new ResourceAlreadyExistsException("Your username is already a " + user.getUsername());
        }

        repository.findByUsername(dto.getUsername().get()).ifPresent(_user -> {
            throw new ResourceAlreadyExistsException("User " + _user.getUsername() + " already exists!");
        });

        repository.findByEmail(dto.getEmail().get()).ifPresent(_user -> {
            throw new ResourceAlreadyExistsException("User with email " + _user.getEmail() + " already exists!");
        });

        mapper.update(dto, user);
        repository.save(user);
        return mapper.map(user);
    }

    public void delete(Long id){
        User user = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + id + " not found!"));
        repository.delete(user);
    }
}
