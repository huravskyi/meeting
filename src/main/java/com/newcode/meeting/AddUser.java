//package com.newcode.meeting;
//
//
//import com.newcode.meeting.domain.ProfileDetail;
//import com.newcode.meeting.domain.User;
//import com.newcode.meeting.domain.dto.Gender;
//import com.newcode.meeting.dto.Role;
//import com.newcode.meeting.repo.UserRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.UUID;
//
//@Service
//public  class AddUser {
//    @Autowired
//    private final UserRepo userRepo;
////    @Autowired
////    private PasswordEncoder passwordEncoder;
//
//
//
//    public AddUser(UserRepo userRepo) {
//        this.userRepo = userRepo;
//    }
//
//    public void addUsersMore() {
//
//        int k = 0;
//        while (k < 5) {
//            k++;
//            ProfileDetail detail = new ProfileDetail();
//            User user = new User();
//            detail.setUser(user);
//            user.setProfileDetail(detail);
//            user.setEmail("email.@FEMALE.gmail" + k);
//            user.setUsername("FEMALE " + k);
//            user.setGender(Gender.FEMALE);
//            user.setBirthDate(getDate("1979-03-19"));
//            user.setDateOfRegistration(LocalDateTime.now());
//            user.setActive(true);
//            user.setRoles(Collections.singleton(Role.USER));
//            user.setActivationCode(UUID.randomUUID().toString());
////            String password = "aaaaaa";
////            String newPassword = passwordEncoder.encode(password);
////            System.out.println("ZZZZZZ");
////            System.out.println(newPassword);
//            user.setPassword("$2a$08$WZB8k5N5YEV7w/qKjTNfFuHsv9nVJc7jf48WtPvmnicmDUjDtYEYa");
//
//            userRepo.save(user);
//        }
//    }
//    private LocalDate getDate(String dateString) {
//        LocalDate date = LocalDate.parse(dateString);
//        System.out.println(date);
//        return date;
//    }
//}
