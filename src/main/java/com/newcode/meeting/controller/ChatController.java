package com.newcode.meeting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.newcode.meeting.domain.Chat;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.domain.dto.ChatDto;
import com.newcode.meeting.service.ChatService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("chat")
    @JsonView(Views.ChatIdName.class)
    public ChatDto addChat(@RequestBody Chat chat, @AuthenticationPrincipal User userFrom) throws IOException {
        return chatService.saveChat(chat, userFrom);
    }
    @GetMapping("chat")
    @JsonView(Views.ChatIdName.class)
    public List<ChatDto> getChats(@AuthenticationPrincipal User userFromDb){
         return chatService.getChatsList(userFromDb);
    }

    @GetMapping("chatBlock")
    @JsonView(Views.ChatIdName.class)
    public List<ChatDto> getChatsBlock(@AuthenticationPrincipal User userFromDb){
        return chatService.getChatsBlockList(userFromDb);

    }
}

