package org.example.service;

import jakarta.transaction.Transactional;
import org.example.dto.chatDto.*;
import org.example.entity.Chat;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.ChatMapper;
import org.example.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ChatService {
    private final ChatRepository repository;
    private final ChatMapper mapper;

    @Autowired
    public ChatService(ChatRepository repository, ChatMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }
    
    public List<ChatDTO> index(){
        return mapper.map(repository.findAll());
    }
    
    public ChatDTO get(Long id){
        Chat chat = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat with id " + id + " not found!"));
        return mapper.map(chat);
    }

    public ChatDTO save(ChatCreateDTO dto){
        Chat chat = mapper.map(dto);
        repository.save(chat);

        return mapper.map(chat);
    }

    public ChatDTO update(Long id, ChatUpdateDTO dto){
        Chat chat = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat with id " + id + " not found!"));
        mapper.update(dto, chat);
        repository.save(chat);
        return mapper.map(chat);
    }

    public void delete(Long id){
        Chat chat = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Chat with id " + id + " not found!"));
        repository.delete(chat);
    }
}
