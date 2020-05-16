package com.newcode.meeting.repo;

import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.UserLike;
import com.newcode.meeting.domain.UserLikeId;
import com.newcode.meeting.pojo.UserPojo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserLikeRepo extends JpaRepository<UserLike, UserLikeId> {

    @Query("select new com.newcode.meeting.pojo.UserPojo(" +
            " us.owner, " +
            "(SUM (case  when ul = :user then 1 else 0 end) > 0), " +
            " us.timeLike ) " +
            " from UserLike us left join us.like ul  where " +
            " us.owner IN :users and ul IN :user  group by us")
    Page<UserPojo> findUsersByCoincidence(
            @Param("users") Set<User> users,
            @Param("user") User user,
            Pageable pageable
    );

    @Query("select new com.newcode.meeting.pojo.UserPojo( us.owner, " +
            "(SUM (case  when ul = :user then 1 else 0 end) > 0 )," +
            " us.timeLike) " +
            " from UserLike us left join us.like ul  where " +
            " ul IN :user  group by us")
    Page<UserPojo> findUsersMyLike(@Param("user") User user, Pageable pageable);

    UserLike findByLikeAndOwner(User userFromDb, User ownerLike);

}
