package com.newcode.meeting.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.newcode.meeting.domain.Chat;
import com.newcode.meeting.domain.Message;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.domain.dto.ChatDto;
import com.newcode.meeting.dto.EventType;
import com.newcode.meeting.dto.ObjectType;
import com.newcode.meeting.dto.WsEventsDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WsSender {
    //    отвечае за очередь и отправку сообщениц
    private SimpMessagingTemplate simpMessagingTemplate;
    //    сирелезует и дисерелезует обекты
    private ObjectWriter writerMessage;
    private ObjectWriter writerChat;
    private ObjectWriter writerChatDto;

    private WsSender(SimpMessagingTemplate template, ObjectMapper mapper) {
        this.simpMessagingTemplate = template;
        this.writerMessage = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(Views.IdMessage.class);
        this.writerChat = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(Views.FullProfile.class);
        this.writerChatDto = mapper
                .setConfig(mapper.getSerializationConfig())
                .writerWithView(Views.ChatIdName.class);
    }


    public void wsSender(Message message, User userTo, ObjectType objectType, EventType eventType) throws JsonProcessingException {
        String value = writerMessage.writeValueAsString(message);
        simpMessagingTemplate.convertAndSend("/topic/activity" + userTo.getId(),
                new WsEventsDto(objectType, eventType, value));
    }

    public void wsSender(Long id, User userTo, ObjectType objectType, EventType eventType) throws JsonProcessingException {
        String value = writerMessage.writeValueAsString(id);
        simpMessagingTemplate.convertAndSend("/topic/activity" + userTo.getId(),
                new WsEventsDto(objectType, eventType, value));
    }

    public void wsSender(Chat chat, User userTo, ObjectType objectType, EventType eventType) throws JsonProcessingException {
        String value = writerChat.writeValueAsString(chat);
        simpMessagingTemplate.convertAndSend("/topic/activity" + userTo.getId(),
                new WsEventsDto(objectType, eventType, value));
    }
    public void wsSender(ChatDto chatDto, User userTo, ObjectType objectType, EventType eventType) throws JsonProcessingException {
        String value = writerChatDto.writeValueAsString(chatDto);
        simpMessagingTemplate.convertAndSend("/topic/activity" + userTo.getId(),
                new WsEventsDto(objectType, eventType, value));
    }
}
