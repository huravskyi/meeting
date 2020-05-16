package com.newcode.meeting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.Image;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.domain.dto.ImageDto;
import com.newcode.meeting.service.AdminService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("adminUser")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private AdminService adminService;
    public static final int IMAGE_PAGE = 10;


    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @JsonView(Views.IdName.class)
    @GetMapping("/get-admin")
    public List<Long> updateUser() {
        return adminService.findIdAdmins();
    }

    @JsonView(Views.IdName.class)
    @PutMapping("/user-update/{id}")
    public User updateUser(@PathVariable("id") User userFromDb,
                           @RequestBody User user ) throws MessagingException {

        return adminService.updateUserByAdmin(userFromDb, user);
    }

    @JsonView(Views.FullProfile.class)
    @GetMapping("/user")
    public User get( @RequestParam Map<String, String> form){
        return adminService.getUserBy(form);
    }

    @JsonView(Views.FullProfile.class)
    @GetMapping("/images")
    public ImageDto getImage(@RequestParam String event,
                             @PageableDefault(size = IMAGE_PAGE, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable){
        return adminService.getImageForAdmin(event, pageable);
    }

    @JsonView(Views.FullProfile.class)
    @GetMapping("/images-by-user")
    public ImageDto getImageById(@RequestParam User user){
        return adminService.getImageByUser(user);
    }


    @DeleteMapping("deleteAndWarning")
    public Long deleteAndWarning(Image image, User user) throws MessagingException {
        return adminService.deleteAndWarningAdmin(image, user);
    }

    @JsonView(Views.FullProfile.class)
    @PutMapping("images/{id}")
    public boolean  setApprove(
            @PathVariable("id") Image  imageFromDb,
            @RequestBody boolean tested
    ){
        System.out.println(tested);
        return adminService.setApproveImage(imageFromDb, tested);
    }
}
