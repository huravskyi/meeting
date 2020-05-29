package com.newcode.meeting.service;

import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.UserView;
import com.newcode.meeting.domain.dto.UserViewDto;
import com.newcode.meeting.repo.UserViewRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class UserViewService {
    private final UserViewRepo userViewRepo;

    public UserViewService(UserViewRepo userViewRepo) {
        this.userViewRepo = userViewRepo;
    }

    public UserViewDto getListWhoViewsUser(User owner, Pageable pageable) {
        Page<UserView> page = userViewRepo.findByOwner(owner, pageable);
        return new UserViewDto(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages());
    }

    public boolean deleteUserView(User user) {
        userViewRepo.deleteAllByOwner(user);
        return true;
    }
}
