package com.newcode.meeting.pojo;

import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.Image;
import com.newcode.meeting.domain.ProfileDetail;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.domain.dto.Gender;
import com.newcode.meeting.dto.Role;
import lombok.*;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString(of = {"id", "meLiked", "isOnline"})
@JsonView(Views.FullProfileDetail.class)
public class UserPojo {
    private Long id;
    private String username;
    private URL userpic;
    private Gender gender;
    private LocalDate birthDate;
    private boolean isOnline;
    private Set<Role> roles;
    private boolean active;
    private String region;
    private String country;
    private LocalDateTime lastVisit;
    private ProfileDetail profileDetail;
    @JsonView(Views.FullProfileDetailImage.class)
    private Set<Image> images;
    private boolean meLiked;
    Integer likeNew;
    private LocalDateTime timeLike;

    public UserPojo(User user, boolean meLiked, LocalDateTime timeLike) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.userpic = user.getUserpic();
        this.gender = user.getGender();
        this.birthDate = user.getBirthDate();
        this.isOnline = user.isOnline();
        this.roles = user.getRoles();
        this.active = user.isActive();
        this.region = user.getRegion();
        this.country = user.getCountry();
        this.lastVisit = user.getLastVisit();
        this.profileDetail = user.getProfileDetail();
        this.images = user.getImages();
        this.meLiked = meLiked;
        this.likeNew = user.getLikeNew();
        this.timeLike = timeLike;
    }
}
