package com.newcode.meeting.domain;


import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity

@Data
@EqualsAndHashCode(of = "id")
@Table
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"id", "name"})
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(mappedBy ="country" )
    private Set<Region> regions = new HashSet<>();
}
