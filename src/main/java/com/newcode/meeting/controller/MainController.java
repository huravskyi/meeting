package com.newcode.meeting.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.newcode.meeting.domain.*;
import com.newcode.meeting.domain.dto.ChatDto;
import com.newcode.meeting.repo.UserRepo;
import com.newcode.meeting.service.ChatService;
import com.newcode.meeting.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/")
public class MainController {

    @Value("${spring.profile.active:prod}")
    private String profile;

    private final UserRepo userRepo;
    private final MainService mainService;
    private final ChatService chatService;


    private final ObjectWriter profileWriter;
    private final ObjectWriter chatWriter;

    @Autowired
    public MainController(ObjectMapper mapper, UserRepo userRepo, MainService mainService, ChatService chatService) {
        this.userRepo = userRepo;
        this.chatService = chatService;
        this.mainService = mainService;
        ObjectMapper objectMapper = mapper.setConfig(mapper.getSerializationConfig());

        this.profileWriter = objectMapper.writerWithView(Views.FullProfileDetailAndEmail.class);
        this.chatWriter = objectMapper.writerWithView(Views.ChatIdName.class);

    }


    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {
        if (user != null) {
            user = userRepo.findUserById(user.getId());
            if(user == null)
              return   "redirect:/login";
            String serializedProfile = profileWriter.writeValueAsString(user);
            model.addAttribute("profile", serializedProfile);

            mainService.updateMessage(user);
            List<ChatDto> chats = chatService.getChatsList(user);
            List<ChatDto> chatsBlock = chatService.getChatsBlockList(user);

            String serializedChat = chatWriter.writeValueAsString(chats);
            String serializedChatBlock = chatWriter.writeValueAsString(chatsBlock);
            model.addAttribute("chats", serializedChat);
            model.addAttribute("chatsBlock", serializedChatBlock);
            model.addAttribute("isDevMode", "dev".equals(profile));
            return "index";
        }
        return "redirect:/login";
    }
}