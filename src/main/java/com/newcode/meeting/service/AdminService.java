package com.newcode.meeting.service;

import com.newcode.meeting.domain.Image;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.dto.ImageDto;
import com.newcode.meeting.dto.Role;
import com.newcode.meeting.repo.ImageRepo;
import com.newcode.meeting.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final ImageRepo imageRepo;
    private final UserRepo userRepo;
    @Autowired
    private MailSender mailSender;

    public AdminService(ImageRepo imageRepo, UserRepo userRepo) {
        this.imageRepo = imageRepo;
        this.userRepo = userRepo;
    }

    public ImageDto getImageForAdmin(String event, Pageable pageable) {
        Page<Image> page;
        if (event.equals("")) {
            page = imageRepo.findAll(pageable);
        } else {
            page = imageRepo.findByTested(Boolean.valueOf(event), pageable);
        }
        return new ImageDto(page.getContent(), page.getNumber(), page.getTotalPages());
    }

    public Long deleteAndWarningAdmin(Image image, User user) throws MessagingException {
        Long imageId = image.getId();
        Set<Image> imageSet = user.getImages();
        if (image.isMain()) {
            if (imageSet.size() > 1) {
                imageSet.stream().filter(imageStream -> !imageStream.isMain()).findFirst()
                        .ifPresent(img -> {
                            user.setUserpic(img.getUrlLink());
                            img.setMain(true);
                            img.setHide(false);
                        });
            } else {
                user.setUserpic(null);
                user.getImages().remove(image);
            }
        }
        imageSet.remove(image);
        addWarning(user);
        userRepo.save(user);
        sendMessage(user, "warn");

        return imageId;
    }

    private void addWarning(User user) {
        if (user.getWarning() != null) {
            int warning = user.getWarning();
            user.setWarning(warning + 1);
        } else {
            user.setWarning(1);
        }
    }

    private void sendMessage(User user, String action) throws MessagingException {
        String email;
        String mess;
        email = user.getEmail();
        if (action.equals("del")) {
            mess = " <div>\n" +
                    "                <div  style='display: flex;'>\n" +
                    "                    <h1 STYLE='margin:0; color: #0a70ff'>Dating World</h1>\n" +
                    "                    <img  width='35px' height='35px '\n" +
                    "                         src='%s'/>\n" +
                    "                </div>\n" +
                    "                <p>Пользователь\n" +
                    "                    <span style='color: blue'>%s</span>\n" +
                    "                    Ваша анкета заблокирована </p>\n" +
                    "            </div>";
        } else {
            mess = " <div>\n" +
                    "                <div  style='display: flex;'>\n" +
                    "                    <h1 STYLE='margin:0; color: #0a70ff'>Dating World</h1>\n" +
                    "                    <img  width='35px' height='35px '\n" +
                    "                         src='%s'/>\n" +
                    "                </div>\n" +
                    "                <p>Пользователь\n" +
                    "                    <span style='color: blue'>%s</span>\n" +
                    "                    Ваша фотография не прошла проверку. Вы нарушили правила сайта\n" +
                    "                    ваша фотография будет удалена и вы получпете предупреждение</p>\n" +
                    "            </div>";

        }
        if (!StringUtils.isEmpty(user.getEmail())) {
            String WARNING = "https://firebasestorage.googleapis.com/v0/b/meeting-app-af0af.appspot.com/o/warning_o.png?alt=media&token=5e9a9ea8-5bf0-4a4f-9de0-56f29787bcc9";
            String message = String.format(
                    mess,
                    WARNING,
                    user.getUsername()
            );
            mailSender.sendHtml(email, "Warning!", message);
        }
    }

    public boolean setApproveImage(Image imageFromDb, boolean tested) {
        imageFromDb.setTested(tested);
        imageRepo.save(imageFromDb);
        return tested;
    }

    public ImageDto getImageByUser(User user) {
        List<Image> images = imageRepo.findImageByUser(user);
        return new ImageDto(images);
    }

    public User getUserBy(Map<String, String> form) {
        User user;
        if (form.get("type").equals("number")) {
            user = userRepo.findUserById(Long.valueOf(form.get("model")));
        } else {
            user = userRepo.findUserByEmail(form.get("model"));
        }
        return user;
    }

    public User updateUserByAdmin(User userFromDb, User user) throws MessagingException {
        userFromDb.setRoles(user.getRoles());
        if (user.isBlocked()) {
            sendMessage(userFromDb, "del");
        }
        userFromDb.setBlocked(user.isBlocked());
        return userRepo.save(userFromDb);

    }

    public List<Long> findIdAdmins() {
        return userRepo.findByRolesContains(Role.ADMIN).stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }
}
