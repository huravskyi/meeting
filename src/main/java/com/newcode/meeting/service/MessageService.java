package com.newcode.meeting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.newcode.meeting.domain.Chat;
import com.newcode.meeting.domain.Message;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.dto.ChatDto;
import com.newcode.meeting.dto.EventType;
import com.newcode.meeting.domain.dto.MessagePageDto;
import com.newcode.meeting.dto.ObjectType;
import com.newcode.meeting.repo.ChatRepo;
import com.newcode.meeting.repo.MessageRepo;
import com.newcode.meeting.repo.UserRepo;
import com.newcode.meeting.util.Meta;
import com.newcode.meeting.util.WsSender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MessageService {
    private final MessageRepo messageRepo;
    private final ChatRepo chatRepo;
    private final UserRepo userRepo;
    private final WsSender ws;

    public MessageService(
            MessageRepo messageRepo,
            ChatRepo chatRepo,
            UserRepo userRepo,
            WsSender wsSender) {

        this.messageRepo = messageRepo;
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
        this.ws = wsSender;
    }

    public Message saveNewMessage(Message message, User author) throws IOException {
        Chat chat = chatRepo.findChatById(message.getChat().getId());
        author = userRepo.findUserById(author.getId());
        User userTo = getUserTo(chat.getMembers(), author);

        if (userTo.getUsersBlock().contains(author)) {
            return new Message(0L);
        }

        return createMessage(chat, message, author, userTo);
    }

    private Message createMessage(Chat chat, Message message, User author, User userTo) throws IOException {
        chat.setUpdatedDate(LocalDateTime.now());
        chat.setLastMessage(message.getContent().getContent());
        message.setChat(chat);

        setNewMessage(message, author);

        Message newMessage = messageRepo.save(message);
        newMessage.setChat(new Chat(chat.getId()));

        List<Chat> chats = userTo.getChatsDeleted();
        if (chats.contains(chat)) {
            chats.remove(chat);
            userRepo.save(userTo);
            ChatDto chatDto = chatRepo.findChatDto(chat, author);
            chatDto.setMessages(Collections.singletonList(newMessage));
            ws.wsSender(chatDto, userTo, ObjectType.CHATDTO, EventType.CREATE);
        } else {
            ws.wsSender(newMessage, userTo, ObjectType.MESSAGE, EventType.CREATE);
        }
        return newMessage;
    }

    static void setNewMessage(Message message, User author) throws IOException {
        message.setAuthor(author);
        message.setDelivered(false);
        message.setViewed(false);
        message.setViewedPage(false);
        message.setCreationDate(LocalDateTime.now());
        message.getContent().setMessage(message);
        Meta.fillMeta(message.getContent());
    }

    public MessagePageDto getMessageListForChat(Chat chat, Pageable pageable, String numberOfNewMessage) {
        Page<Message> page;
        Page<Message> pageNotViewedPage;
        List<Message> messages = new ArrayList<>();
        int totalPageNotViewed = 0;
        int currentPageNotViewed = 0;

        if (Integer.parseInt(numberOfNewMessage) > 0) {
            page = messageRepo.findByChatAndViewedPage(chat, true, pageable);
            pageNotViewedPage = messageRepo.findByChatAndViewedPageOrderByIdAsc(chat, false, pageable);
            totalPageNotViewed = pageNotViewedPage.getTotalPages();
            currentPageNotViewed = pageNotViewedPage.getNumber();
            messages.addAll(pageNotViewedPage.getContent());
        } else {
            page = messageRepo.findByChat(chat, pageable);
        }
        messages.addAll(page.getContent());

        messages.sort(Comparator.comparing(Message::getId));
        return new MessagePageDto(
                messages,
                page.getNumber(),
                page.getTotalPages(),
                currentPageNotViewed,
                totalPageNotViewed
        );
    }

    public MessagePageDto getNewMessageListForChat(Chat chat, Pageable pageable) {
        Page<Message> page = messageRepo.findByChatAndViewedPageOrderByIdAsc(chat, false, pageable);

        return new MessagePageDto(
                page.getContent(),
                0,
                0,
                page.getNumber(),
                page.getTotalPages()
        );
    }

    public MessagePageDto getNewListOldMessageListForChat(Chat chat, User author) throws JsonProcessingException {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 6, sort);
        List<Long> ids = new ArrayList<>();
        List<Message> messages = messageRepo.findByChatAndViewedAndAuthorNot(chat, false, author);
        for (Message message : messages) {
            ids.add(message.getId());
        }
        chat.setMessages(messages);
        ws.wsSender(chat, getUserTo(chat.getMembers(), author), ObjectType.MESSAGE, EventType.VIEWED);
        messageRepo.updateMessageViewedAndViewedPageForIds(ids);
        return getMessagePageDto(chat, pageable);
    }
    public MessagePageDto getOldMessageListForChat(Chat chat, Pageable pageable){
        return getMessagePageDto(chat, pageable);
    }

    private MessagePageDto getMessagePageDto(Chat chat, Pageable pageable) {
        Page<Message> page = messageRepo.findByChatAndViewedPage(chat, true, pageable);
        List<Message> messages = new ArrayList<>(page.getContent());
        messages.sort(Comparator.comparing(Message::getId));
        return new MessagePageDto(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                0,
                0
        );
    }

    public Boolean updateMessageDelivered(Message message, User userFrom) {
        try {
            message = messageRepo.findMessageById(message.getId());
            message.setDelivered(true);
            messageRepo.save(message);
            User user = getUserTo(message.getChat().getMembers(), userFrom);

            ws.wsSender(message, user, ObjectType.MESSAGE, EventType.DELIVERED);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean editMessageViewed(Chat chat, User user) throws JsonProcessingException {
        List<Long> ids = new ArrayList<>();
        for (Message message : chat.getMessages()) {
            ids.add(message.getId());
        }
        messageRepo.updateMessageViewedForIds(ids);
        ws.wsSender(chat, getUserTo(chat.getMembers(), user), ObjectType.MESSAGE, EventType.VIEWED);
        return true;
    }

    private User getUserTo(List<User> users, User userFrom) {
        User userTo = new User();
        for (User u : users) {
            if (!u.getId().equals(userFrom.getId())) {
                userTo = u;
                break;
            }
        }
        return userTo;
    }
}
