package com.newcode.meeting.domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.newcode.meeting.domain.dto.Look;
import lombok.*;
import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
@EqualsAndHashCode(of = "id")
@Table
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"id"})
@JsonIdentityReference
@JsonIdentityInfo(
        property = "id",
        generator = ObjectIdGenerators.PropertyGenerator.class
)
public class ProfileDetail implements Serializable {

    @JsonView(Views.Id.class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @JsonView(Views.IdName.class)
    @Column(length = 2520)
    private String textAbout;
    @JsonView(Views.IdName.class)
    private String target;
    @JsonView(Views.IdName.class)
    private Look look = new Look();
    @JsonView(Views.IdName.class)
    private String personalInformation;
    @JsonView(Views.IdName.class)
    private String hobby;

    @JsonView(Views.FullField.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public void setLook() {
    }
}
