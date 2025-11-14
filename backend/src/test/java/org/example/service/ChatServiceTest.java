package org.example.service;

import org.example.Messenger;
import org.example.dto.chatDto.ChatCreateDTO;
import org.example.dto.chatDto.ChatDTO;
import org.example.dto.chatDto.ChatUpdateDTO;
import org.example.entity.Chat;
import org.example.exception.ResourceNotFoundException;
import org.example.mapper.ChatMapper;
import org.example.repository.ChatRepository;
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
public class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @MockitoBean
    private ChatMapper chatMapper;

    @MockitoBean
    private ChatRepository chatRepository;

    //chatService.index()
    @Test
    public void noChats_index_ThenEmptyList() {
        when(chatRepository.findAll()).thenReturn(List.of());

        var chatList = chatService.index();

        Assertions.assertTrue(chatList.isEmpty());
        verify(chatRepository).findAll();
    }

    @Test
    public void indexChats_ReturnsList() {
        var chat = new Chat("Test Chat", new ArrayList<>());
        chat.setId(1L);

        var chatDTO = new ChatDTO();
        chatDTO.setId(1L);
        chatDTO.setName("Test Chat");

        when(chatRepository.findAll()).thenReturn(List.of(chat));
        when(chatMapper.map(List.of(chat))).thenReturn(List.of(chatDTO));

        var result = chatService.index();

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(chatDTO, result.get(0));
        verify(chatRepository).findAll();
        verify(chatMapper).map(List.of(chat));
    }

    //chatService.get()
    @Test
    public void getChat_NotFound() {
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> chatService.get(1L));

        verify(chatRepository).findById(1L);
    }

    @Test
    public void getChat_ReturnsChat() {
        var chat = new Chat("Test Chat", new ArrayList<>());
        chat.setId(1L);
        var chatDTO = new ChatDTO();
        chatDTO.setId(1L);
        chatDTO.setName("Test Chat");

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));
        when(chatMapper.map(chat)).thenReturn(chatDTO);

        var result = chatService.get(1L);

        Assertions.assertEquals(chatDTO, result);
        verify(chatRepository).findById(1L);
        verify(chatMapper).map(chat);
    }

    //chatService.save()
    @Test
    public void saveChat_ReturnsChat() {
        var chatDTO_ = new ChatDTO();
        chatDTO_.setName("New Chat");

        var chatDTO = new ChatCreateDTO();
        chatDTO.setName("New Chat");

        var chat = new Chat("New Chat", new ArrayList<>());

        when(chatMapper.map(chatDTO)).thenReturn(chat);
        when(chatRepository.save(chat)).thenReturn(chat);
        when(chatMapper.map(chat)).thenReturn(chatDTO_);

        var result = chatService.save(chatDTO);

        Assertions.assertEquals(chatDTO_, result);
        verify(chatMapper).map(chatDTO);
        verify(chatRepository).save(chat);
        verify(chatMapper).map(chat);
    }

    //chatService.update()
    @Test
    public void updateChat_NotFound() {
        var chatUpdateDTO = new ChatUpdateDTO();
        chatUpdateDTO.setName(JsonNullable.of("Updated Chat"));

        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> chatService.update(1L, chatUpdateDTO));

        verify(chatRepository).findById(1L);
    }

    @Test
    public void updateChat_Success() {
        var chat = new Chat("Old Chat", new ArrayList<>());
        chat.setId(1L);
        var chatUpdateDTO = new ChatUpdateDTO();
        chatUpdateDTO.setName(JsonNullable.of("Updated Chat"));

        var chatDTO = new ChatDTO();
        chatDTO.setId(1L);
        chatDTO.setName("Updated Chat");

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));
        when(chatMapper.map(chat)).thenReturn(chatDTO);

        var result = chatService.update(1L, chatUpdateDTO);

        verify(chatMapper).update(chatUpdateDTO, chat);
        verify(chatMapper).map(chat);
        Assertions.assertEquals(chatDTO, result);
    }

    //chatService.delete()
    @Test
    public void deleteChat_NotFound() {
        when(chatRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> chatService.delete(1L));

        verify(chatRepository).findById(1L);
        verify(chatRepository, never()).delete(any());
    }

    @Test
    public void deleteChat_Success() {
        var chat = new Chat("Chat to Delete", new ArrayList<>());
        chat.setId(1L);

        when(chatRepository.findById(1L)).thenReturn(Optional.of(chat));

        chatService.delete(1L);

        verify(chatRepository).findById(1L);
        verify(chatRepository).delete(chat);
    }
}

