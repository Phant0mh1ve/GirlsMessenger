package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.messageDto.*;
import org.example.exception.*;

import org.example.entity.Message;
import org.example.entity.User;

import org.example.mapper.MessageMapper;
import org.example.repository.ChatRepository;
import org.example.repository.MessageRepository;
import org.example.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Transactional
public class MessageService {
    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageService(MessageRepository messageRepository, ChatRepository chatRepository, UserRepository userRepository, MessageMapper messageMapper) {
        this.messageRepository = messageRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.messageMapper = messageMapper;
    }

    public MessageDTO create(MessageCreateDTO messageCreateDTO){
        var user = userRepository.findById(messageCreateDTO.getSender()).orElseThrow(()
                -> new ResourceNotFoundException(
                        "User with id " + messageCreateDTO.getSender() + " does`nt exists!"));
        var chat = chatRepository.findById(messageCreateDTO.getChat()).orElseThrow(()
                -> new ResourceNotFoundException(
                "Chat with id " + messageCreateDTO.getChat() + " does`nt exists!"));

        if (!chat.getUsers().stream().map(User::getId).toList().contains(user.getId())){
            throw new RuntimeException("User with id " + user.getId()
                    + " can`t send messages to chat " + chat.getId() + "!");
        }

        var message = messageMapper.map(messageCreateDTO);
        message.setSender(user);
        message.setChat(chat);
        message.setCreatedAt(LocalDateTime.now());
        messageRepository.save(message);

        return messageMapper.map(message);
    }

    public MessageDTO update(Long messageId, MessageUpdateDTO updateDTO){
        var message = getMessageOrThrow(messageId);
        checkIsEditable(message);

        messageMapper.update(updateDTO, message);
        messageRepository.save(message);

        return messageMapper.map(message);
    }

    public void delete(Long messageId){
        var message = getMessageOrThrow(messageId);
        checkIsEditable(message);

        messageRepository.delete(message);
    }

    public MessageDTO getById(Long id){
        return messageMapper.map(getMessageOrThrow(id));
    }

    public List<MessageDTO> getMessagesByChat(Long chatId){
        return messageMapper.map(messageRepository.findByChatIdOrderByCreatedAtAsc(chatId));
    }

    private Message getMessageOrThrow(Long id) {
        return messageRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Message with id " + id + " doesn't exist"));
    }

    private void checkIsEditable(Message message){
        if (LocalDateTime.now().isAfter(message.getCreatedAt().plusDays(1))) {
            throw new ActionNotAllowedException("Message can’t be edited after 1 day!");
        }
    }
}
