package com.newcode.meeting.repo;

import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.UserView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;


@Repository
public interface UserViewRepo extends JpaRepository<UserView, Long> {

    UserView findByUserViewAndOwnerAndLastVisitUser(User userView, User owner, LocalDate date);

    Page<UserView> findByOwner(User owner, Pageable pageable);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    void deleteAllByOwner(User user);
}
