package org.example.mapper;

import jakarta.persistence.EntityManager;
import lombok.NoArgsConstructor;
import org.example.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.TargetType;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public abstract class ReferenceMapper {
    @Autowired
    private EntityManager entityManager;

    @Named("ChatsToIds")
    public List<Long> ChatsToIds(List<Chat> chats) {
        if (chats == null) return null;
        return chats.stream().map(Chat::getId).collect(Collectors.toList());
    }
    @Named("IdsToChats")
    public List<Chat> IdsToChats(List<Long> ids){
        if (ids == null) return null;
        return ids.stream()
                .map(id -> toEntity(id, Chat.class))
                .collect(Collectors.toList());
    }



    @Named("UsersToIds")
    public List<Long> UsersToIds(List<User> users) {
        if (users == null) return null;
        return users.stream().map(User::getId).collect(Collectors.toList());
    }
    @Named("IdsToUsers")
    public List<User> IdsToUsers(List<Long> ids){
        if (ids == null) return null;
        return ids.stream()
                .map(id -> toEntity(id, User.class))
                .collect(Collectors.toList());
    }



    @Named("MessagesToIds")
    public List<Long> MessagesToIds(List<Message> messages) {
        if (messages == null) return null;
        return messages.stream()
                .map(Message::getId)
                .collect(Collectors.toList());
    }
    @Named("IdsToMessages")
    public List<Message> IdsToMessages(List<Long> ids){
        if (ids == null) return null;
        return ids.stream()
                .map(id -> toEntity(id, Message.class))
                .collect(Collectors.toList());
    }



    @Named("AttachmentsToIds")
    public List<Long> FilesToIds(List<Attachment> messages) {
        if (messages == null) return null;
        return messages.stream()
                .map(Attachment::getId)
                .collect(Collectors.toList());
    }
    @Named("IdsToAttachments")
    public List<Attachment> IdsToFiles(List<Long> ids){
        if (ids == null) return null;
        return ids.stream()
                .map(id -> toEntity(id, Attachment.class))
                .collect(Collectors.toList());
    }



    @Named("ChatToId")
    public Long chatToId(Chat chat) {
        return chat != null ? chat.getId() : null;
    }
    @Named("IdToChat")
    public Chat idToChat(Long id) {
        return id != null ? entityManager.find(Chat.class, id) : null;
    }



    @Named("UserToId")
    public Long userToId(User user) {
        return user != null ? user.getId() : null;
    }
    @Named("IdToUser")
    public User idToUser(Long id) {
        return id != null ? entityManager.find(User.class, id) : null;
    }


    public <T extends BaseEntity> T toEntity(Long id, @TargetType Class<T> entityClass) {
        return id != null ? entityManager.find(entityClass, id) : null;
    }
}
