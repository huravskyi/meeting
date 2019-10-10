package com.newcode.meeting.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.sevice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequestMapping("/")
public class RegistrationController {
    @Value("${spring.profile.active:prod}")
    private String profile;

    private final UserService userService;

    private final ObjectWriter profileWriter;


    @Autowired
    public RegistrationController(UserService userService, ObjectMapper mapper) {
        this.userService = userService;
        ObjectMapper objectMapper = mapper.setConfig(mapper.getSerializationConfig());

        this.profileWriter = objectMapper.writer();
    }

    @GetMapping("/login")
    public String login(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {
        if (user != null) {
            return "redirect:/";
        }
        helper(model, new User(), new HashMap<Object, Object>());
        return "index";
    }

    @PostMapping("/login/forgotPassword")
    public String forgotPassword(
            Model model,
            @RequestParam("email") String email
    ) throws JsonProcessingException {
        if (userService.passwordReset(email)) {
            helper(model, new User(), new HashMap<Object, Object>());
            return "redirect:/login";
        } else {
            return "redirect:/registration?notFound";
        }
    }

    @GetMapping("/login/forgotPassword")
    public String forgot(
            Model model,
            @AuthenticationPrincipal User user
    ) throws JsonProcessingException {
        if (user != null) {
            return "/";
        }
        helper(model, new User(), new HashMap<Object, Object>());
        return "index";
    }

    @GetMapping("/registration")
    public String registration(Model model, @AuthenticationPrincipal User user) throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();
        if (user != null) {
            helper(model, user, data);
            return "redirect:/";
        }

        helper(model, new User(), data);
        return "index";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        if (userService.addUser(user)) {
        } else {
            return "redirect:/registration?exist";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{registration}/{code}")
    public String activate(Model model, @PathVariable String code, @PathVariable String registration) throws JsonProcessingException {
        HashMap<Object, Object> data = new HashMap<>();
        data.put("confirm", "Подтвердите аккаунт пожалуйста");
        boolean isActivation = userService.isActivationUser(code);
        if (isActivation) {
            data.put("registration", "User successfully activation");
        } else {
            data.put("registration", "User not successfully activation");
        }
        helper(model, new User(), data);
        return "index";
    }

    private void helper(Model model, User user, HashMap<Object, Object> data) throws JsonProcessingException {
        String prof = "null";
        Object dataTo = "null";

        if (user.getId() != null)
            prof = profileWriter.writeValueAsString(user.toString());

        if (data != null)
            dataTo = data;

        model.addAttribute("profile", prof);
        model.addAttribute("frontendData", dataTo);
        model.addAttribute("isDevMode", "dev".equals(profile));
    }

}
