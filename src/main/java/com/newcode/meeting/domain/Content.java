package com.newcode.meeting.domain;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table
@ToString(of = {"id"})
@EqualsAndHashCode(of = {"id"})
@Data

@JsonView(Views.Id.class)
public class Content implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;
    @JsonView(Views.IdMessage.class)
    private String content;

    @JsonView(Views.IdMessage.class)
    private String link;
    @JsonView(Views.IdMessage.class)
    private String linkTitle;
    @JsonView(Views.IdMessage.class)
    private String linkDescription;
    @JsonView(Views.IdMessage.class)
    private String linkCover;

    @JsonView(Views.IdName.class)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private Message message;
}
