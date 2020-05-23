package com.newcode.meeting.service;


import com.newcode.meeting.domain.*;
import com.newcode.meeting.domain.dto.Gender;
import com.newcode.meeting.domain.dto.Look;
import com.newcode.meeting.pojo.UserPojo;
import com.newcode.meeting.domain.dto.UserPageDto;
import com.newcode.meeting.dto.Role;
import com.newcode.meeting.repo.UserRepo;
import com.newcode.meeting.repo.UserViewRepo;
import com.newcode.meeting.util.RandomHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Value("${domain}")
    private String domain;

    @Autowired
    private MailSender mailSender;

    private  final PasswordEncoder passwordEncoder;

    private final UserViewRepo userViewRepo;

    private final UserRepo userRepo;
    private final ImageService imageService;


    public UserService(PasswordEncoder passwordEncoder, UserViewRepo userViewRepo, UserRepo userRepo, ImageService imageService) {
        this.passwordEncoder = passwordEncoder;
        this.userViewRepo = userViewRepo;
        this.userRepo = userRepo;
        this.imageService = imageService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Not found!");
        }
        user.setLastVisit(LocalDateTime.now());
        userRepo.save(user);
        return user;
    }

    public void sendToMail(User user) {
        user.setActivationCode(UUID.randomUUID().toString());
        userRepo.save(user);
        sendMessage(user, "registration");
    }

    public boolean addUser(
            String username, String password, String email, String date, String gender,
            String city, String region, String country
    ) {
        User userFromDB = userRepo.findByEmail(email);
        if (userFromDB != null) {
            return false;
        }
        ProfileDetail detail = new ProfileDetail();
        User user = new User();
        user.setCity(city);
        user.setRegion(region);
        user.setCountry(country);
        detail.setUser(user);
        user.setProfileDetail(detail);
        user.setEmail(email);
        user.setUsername(username);
        user.setGender(gender.equals("Мужчина") ? Gender.MALE : Gender.FEMALE);

        user.setBirthDate(getDate(date));

        user.setDateOfRegistration(LocalDateTime.now());
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        user.setPassword(passwordEncoder.encode(password));

        try {
            sendMessage(user, "registration");
        } catch (MailException e) {
            return false;
        }

        userRepo.save(user);
        return true;
    }

    private LocalDate getDate(String dateString) {
        LocalDate date = LocalDate.parse(dateString);
        System.out.println(date);
        return date;
    }

    private void sendMessage(User user, String action) {
        String email = null;
        String mess = null;
        switch (action) {
            case "registration":
                email = user.getEmail();
                mess = "Привет, %s! \n" +
                        "Добро пожаловать на сайт Dating World.\n  " +
                        "Пожайлуста перейдите по ссылке чтобы подтвердить профиль :  \n" +
                        domain +"/activate/" + action + "/%s";
                break;
            case "registrationNewEmail":
                email = user.getNewEmail();
                mess = " Dating World  Привет, %s! \n" +
                        "Чтобы обновить почту перейдите по ссылке:  \n" +
                        domain +"/activate/" + action + "/%s";
                break;
        }
        if (!StringUtils.isEmpty(user.getEmail())) {
            assert mess != null;
            String message = String.format(
                    mess,
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(email, "Activation code", message);
        }
    }

    private void sendMessageChangePassword(User user, String password) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    " %s! Ваш новый пароль:   %s\n",
                    user.getUsername(),
                    password
            );
            mailSender.send(user.getEmail(), "Новый пароль ", message);
        }
    }


    public boolean isActivationUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActive(true);

        userRepo.save(user);
        return true;
    }

    public boolean passwordReset(String email) {
        User userFromDB = userRepo.findByEmail(email);
        if (userFromDB == null) {
//            throw new UsernameNotFoundException("Not found!");
            return false;
        } else {
            String password = RandomHelper.generatePassword(6);
            userFromDB.setPassword(passwordEncoder.encode(password));

            sendMessageChangePassword(userFromDB, password);
            userRepo.save(userFromDB);
            return true;
        }
    }

    public User editUser(User oldUser, User newUser) {
        oldUser = userRepo.findUserById(oldUser.getId());
        oldUser.setBirthDate(newUser.getBirthDate());
        oldUser.setUsername(newUser.getUsername());
        oldUser.setCity(newUser.getCity());
        oldUser.setRegion(newUser.getRegion());
        oldUser.setCountry(newUser.getCountry());
        return userRepo.save(oldUser);
    }

    public User editEmailPassword(User userFromDb, User newUser) {
        if (newUser.getEmail() != null) {
            userFromDb = changeEmail(newUser, userFromDb);
        } else {
            newUser = changePassword(newUser, userFromDb);
        }
        if (userFromDb.getId() == null)
            return userFromDb;
        userRepo.save(userFromDb);
        return newUser;
    }

    private User changeEmail(User newUser, User userFromDb) {
        User user = userRepo.findByEmail(newUser.getEmail());
        if (user != null)
            return new User(null);
        userFromDb.setNewEmail(newUser.getEmail());
        userFromDb.setActivationCode(UUID.randomUUID().toString());
        sendMessage(userFromDb, "registrationNewEmail");
        return userFromDb;
    }

    private User changePassword(User newUser, User userFromDb) {
        String passwordOld = newUser.getPassword();
        String passwordOldFromDb = userFromDb.getPassword();
        if (!passwordEncoder.matches(passwordOld, passwordOldFromDb)) {
            return new User(null);
        }
        String passwordNew = passwordEncoder.encode(newUser.getNewPassword());
        userFromDb.setPassword(passwordNew);
        return newUser;
    }

    public boolean isActivationEmail(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        } else {
            user.setActivationCode(null);
            user.setEmail(user.getNewEmail());
            userRepo.save(user);
            return true;
        }
    }

    public UserPageDto getAllUser(String localeName, String localeType, User userFromDb, String ageMax, String ageMin, String gender, Pageable pageable) {
        userFromDb = userRepo.findUserById(userFromDb.getId());

        LocalDate minDate = getDateForSearchUser(ageMin);
        LocalDate maxDate = getDateForSearchUser(ageMax);
        Gender sex;

        if (gender.equals(Gender.FEMALE.toString())) {
            sex = Gender.MALE;
        } else {
            sex = Gender.FEMALE;
        }
        Page<User> page;
        switch (localeType) {
            case "city":
                page = userRepo.findUsersByBirthDateIsBetweenAndGenderCityDto(localeName, pageable, maxDate, minDate, sex);
                break;
            case "region":
                page = userRepo.findUsersByBirthDateIsBetweenAndGenderRegionDto(localeName, pageable, maxDate, minDate, sex);
                break;
            case "country":
                page = userRepo.findUsersByBirthDateIsBetweenAndGenderCountryDto(localeName, pageable, maxDate, minDate, sex);
                break;
            default:
                page = userRepo.findUsersByBirthDateIsBetweenAndGenderDto(pageable, maxDate, minDate, sex);
                break;
        }
//        Page<UserDto> page = userRepo.findUsersByBirthDateIsBetweenAndGenderDto(pageable, maxDate, minDate, sex, user);
        List<UserPojo> usersDto = new ArrayList<>();

        List<User> usersList = page.getContent();
        final User finalUserFromDb = userFromDb;
        usersList.forEach(user -> {
            Set<UserLike> likes = user.getLikes();
            if (likes.isEmpty()) {
                usersDto.add(new UserPojo(user, false, null));
            } else {
                if (likes.contains(new UserLike(finalUserFromDb, user))) {
                    usersDto.add(new UserPojo(user, true, null));
                } else {
                    usersDto.add(new UserPojo(user, false, null));
                }
            }
        });
        return new UserPageDto(
                usersDto,
                page.getNumber(),
                page.getTotalPages());
    }

    private LocalDate getDateForSearchUser(String ageMin) {
        LocalDate date = LocalDate.now();
        date = date.minusYears(Integer.parseInt(ageMin));
        return date;
    }

    public User updateUserBlock(User updateBlockUser, User userFromDb) {
        userFromDb = userRepo.findUserById(userFromDb.getId());
        List<User> usersBlockList = userFromDb.getUsersBlock();
        if (usersBlockList.contains(updateBlockUser)) {
            usersBlockList.remove(updateBlockUser);
        } else {
            usersBlockList.add(updateBlockUser);
        }
        userFromDb.setUsersBlock(usersBlockList);
        userRepo.save(userFromDb);
        return updateBlockUser;
    }

    public Long deleteChat(Chat removeChat, User userFromDb) {
        userFromDb = userRepo.findUserById(userFromDb.getId());
        List<Chat> chatsDeleteList = userFromDb.getChatsDeleted();
        chatsDeleteList.add(removeChat);
        userFromDb.setChatsDeleted(chatsDeleteList);
        userRepo.save(userFromDb);
        return removeChat.getId();
    }

    public boolean accountDelete(User user) throws MalformedURLException {
        URL url = new URL("https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/profile_delete.jpg?alt=media&token=6ada7493-170c-4f21-aef2-afd93ac1b1d7");
        user = userRepo.findUserById(user.getId());
        user.getRoles().remove(Role.ADMIN);

        user.setUserpic(url);
        user.setBlocked(false);
        user.setLikeNew(null);
        user.setOnline(false);
        user.setUsername("Удален");

        user.setNewPassword(null);
        user.setNewEmail(null);

        user.setActivationCode(null);

        user.getChatsDeleted().clear();
        user.getUsersBlock().clear();

        ProfileDetail detail = user.getProfileDetail();
        detail.setHobby(null);
        detail.setTextAbout(null);
        detail.setLook(new Look());
        detail.setPersonalInformation(null);
        detail.setTarget(null);

        if (user.getUsersViews() != null) {
            userViewRepo.deleteAllByOwner(user);
        }
        Set<Image> images = new HashSet<>();
        if (user.getImages() != null) {
            images = user.getImages();
            user.getImages().removeAll(images);
        }
        userRepo.save(user);
        for (Image image : images) {
            try {
                imageService.remove(image, user);
            } catch (Exception e) {
                user.getImages().addAll(images);
                userRepo.save(user);
            }
        }
        return true;
    }
}