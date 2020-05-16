package com.newcode.meeting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.UserLike;
import com.newcode.meeting.pojo.UserPojo;
import com.newcode.meeting.domain.dto.UserPageDto;
import com.newcode.meeting.dto.EventType;
import com.newcode.meeting.dto.ObjectType;
import com.newcode.meeting.repo.UserLikeRepo;
import com.newcode.meeting.repo.UserRepo;
import com.newcode.meeting.util.WsSender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserLikeService {
    private final UserRepo userRepo;
    private final UserLikeRepo likeRepo;
    private final WsSender ws;


    public UserLikeService(UserRepo userRepo, UserLikeRepo likeRepo, WsSender ws) {
        this.userRepo = userRepo;
        this.likeRepo = likeRepo;
        this.ws = ws;
    }

    public UserPageDto getUsersMyLike(User user, Pageable pageable) {
        user = userRepo.findUserById(user.getId());
        Page<UserPojo> page = likeRepo.findUsersMyLike(user, pageable);
        return new UserPageDto(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages());
    }

    public boolean setUserLike(User ownerLike, UserPojo newUserPojo, User userFromDb){

        Set<UserLike> usersSet = ownerLike.getLikes();
        if (!newUserPojo.isMeLiked()) {
            UserLike like = usersSet.stream()
                    .filter(likeUser ->
                            likeUser.getLike().equals(userFromDb) && likeUser.getOwner().equals(ownerLike)
                    ).findAny().orElse(null);
            usersSet.remove(like);
            userRepo.save(ownerLike);
        }else {
            UserLike like = new UserLike(userFromDb, ownerLike);
            usersSet.add(like);
            userRepo.save(ownerLike);
            UserLike userLike = likeRepo.findByLikeAndOwner(ownerLike, userFromDb);
            if (userLike != null){
                userFromDb.setLikeNew(1);
                ownerLike.setLikeNew(1);
                List<User> usersList = new ArrayList<>();
                usersList.add(userFromDb);
                usersList.add(ownerLike);
                userRepo.saveAll(usersList);
                usersList.forEach(user -> {
                    try {
                        ws.wsSender(1L, user, ObjectType.NEWLIKE, EventType.UPDATE);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        return true;
    }
    public UserPageDto getUsersCoincidence(User user, Pageable pageable) {
        user = userRepo.findUserById(user.getId());
        user.setLikeNew(null);
        Set<User> users = user.getLikes().stream()
                .map(UserLike::getLike)
                .collect(Collectors.toSet());
        if(users.isEmpty()){
            return new UserPageDto(
                    new ArrayList<>(),
                    0,
                    0);
        }else {

            Page<UserPojo> page = likeRepo.findUsersByCoincidence(users, user, pageable);
            userRepo.save(user);
            return new UserPageDto(
                    page.getContent(),
                    page.getNumber(),
                    page.getTotalPages());
        }
    }
}
