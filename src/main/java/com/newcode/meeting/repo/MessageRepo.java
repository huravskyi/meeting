package com.newcode.meeting.repo;

import com.newcode.meeting.domain.Chat;
import com.newcode.meeting.domain.Message;
import com.newcode.meeting.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {

    Message findMessageById(Long id);

    Page<Message> findByChatAndIdIsBetween(Chat chat, Long firsId, Long lastId, Pageable pageable);

    List<Message> findByChatAndViewedAndAuthorNot(Chat chat, boolean viewed, User author);

    Page<Message> findByChat(Chat chat, Pageable pageable);

    Page<Message> findByChatAndViewedPage(Chat chat, boolean viewedPage, Pageable pageable);

    Page<Message> findByChatAndViewedPageOrderByIdAsc(Chat chat, boolean viewedPage, Pageable pageable);
    Page<Message> findByIdInAndChatOrderByIdAsc(Set<Long> ids, Chat chat, Pageable pageable);


    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Message  mes SET mes.viewed = true WHERE mes.id IN :ids")
    void updateMessageViewedForIds(@Param("ids") List<Long> ids);


    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Message  mes SET  mes.viewed = true, mes.viewedPage = true  WHERE mes.id IN :ids")
    void updateMessageViewedAndViewedPageForIds(@Param("ids") List<Long> ids);
}
