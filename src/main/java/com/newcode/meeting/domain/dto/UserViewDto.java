package com.newcode.meeting.domain.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.UserView;
import com.newcode.meeting.domain.Views;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@JsonView(Views.FullProfileDetail.class)
public class UserViewDto {
    private List<UserView> listFromDto;
    private int currentPage;
    private int totalPage;
}
