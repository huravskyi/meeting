package com.newcode.meeting.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table()
@ToString(of = {"id","viewed"})
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@RequiredArgsConstructor
@Data

public class Message  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(Views.Id.class)
    private Long id;
    @JsonView(Views.IdMessage.class)
    private Boolean viewed;
    @JsonView(Views.IdMessage.class)
    private Boolean viewedPage;
    @JsonView(Views.IdMessage.class)
    private Boolean delivered;
    @Lob
    @Column(columnDefinition="LONGBLOB")
    @JsonView(Views.IdMessage.class)
    private Content content = new Content();
    @JsonView(Views.IdMessage.class)
    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    @JsonView(Views.IdMessage.class)
    @Column(updatable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;
    @JsonView(Views.IdMessage.class)
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    @JsonView(Views.IdMessage.class)
    @JsonIdentityReference
    @JsonIdentityInfo(
            property = "id",
            generator = ObjectIdGenerators.PropertyGenerator.class
    )
    private Chat chat;

    public Message(Long id){}
}
