package com.newcode.meeting.domain.dto;


import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.Views;
import lombok.*;

import java.io.Serializable;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@JsonView(Views.IdName.class)
public class Look implements Serializable{
    private String hair;
    private int height;
    private int weight;
    private String eyes;
    private String body;
    private String type;
}
