package com.newcode.meeting.service;

import com.newcode.meeting.domain.*;
import com.newcode.meeting.domain.dto.Look;
import com.newcode.meeting.pojo.UserPojo;
import com.newcode.meeting.repo.ProfileDetailRepo;
import com.newcode.meeting.repo.UserLikeRepo;
import com.newcode.meeting.repo.UserRepo;
import com.newcode.meeting.repo.UserViewRepo;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Set;

@Service
public class ProfileDetailService {
    private final String BLOCKED_IMAGE = "https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/image.jpg?alt=media&token=55626585-4958-4d5a-9cac-ac2c5f5e00a0";

    private final ProfileDetailRepo detailRepo;
    private final UserRepo userRepo;
    private final UserViewRepo userViewRepo;
    private final UserLikeRepo likeRepo;

    public ProfileDetailService(ProfileDetailRepo detailRepo, UserRepo userRepo, UserViewRepo userViewRepo, UserLikeRepo likeRepo) {
        this.detailRepo = detailRepo;
        this.userRepo = userRepo;
        this.userViewRepo = userViewRepo;
        this.likeRepo = likeRepo;
    }


    public ProfileDetail editProfile(ProfileDetail detail, ProfileDetail detailFromDb, User user) {
        user = userRepo.findUserById(user.getId());
        if (detail.getTextAbout() != null)
            detailFromDb.setTextAbout(detail.getTextAbout());
        if (detail.getTarget() != null)
            detailFromDb.setTarget(detail.getTarget());

        Look look = check(detail.getLook(), detailFromDb.getLook());
        detailFromDb.setLook(look);

        if (detail.getPersonalInformation() != null)
            detailFromDb.setPersonalInformation(detail.getPersonalInformation());
        if (detail.getHobby() != null)
            detailFromDb.setHobby(detail.getHobby());

        user.setProfileDetail(detailFromDb);
        detailFromDb.setUser(user);

        detailRepo.save(detailFromDb);
        return detailFromDb;
    }

    private Look check(Look look, Look lookFromDb) {
        if (look.getHair() != null)
            lookFromDb.setHair(look.getHair());
        if (look.getHeight() != 0)
            lookFromDb.setHeight(look.getHeight());
        if (look.getWeight() != 0)
            lookFromDb.setWeight(look.getWeight());
        if (look.getEyes() != null)
            lookFromDb.setEyes(look.getEyes());
        if (look.getBody() != null)
            lookFromDb.setBody(look.getBody());
        if (look.getType() != null)
            lookFromDb.setType(look.getType());
        return lookFromDb;
    }

    private void setLastVisitProfileUser(User userOwner, User userGuest) {
        userGuest = userRepo.findUserById(userGuest.getId());
        if (!userGuest.getId().equals(userOwner.getId())) {
            UserView userView =
                    userViewRepo.findByUserViewAndOwnerAndLastVisitUser(userGuest, userOwner, LocalDate.now());
            if (userView == null) {
                UserView newUserView = new UserView();
                newUserView.setLastVisitUser(LocalDate.now());
                newUserView.setUserView(userGuest);
                newUserView.setOwner(userOwner);

                userViewRepo.save(newUserView);
            }
        }
    }

    public UserPojo getUserProfile(User userOwner, User userGuest) {
        setLastVisitProfileUser(userOwner, userGuest);
        userGuest = userRepo.findUserById(userGuest.getId());
        UserPojo userPojo = userRepo.findByUserDto(userOwner, userGuest);
        UserLike like = likeRepo.findByLikeAndOwner(userOwner, userGuest);
        if (like == null) {
            Set<Image> imageSet = userPojo.getImages();
            imageSet.forEach(image -> {
                if (image.isHide()) {
                    try {
                        image.setUrlLink(new URL(BLOCKED_IMAGE));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return userPojo;
    }
}
