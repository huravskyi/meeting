package com.newcode.meeting.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@Entity

@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"id"})
public class UserView {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;
    @JsonView(Views.Name.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate lastVisitUser;

    @JsonView(Views.Name.class)
    @ManyToOne
    private User userView;

    @JsonView(Views.FullProfile.class)
    @ManyToOne
    private User owner;
}
