package com.newcode.meeting.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.ProfileDetail;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.pojo.UserPojo;
import com.newcode.meeting.service.ProfileDetailService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("profileDetail")
public class ProfileDetailController {

    private final ProfileDetailService detailService;

    public ProfileDetailController(ProfileDetailService detailService) {
        this.detailService = detailService;
    }

    @JsonView(Views.FullProfileDetail.class)
    @PutMapping("/about/{id}")
    public ProfileDetail update(
            @AuthenticationPrincipal User user,
            @PathVariable("id") ProfileDetail detailFromDb,
            @RequestBody ProfileDetail detail
    ) {
        return detailService.editProfile(detail, detailFromDb, user);
    }

    @JsonView(Views.FullProfileDetailImage.class)
    @GetMapping("{id}")
    public UserPojo get(@PathVariable("id") User userOwner, @AuthenticationPrincipal User userGuest) {
        return detailService.getUserProfile(userOwner, userGuest);
    }


}
