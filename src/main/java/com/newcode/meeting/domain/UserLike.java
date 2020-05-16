package com.newcode.meeting.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"id"})
public class UserLike {

    @EmbeddedId
    private UserLikeId id;

    @ManyToOne
    @MapsId("userLikeId")
    @JoinColumn(name = "user_like_id")
    @JsonView(Views.Name.class)
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private User like;

    @ManyToOne
    @MapsId("ownerId")
    @JoinColumn(name = "owner_id")
    @JsonView(Views.Name.class)
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private User owner;

    @JsonView(Views.Name.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeLike;

    public UserLike(User like, User owner){
        this.like = like;
        this.owner = owner;
        this.timeLike = LocalDateTime.now();
        this.id = new UserLikeId(like.getId(), owner.getId());
    }
}
