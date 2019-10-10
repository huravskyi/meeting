package com.newcode.meeting.domain;


import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Entity

@Data
@EqualsAndHashCode(of = "id")
@Table
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"id", "name"})
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "region_id")
    private Region region;
}
