package com.newcode.meeting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.domain.dto.UserViewDto;
import com.newcode.meeting.service.UserViewService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("userView")
public class UserViewController {
    public static final int USER_WHO_VIEWS_PAGE = 15;
    public final UserViewService userViewService;

    public UserViewController(UserViewService userViewService) {
        this.userViewService = userViewService;
    }


    @DeleteMapping("clean-viewed/{id}")
    public boolean delete(@PathVariable("id")User user){
        return userViewService.deleteUserView(user);
    }

    @JsonView(Views.FullProfileDetail.class)
    @GetMapping("/usersListViews")
    public UserViewDto getUsersListWhoViews(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = USER_WHO_VIEWS_PAGE, sort = {"id"}, direction = Sort.Direction.DESC)
                    Pageable pageable
    ){
        return userViewService.getListWhoViewsUser(user, pageable);
    }
}
