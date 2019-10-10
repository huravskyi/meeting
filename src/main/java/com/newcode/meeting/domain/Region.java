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
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id")
    private Country country;

    @OneToMany(mappedBy = "region")
    private Set<City> cities = new HashSet<>();
}
