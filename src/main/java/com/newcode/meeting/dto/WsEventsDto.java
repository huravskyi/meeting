package com.newcode.meeting.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonView;
import com.newcode.meeting.domain.Views;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonView(Views.Id.class)
public class WsEventsDto {
    private ObjectType objectType;
    private EventType eventType;
//  it  allows  to insert a string json to json
    @JsonRawValue
    private String body;

}
