package com.newcode.meeting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.newcode.meeting.domain.Chat;
import com.newcode.meeting.domain.Message;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.domain.dto.MessagePageDto;
import com.newcode.meeting.service.MessageService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("messages")
public class MessageController {
    public static final int MESSAGE_PER_PAGE = 5;
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("new-list")
    @JsonView(Views.IdMessage.class)
    public MessagePageDto getNewListMessage(Chat chat, @AuthenticationPrincipal User author) throws JsonProcessingException {

        return messageService.getNewListOldMessageListForChat(chat, author);
    }


    @PostMapping
    @JsonView(Views.IdMessage.class)
    public Message saveMessage(
            @RequestBody Message message,
            @AuthenticationPrincipal User author
    ) throws IOException {
        return messageService.saveNewMessage(message, author);
    }

    @PutMapping("/delivered/{id}")
    @JsonView(Views.IdName.class)
    public Boolean updateDeliveredMessage(@PathVariable("id") Message message, @AuthenticationPrincipal User user) {
        return messageService.updateMessageDelivered(message, user);
    }

    @PostMapping("messageIds")
    @JsonView(Views.IdName.class)
    public Boolean updateMessageViewed(
            @AuthenticationPrincipal User user,
            @RequestBody Chat chat
    ) throws JsonProcessingException {
        return messageService.editMessageViewed(chat, user);
    }

    @JsonView(Views.IdMessage.class)
    @GetMapping
    public MessagePageDto messageList(
            Chat chat,
            String numberOfNewMessage,
            @PageableDefault(size = MESSAGE_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC)
                    Pageable pageable
    ) {
        return messageService.getMessageListForChat(chat, pageable, numberOfNewMessage);
    }

    @JsonView(Views.IdMessage.class)
    @GetMapping("old")
    public MessagePageDto messageListOld(
            Chat chat,
            Long idMessage,
            @PageableDefault(size = MESSAGE_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC)
                    Pageable pageable
    ) {
        return messageService.getOldMessageListForChat(chat, pageable, idMessage);
    }

    @JsonView(Views.IdMessage.class)
    @GetMapping("new")
    public MessagePageDto messageListNew(
            Chat chat,
            @AuthenticationPrincipal User user,
            @PageableDefault(size = MESSAGE_PER_PAGE, sort = {"id"}, direction = Sort.Direction.DESC)
                    Pageable pageable
    ) {
        return messageService.getNewMessageListForChat(chat, pageable);
    }
}
