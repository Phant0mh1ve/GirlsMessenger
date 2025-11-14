package org.example.service;

import org.example.Messenger;
import org.example.dto.chatDto.ChatDTO;
import org.example.dto.userDto.UserCreateDTO;
import org.example.dto.userDto.UserDTO;
import org.example.dto.userDto.UserUpdateDTO;
import org.example.entity.User;
import org.example.exception.ResourceAlreadyExistsException;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;

import org.instancio.Instancio;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@SpringBootTest(classes = Messenger.class)
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    UserRepository userRepository;

    //userService.index()
    @Test
    public void noUsers_index_ThenEmptyList(){
        when(userRepository.findAll()).thenReturn(List.of());

        var usersList = userService.index();

        Assertions.assertTrue(usersList.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    public void indexUsers_ReturnsList() {
        var user = Instancio.of(User.class).create();
        user.setId(1L);

        var userDTO = Instancio.of(UserDTO.class).create();
        userDTO.setId(1L);

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.map(List.of(user))).thenReturn(List.of(userDTO));

        var result = userService.index();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(userDTO, result.get(0));
        verify(userRepository).findAll();
        verify(userMapper).map(List.of(user));
    }

    //userService.get()
    @Test
    public void noUsers_getUser_ThenNotFound(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.get(1L));
        verify(userRepository).findById(1L);
    }

    @Test
    public void getUser_ThenUser(){
        var user = Instancio.of(User.class).create();
        user.setId(1L);
        var userDTO = Instancio.of(UserDTO.class).create();
        userDTO.setId(1L);

        when(userMapper.map(user)).thenReturn(userDTO);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Assertions.assertEquals(userService.get(1L), userDTO);
        verify(userRepository).findById(1L);
        verify(userMapper).map(user);
    }

    //userService.save()
    @Test
    public void saveUser_ThenUser(){
        var user = Instancio.of(User.class).create();
        user.setId(1L);
        when(userRepository.save(user)).thenReturn(user);

        var userDTO = Instancio.of(UserDTO.class).create();
        userDTO.setId(1L);
        var userCreateDTO = Instancio.of(UserCreateDTO.class).create();

        when(userMapper.map(userCreateDTO)).thenReturn(user);
        when(userMapper.map(user)).thenReturn(userDTO);

        Assertions.assertEquals(userService.save(userCreateDTO), userDTO);
        verify(userRepository).save(user);
        verify(userMapper).map(user);
        verify(userMapper).map(userCreateDTO);
    }

    @Test
    public void saveUser_AlreadyExists(){
        var userCreateDTO = Instancio.of(UserCreateDTO.class).create();
        var user = Instancio.of(User.class).create();
        user.setId(1L);

        when(userRepository.findByUsername(userCreateDTO.getUsername()))
                .thenReturn(Optional.of(user));
        when(userMapper.map(userCreateDTO)).thenReturn(user);

        Assertions.assertThrows(ResourceAlreadyExistsException.class,
                ()-> userService.save(userCreateDTO));
        verify(userRepository).findByUsername(userCreateDTO.getUsername());
        verify(userRepository, never()).save(user);
        verify(userMapper, never()).map(user);
    }

    //userService.update()
    @Test
    public void updateUser_NotExists(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        var userUpdateDTO = Instancio.of(UserUpdateDTO.class).create();
        var user = Instancio.of(User.class);

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.update(1L, userUpdateDTO));
        verify(userRepository).findById(1L);
    }

    @Test
    public void updateUser_ExistingEmail(){
        var user = new User("existingUser", "user@example.com", "pass");
        user.setId(1L);

        var _user = new User("otherUser", "other@example.com", "pass");
        _user.setId(2L);

        var userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setUsername(JsonNullable.of("newUsername"));
        userUpdateDTO.setEmail(JsonNullable.of("other@example.com"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(userUpdateDTO.getEmail().get())).thenReturn(Optional.of(_user));

        Assertions.assertThrows(ResourceAlreadyExistsException.class,
                () -> userService.update(1L, userUpdateDTO));

        verify(userRepository).findById(1L);
        verify(userRepository).findByEmail(userUpdateDTO.getEmail().get());
    }

    @Test
    public void updateUser_ExistingUsername(){
        var user = new User("existingUser", "user@example.com", "pass");
        user.setId(1L);

        var _user = new User("otherUser", "other@example.com", "pass");
        _user.setId(2L);

        var userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setUsername(JsonNullable.of("otherUser")); // совпадает с другим пользователем
        userUpdateDTO.setEmail(JsonNullable.of("new@example.com"));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername(userUpdateDTO.getUsername().get())).thenReturn(Optional.of(_user));

        Assertions.assertThrows(ResourceAlreadyExistsException.class,
                () -> userService.update(1L, userUpdateDTO));

        verify(userRepository).findById(1L);
        verify(userRepository).findByUsername(userUpdateDTO.getUsername().get());
    }


    @Test
    public void updateUser_Success(){
        var user = new User("test", "test@gmail.com", "lssektoe");
        var userUpdateDTO = new UserUpdateDTO();
        userUpdateDTO.setUsername(JsonNullable.of("John_Doe"));
        userUpdateDTO.setEmail(JsonNullable.of("JohnDoe@gmail.com"));
        var userDTO = Instancio.of(UserDTO.class).create();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.map(user)).thenReturn(userDTO);

        var result = userService.update(1L, userUpdateDTO);

        verify(userMapper).update(userUpdateDTO, user);
        verify(userMapper).map(user);
        Assertions.assertEquals(userDTO, result);
    }

    //userService.delete()
    @Test
    public void deleteUser_Success(){
        var user = Instancio.of(User.class).create();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository).findById(1L);
        verify(userRepository).delete(user);
    }

    @Test
    public void deleteUser_NotFound(){
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.delete(1L));

        verify(userRepository).findById(1L);
        verify(userRepository, never()).delete(any());
    }

}
