package com.newcode.meeting.domain;

import com.fasterxml.jackson.annotation.*;
import com.newcode.meeting.domain.dto.Gender;
import com.newcode.meeting.dto.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity

@Data
@EqualsAndHashCode(of = "id")
@Table(name = "usr")
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"id", "username", "isOnline"})

public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;
    @JsonView(Views.Name.class)
    private String username;
    @JsonView(Views.Name.class)
    @Column(length = 2520)
    private URL userpic;
    @JsonView(Views.FullField.class)
    private String password;
    @JsonView(Views.FullProfile.class)
    private String newPassword;
    @Enumerated(EnumType.STRING)
    @JsonView(Views.IdName.class)
    private Gender gender;
    @JsonView(Views.IdName.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    @JsonView(Views.Name.class)
    private boolean isOnline;

    @JsonView(Views.Name.class)
    private Integer likeNew;
    @JsonView(Views.FullProfile.class)
    private Integer warning;

    @JsonView(Views.IdName.class)
    private boolean blocked;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @JsonView(Views.IdName.class)
    private Set<Role> roles;
    @JsonView(Views.FullField.class)
    private String activationCode;
    @JsonView(Views.IdName.class)
    private boolean active;
    @JsonView(Views.FullProfileDetailAndEmail.class)
    private String email;
    @JsonView(Views.FullField.class)
    private String newEmail;
    @JsonView(Views.City.class)
    private String city;
    @JsonView(Views.City.class)
    private String region;
    @JsonView(Views.City.class)
    private String country;
    @JsonView(Views.Name.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastVisit;
    @JsonView(Views.FullProfile.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateOfRegistration;

    @JsonView(Views.FullProfileDetail.class)
    @OneToOne(mappedBy ="user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private  ProfileDetail profileDetail;

    @JsonView(Views.Image.class)
    @OneToMany(mappedBy ="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private Set<Image> images = new HashSet<>();

    @JsonView(Views.FullField.class)
    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private List<Chat> chats;

    @JsonView(Views.FullField.class)
    @OneToMany
    private List<User> usersBlock = new ArrayList<>();

    @JsonView(Views.FullField.class)
    @ManyToMany
    private List<Chat> chatsDeleted =new ArrayList<>();

    @JsonView(Views.FullField.class)
    @OneToMany(mappedBy = "userView", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserView> usersViews =new ArrayList<>();

    @JsonView(Views.FullField.class)
    @OneToMany(mappedBy = "owner", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<UserLike> likes = new HashSet<>();

    public User(Long id) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }
//    @Override
//    public String getUsername(){
//        return email;
//    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}