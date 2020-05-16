package com.newcode.meeting.service;

import com.newcode.meeting.domain.Chat;
import com.newcode.meeting.domain.Message;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.dto.ChatDto;
import com.newcode.meeting.dto.EventType;
import com.newcode.meeting.dto.ObjectType;
import com.newcode.meeting.repo.ChatRepo;
import com.newcode.meeting.repo.MessageRepo;
import com.newcode.meeting.repo.UserRepo;
import com.newcode.meeting.util.WsSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final ChatRepo chatRepo;
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;
    private final WsSender ws;


    public ChatService(ChatRepo chatRepo, MessageRepo messageRepo, UserRepo userRepo, WsSender ws) {
        this.chatRepo = chatRepo;
        this.messageRepo = messageRepo;
        this.userRepo = userRepo;
        this.ws = ws;
    }

    private void setRemoveChatAndBlock(Chat chat, User userFromDb) {
        List<User> userList = chat.getMembers();
        User userTo = null;
        User userFrom = null;
        for (User user : userList) {
            if (!user.getId().equals(userFromDb.getId())){
                userTo = user;
            }else {
                userFrom = user;
            }
            List<Chat> chats = user.getChatsDeleted();
            if (chats != null) {
                if (chats.contains(chat)) {
                    chats.remove(chat);
                    user.setChatsDeleted(chats);
                }
            }
        }
        assert userFrom != null;
        userFrom.getUsersBlock().remove(userTo);
        userRepo.saveAll(userList);
    }

    public ChatDto saveChat(Chat chat, User userFrom) throws IOException {
        userFrom = userRepo.findUserById(userFrom.getId());
        User userTo = userRepo.findUserById(chat.getMembers().get(0).getId());

        if (userTo.getUsersBlock().contains(userFrom)) {
            return new ChatDto(0L);
        }

        Message message = chat.getMessages().get(0);

        Chat checkChat = chatRepo.findChatByMembersContainsAndMembersContains(userFrom, userTo);

        if (checkChat != null) {
            chat.setId(checkChat.getId());
        }

        MessageService.setNewMessage(message, userFrom);

        List<Message> messages = new ArrayList<>();
        List<User> members = new ArrayList<>();

        members.add(userTo);
        members.add(userFrom);

        messages.add(message);
        chat.setMessages(messages);
        chat.setMembers(members);
        chat.setCreationDate(LocalDateTime.now());
        chat.setUpdatedDate(LocalDateTime.now());
        chat.setLastMessage(message.getContent().getContent());
        chatRepo.save(chat);
        message.setChat(chat);
        messageRepo.save(message);

        if (checkChat != null) {
            setRemoveChatAndBlock(chat, userFrom);
        }

        ws.wsSender(chat, userTo, ObjectType.CHAT, EventType.CREATE);
        return new ChatDto(chat, 0L);
    }

    public List<ChatDto> getChatsList(User userFromDb) {
        userFromDb = userRepo.findUserById(userFromDb.getId());
        List<User> usersBlock = userFromDb.getUsersBlock();

        List<Chat> chats = new ArrayList<>();
        List<Chat> chatsDeleted = userFromDb.getChatsDeleted();
        if (usersBlock.isEmpty()) {
            chats.add(new Chat(1L));
        } else {
            chats = chatRepo.findChatsByMembersIn(usersBlock);
        }
        if (!chatsDeleted.isEmpty()) chats.addAll(chatsDeleted);

        return chatRepo.findChatsAndNumber(userFromDb, chats);
    }

    public List<ChatDto> getChatsBlockList(User userFromDb) {
        List<User> usersBlock = getUserBlock(userFromDb);
        List<Chat> chats = new ArrayList<>();
        if (usersBlock.isEmpty()) {
            return new ArrayList<ChatDto>();
        } else {
            chats = chatRepo.findChatsByMembersIn(usersBlock);
        }
        return chatRepo.findChatsBlock(userFromDb, chats);
    }

    private List<User> getUserBlock(User userFromDb) {
        userFromDb = userRepo.findUserById(userFromDb.getId());
        return userFromDb.getUsersBlock();
    }
}
