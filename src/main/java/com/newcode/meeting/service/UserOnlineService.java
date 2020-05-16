package com.newcode.meeting.service;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.repo.UserRepo;
import org.springframework.stereotype.Service;



@Service
public class UserOnlineService {
    private final UserRepo userRepo;

    public UserOnlineService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void setUserOnline(Long userId, boolean connect) {
        User user = userRepo.findUserById(userId);
        user.setOnline(connect);
        userRepo.save(user);
    }

}
