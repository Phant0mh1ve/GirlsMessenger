package org.example.mapper;

import org.example.dto.userDto.UserCreateDTO;
import org.example.dto.userDto.UserDTO;
import org.example.dto.userDto.UserUpdateDTO;
import org.example.entity.Chat;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        uses = { JsonNullableMapper.class, ReferenceMapper.class, ChatMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    public abstract User map(UserCreateDTO dto);

    @Mapping(target = "chats", source = "chats", qualifiedByName = "ChatsToIds")
    public abstract UserDTO map(User user);

    public abstract List<UserDTO> map(List<User> user);

    @Mapping(target = "chats", source = "chats", qualifiedByName = "IdsToChats")
    public abstract void update(UserUpdateDTO dto, @MappingTarget User user);
}
