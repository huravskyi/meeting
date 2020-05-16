package com.newcode.meeting.domain.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.Views;
import com.newcode.meeting.pojo.UserPojo;
import lombok.*;

import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@JsonView(Views.FullProfileDetail.class)
public class UserPageDto {
    List<UserPojo> listFromDto;
    private int currentPage;
    private int totalPage;
}
