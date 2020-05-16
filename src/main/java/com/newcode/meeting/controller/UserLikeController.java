package com.newcode.meeting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.pojo.UserPojo;
import com.newcode.meeting.domain.dto.UserPageDto;
import com.newcode.meeting.service.UserLikeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("userLike")
public class UserLikeController {
    public static final int USER_PAGE = 1;
    private final UserLikeService likeService;

    public UserLikeController(UserLikeService likeService) {
        this.likeService = likeService;
    }

    @JsonView(Views.FullProfileDetail.class)
    @GetMapping("myLikes")
    public UserPageDto getLikeMeUsersList(
            @AuthenticationPrincipal User user,
            @PageableDefault
                    (size = USER_PAGE, sort = {"timeLike"}, direction = Sort.Direction.DESC)
                    Pageable pageable
    ){
        return likeService.getUsersMyLike(user, pageable);
    }

    @PutMapping("like/{id}")
    public boolean like(
            @PathVariable("id") User ownerLike,
            @RequestBody UserPojo newUserPojo,
            @AuthenticationPrincipal User userFromDb
    ) {
        return likeService.setUserLike(ownerLike, newUserPojo, userFromDb);
    }

    @JsonView(Views.FullProfileDetail.class)
    @GetMapping("coincidence")
    public UserPageDto getCoincidenceList(
            @AuthenticationPrincipal User user,
            @PageableDefault
                    (size = USER_PAGE, sort = {"timeLike"}, direction = Sort.Direction.DESC)
                    Pageable pageable
    ){
        return likeService.getUsersCoincidence(user, pageable);
    }
}
