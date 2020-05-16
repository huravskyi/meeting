package com.newcode.meeting.domain;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLikeId implements Serializable {

    @JsonView(Views.Id.class)
    @Column(name = "user_like_id")
    private Long userLikeId;

    @JsonView(Views.Id.class)
    @Column(name = "owner_id")
    private Long ownerId;
}
