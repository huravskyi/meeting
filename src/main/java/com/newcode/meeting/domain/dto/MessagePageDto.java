package com.newcode.meeting.domain.dto;


import com.fasterxml.jackson.annotation.JsonView;

import com.newcode.meeting.domain.Message;
import com.newcode.meeting.domain.Views;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@JsonView(Views.IdMessage.class)
public class MessagePageDto {
    private List<Message> messages;
    private int currentPage;
    private int totalPage;
    private int currentPageNotViewed;
    private int totalPageNotViewed;
    private boolean isPage;

}
