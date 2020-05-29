package com.newcode.meeting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.newcode.meeting.domain.Chat;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.dto.EventType;
import com.newcode.meeting.dto.ObjectType;
import com.newcode.meeting.repo.ChatRepo;
import com.newcode.meeting.repo.UserRepo;
import com.newcode.meeting.util.WsSender;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MainService {
    private final ChatRepo chatRepo;
    private final WsSender ws;
    private final UserRepo userRepo;


    public MainService(ChatRepo chatRepo, WsSender ws, UserRepo userRepo) {
        this.chatRepo = chatRepo;
        this.ws = ws;
        this.userRepo = userRepo;
    }

    public void updateMessage(User user) throws JsonProcessingException {

        if (!user.getChats().isEmpty()) {
            List<Chat> chats = chatRepo.findChatsAndMessages(user);
            User userTo = new User();
            if (!chats.isEmpty()) {
                for (Chat chat : chats) {
                    chat.getMembers().forEach(us -> {
                        if (!us.getId().equals(user.getId()))
                            userTo.setId(us.getId());
                    });
                    ws.wsSender(chat.getId(), userTo, ObjectType.MESSAGE, EventType.DELIVEREDAll);
                }
            }
            chatRepo.updateMessageDelivered(user.getChats(), user);
            chatRepo.updateMessageViewedPage(user.getChats(), user);
        }
    }
}
