package org.example.controller;

import org.example.dto.chatDto.*;
import org.example.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/chat")
public class ChatController {
    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping
    public List<ChatDTO> index() {
        return chatService.index();
    }

    @GetMapping("/{id}")
    public ChatDTO get(@PathVariable Long id) {
        return chatService.get(id);
    }

    @PostMapping
    public ChatDTO save(@RequestBody ChatCreateDTO dto) {
        return chatService.save(dto);
    }

    @PutMapping("/{id}")
    public ChatDTO update(@PathVariable Long id, @RequestBody ChatUpdateDTO dto) {
        return chatService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        chatService.delete(id);
    }
}

