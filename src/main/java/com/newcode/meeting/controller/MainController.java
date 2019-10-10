package com.newcode.meeting.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.dto.Role;
import com.newcode.meeting.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.HashMap;

@Controller
@RequestMapping("/")
public class MainController {

    @Value("${spring.profile.active:prod}")
    private String profile;

    private final UserRepo userRepo;

    private final ObjectWriter profileWriter;


    @Autowired
    public MainController(ObjectMapper mapper, UserRepo userRepo) {
        this.userRepo = userRepo;
        ObjectMapper objectMapper = mapper.setConfig(mapper.getSerializationConfig());

        this.profileWriter = objectMapper.writer();
    }


    @GetMapping
    public String main(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("1", "start");
        System.out.println(user);

        if(user != null) {
            model.addAttribute("profile", profileWriter.writeValueAsString(user.toString()));
            model.addAttribute("frontendData", data);
            model.addAttribute("isDevMode", "dev".equals(profile));
            return "index";
        }else {
            model.addAttribute("profile", "null");
            model.addAttribute("frontendData", "null");
            model.addAttribute("isDevMode", "dev".equals(profile));
            return "redirect:/login";
        }

    }
}
