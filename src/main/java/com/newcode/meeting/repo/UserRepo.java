package com.newcode.meeting.repo;


import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.dto.Gender;
import com.newcode.meeting.dto.Role;
import com.newcode.meeting.pojo.UserPojo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    List<User> findByRolesContains(Role role);

    User findByEmail(String email);


    User findUserById(Long id);

    User findByActivationCode(String code);

    @Query("select new com.newcode.meeting.pojo.UserPojo( us, " +
            "(SUM (case  when ul.like = :userGuest then 1 else 0 end) > 0)," +
            " us.lastVisit ) " +
            " from User us left join us.likes ul  where us =:userOwner")
    UserPojo findByUserDto(@Param("userOwner") User userOwner, @Param("userGuest") User userGuest);


//    @Query("select new com.newcode.meeting.pojo.UserPojo( " +
//            " us, " +
//            " (SUM (case  when ul.like = :user then 1 else 0 end) > 0), " +
//            " ul.timeLike ) " +
//            " from User us left join us.likes ul  where " +
//            " us.birthDate BETWEEN :maxDate and :minDate " +
//            " and  us.gender =:sex group by us ")
//    Page<UserPojo> findUsersByBirthDateIsBetweenAndGenderDto(
//            Pageable pageable,
//            @Param("maxDate") LocalDate maxDate,
//            @Param("minDate") LocalDate minDate,
//            @Param("sex") Gender sex,
//            @Param("user") User user
//    );

    @Query("select  us from User us where " +
            " us.birthDate BETWEEN :maxDate and :minDate " +
            " and us.gender =:sex and  us.city =:localeName and us.active = true group by us ")
    Page<User> findUsersByBirthDateIsBetweenAndGenderCityDto(
            @Param("localeName") String localeName,
            Pageable pageable,
            @Param("maxDate") LocalDate maxDate,
            @Param("minDate") LocalDate minDate,
            @Param("sex") Gender sex
    );

    @Query("select  us from User us where " +
            " us.birthDate BETWEEN :maxDate and :minDate " +
            " and us.gender =:sex and  us.region =:localeName and us.active = true group by us ")
    Page<User> findUsersByBirthDateIsBetweenAndGenderRegionDto(
            @Param("localeName")String localeName,
            Pageable pageable,
            @Param("maxDate") LocalDate maxDate,
            @Param("minDate") LocalDate minDate,
            @Param("sex") Gender sex
    );
    @Query("select  us from User us where " +
            " us.birthDate BETWEEN :maxDate and :minDate " +
            " and us.gender =:sex and  us.country =:localeName and us.active = true group by us ")
    Page<User> findUsersByBirthDateIsBetweenAndGenderCountryDto(
            @Param("localeName") String localeName,
            Pageable pageable,
            @Param("maxDate") LocalDate maxDate,
            @Param("minDate") LocalDate minDate,
            @Param("sex") Gender sex
    );

    @Query("select  us from User us where " +
            " us.birthDate BETWEEN :maxDate and :minDate " +
            " and us.gender =:sex and us.active = true group by us ")
    Page<User> findUsersByBirthDateIsBetweenAndGenderDto(
            Pageable pageable,
            @Param("maxDate") LocalDate maxDate,
            @Param("minDate") LocalDate minDate,
            @Param("sex") Gender sex
    );

    User findUserByEmail(String model);
}