package com.newcode.meeting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.Chat;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.domain.dto.UserPageDto;
import com.newcode.meeting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.net.MalformedURLException;

@RestController
@RequestMapping("user")
public class UserController {
    public static final int USER_PAGE = 16;


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @DeleteMapping("delete/{id}")
    public boolean deleteAccount(@AuthenticationPrincipal User user) throws MalformedURLException {
        return userService.accountDelete(user);
    }

    @JsonView(Views.FullProfileDetailAndEmail.class)
    @PutMapping("emailOrPassword/{id}")
    public User editEmailOrPassword(
            @PathVariable("id") User userFromDb,
            @RequestBody User newUser
    ) {
        return userService.editEmailPassword(userFromDb, newUser);
    }

    @JsonView(Views.City.class)
    @PutMapping("{id}")
    public User editUser(
            @PathVariable("id") User oldUser,
            @RequestBody User newUser
    ) {
        return userService.editUser(oldUser, newUser);

    }

    @JsonView(Views.FullProfileDetail.class)
    @GetMapping
    public UserPageDto getUsers(
            @RequestParam("ageMin") String ageMin,
            @RequestParam("ageMax") String ageMax,
            @RequestParam("gender") String gender,
            @RequestParam("localeType") String localeType,
            @RequestParam("localeName") String localeName,
            @AuthenticationPrincipal User user,
            @PageableDefault(size = USER_PAGE, sort = {"birthDate"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        return  userService.getAllUser(localeName,localeType, user, ageMax, ageMin, gender, pageable);
    }

    @JsonView(Views.FullProfileDetail.class)
    @DeleteMapping("removeChat/{id}")
    public Long removeChat(
            @PathVariable("id") Chat removeChat,
            @AuthenticationPrincipal User userFromDb
    ){
        return userService.deleteChat(removeChat, userFromDb);
    }

    @JsonView(Views.FullProfileDetail.class)
    @PutMapping("block/{id}")
    public User updateBlock(
            @PathVariable("id") User updateBlockUser,
            @AuthenticationPrincipal User userFromDb
    ){
        return userService.updateUserBlock(updateBlockUser, userFromDb);
    }
}
