package com.newcode.meeting.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity

@Data
@EqualsAndHashCode(of = "id")
@Table
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"id", "members", "messages"})
@JsonIdentityReference
@JsonIdentityInfo(
        property = "id",
        generator = ObjectIdGenerators.PropertyGenerator.class
)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;
    @JsonView(Views.Id.class)
    private LocalDateTime updatedDate;
    @JsonView(Views.FullProfile.class)
    private LocalDateTime creationDate;
    @Lob
    @Column(columnDefinition="LONGBLOB")
    @JsonView(Views.Id.class)
    private String lastMessage;


    @JsonView(Views.FullProfile.class )
    @ManyToMany
    private List<User> members;

    @JsonView(Views.FullProfile.class)
    @OneToMany(mappedBy = "chat", fetch = FetchType.LAZY)
    private List<Message> messages;

    public Chat(Long id) {
        this.id = id;
    }
}
