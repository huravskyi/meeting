package com.newcode.meeting.domain.dto;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.newcode.meeting.domain.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
@RequiredArgsConstructor
@ToString(of = {"id", "updatedDate", "messages",  "numberOfNewMessage"})
@JsonIdentityReference
@JsonIdentityInfo(
        property = "id",
        generator = ObjectIdGenerators.PropertyGenerator.class
)
@JsonView(Views.ChatIdName.class)
public class ChatDto implements Serializable {
    private Long id;
    private LocalDateTime updatedDate;
    private Content lastMessage;
    private List<User> members;
    private List<Message> messages = new ArrayList<>();
    private Long numberOfNewMessage;

    public ChatDto(Chat chat, Long numberOfNewMessage) {
        this.id = chat.getId();
        this.updatedDate = chat.getUpdatedDate();
        this.lastMessage = chat.getLastMessage();
        this.members = chat.getMembers();
        this.numberOfNewMessage = numberOfNewMessage;
    }
    public ChatDto(Long id){ }
}
