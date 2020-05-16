package com.newcode.meeting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class RegistrationController {
    @Value("${spring.profile.active:prod}")
    private String profile;
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(Model model){
        helper(model);
        return "index";
    }

    @PostMapping("/login/forgotPassword")
    public String forgotPassword(
            Model model,
            @RequestParam("email") String email
    ) throws JsonProcessingException {
        if (userService.passwordReset(email)) {
            helper(model);
            return "redirect:/login/forgotPassword?success";
        } else {
            return "redirect:/login/forgotPassword?notFound";
        }
    }

    @GetMapping("/login/forgotPassword")
    public String getUserForgotPassword(Model model){
        helper(model);
        return "index";
    }

    @GetMapping("/registration/sendToMail")
    public String sendToMail(@AuthenticationPrincipal User user) {
        userService.sendToMail(user);
        return "redirect:/";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String date,
            @RequestParam String email,
            @RequestParam String gender,
            @RequestParam String city,
            @RequestParam String region,
            @RequestParam String country
    ) {
        if (userService.addUser(username, password, email, date, gender, city, region, country)) {
            return "redirect:/registration?success";
        } else {
            return "redirect:/registration?exist";
        }
    }

    @GetMapping("/registration")
    public String get(Model model){
        helper(model);
        return "index";
    }

    @JsonView(Views.FullProfile.class)
    @GetMapping("/activate/registrationNewEmail/{code}")
    public String activateNewEmail(
            @PathVariable String code
    ) {
        boolean isActivation = userService.isActivationEmail(code);
        if (isActivation) {
            return "redirect:/settings?isActivationEmail";
        } else {
            return "redirect:/settings?isActivationEmailerr";
        }
    }


    @JsonView(Views.FullProfile.class)
    @GetMapping("/activate/registration/{code}")
    public String activate(
            Model model,
            @PathVariable String code
    ){
        boolean isActivation = userService.isActivationUser(code);
        helper(model);
        if (isActivation) {
            return "redirect:/?isActivation";
        } else {
            return "redirect:/?isActivationerr";
        }
    }

    private void helper(Model model)  {
        model.addAttribute("profile", "null");
        model.addAttribute("chats", "null");
        model.addAttribute("chatsBlock", "null");
        model.addAttribute("isDevMode", "dev".equals(profile));
    }

}
