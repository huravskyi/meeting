package com.newcode.meeting.sevice;

import com.newcode.meeting.domain.User;
import com.newcode.meeting.dto.Role;
import com.newcode.meeting.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Random;
import java.security.SecureRandom;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Autowired
    private MailSender mailSender;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Not found!");
        }
        return user;
    }

    public Boolean addUser(User user) {
        User userFromDB = userRepo.findByEmail(user.getEmail());
        if (userFromDB != null) {
            return false;
        }
        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);

        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to Новые свидания. Please, " +
                            "visit next link: http://localhost:8081/activate/" + "registration" + "/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }
    private void sendMessageChangePassword(User user, String password) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    " %s! Ваш новый пароль:   %s\n",
                    user.getUsername(),
                    password
            );
            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }




    public boolean isActivationUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if (user == null) {
            return false;
        }
        user.setActivationCode(null);
        user.setActive(true);

        userRepo.save(user);
        return true;
    }

    public boolean passwordReset(String email) {
        User userFromDB = userRepo.findByEmail(email);
        if (userFromDB == null) {
//            throw new UsernameNotFoundException("Not found!");
            return false;
        }else {
            String password = generatePassword(6);
            userFromDB.setPassword(passwordEncoder.encode(password));

            sendMessageChangePassword(userFromDB, password);
            userRepo.save(userFromDB);
            return true;
        }
    }


    private static String generatePassword(int length) {
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return new String(returnValue);
    }

}