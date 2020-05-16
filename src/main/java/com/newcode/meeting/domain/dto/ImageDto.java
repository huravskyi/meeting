package com.newcode.meeting.domain.dto;


import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.Image;
import com.newcode.meeting.domain.Views;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@JsonView(Views.FullProfile.class)
public class ImageDto {
    List<Image> listFromDto;
    private int currentPage;
    private int totalPage;

    public ImageDto(List<Image> listFromDto) {
        this.listFromDto = listFromDto;
    }
}
