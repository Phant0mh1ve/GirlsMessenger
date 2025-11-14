package org.example.controller;

import org.example.service.MessageService;
import org.example.dto.messageDto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/chat/{chatId}")
    public List<MessageDTO> getMessagesByChat(@PathVariable Long chatId) {
        return messageService.getMessagesByChat(chatId);
    }

    @GetMapping("/{id}")
    public MessageDTO getById(@PathVariable Long id) {
        return messageService.getById(id);
    }

    @PostMapping
    public MessageDTO create(@RequestBody MessageCreateDTO dto) {
        return messageService.create(dto);
    }

    @PutMapping("/{id}")
    public MessageDTO update(@PathVariable Long id, @RequestBody MessageUpdateDTO dto) {
        return messageService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        messageService.delete(id);
    }
}

