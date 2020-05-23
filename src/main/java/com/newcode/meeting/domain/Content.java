package com.newcode.meeting.domain;



import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@JsonView(Views.Id.class)
public class Content implements Serializable {
    private Long id;
    private String content;
    private String link;
    private String linkTitle;
    private String linkDescription;
    private String linkCover;
}
