package com.newcode.meeting.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.Image;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.service.ImageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("image")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @JsonView(Views.FullProfile.class)
    @PostMapping("/photo")
    public Image addUrlImage(
            @AuthenticationPrincipal User user,
            @RequestBody Image newImage
    ) throws IOException {
        return imageService.addImage(newImage, user);
    }

    @JsonView(Views.FullProfile.class)
    @DeleteMapping("/photo/{id}")
    public void delete(
            @AuthenticationPrincipal User user,
            @PathVariable("id") Image image
    ) {
        imageService.remove(image, user);
    }

    @JsonView(Views.IdName.class)
    @PutMapping("/photo/{id}")
    public Image update(
            @AuthenticationPrincipal User userPrincipal,
            @PathVariable("id") Image imageFromDb,
            @RequestBody Image image
    ) {
        return imageService.editImageMainAndHide(imageFromDb, image, userPrincipal);
    }

}
