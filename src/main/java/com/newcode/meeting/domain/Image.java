package com.newcode.meeting.domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.net.URL;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@Table
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"id"})

public class Image implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;
    @Column(length = 2520)
    @JsonView(Views.Name.class)
    private URL urlLink;
    @Column(length = 2520)
    @JsonView(Views.Name.class)
    private String name;
    @JsonView(Views.Name.class)
    private boolean main;
    @JsonView(Views.Name.class)
    private boolean hide;

    @JsonView(Views.Name.class)
    private boolean tested;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonView(Views.FullProfile.class)
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private User user;

    public Image(Long id) {
    }
}
