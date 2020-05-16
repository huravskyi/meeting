package com.newcode.meeting.repo;

import com.newcode.meeting.domain.Chat;
import com.newcode.meeting.domain.User;
import com.newcode.meeting.domain.dto.ChatDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Long> {

    List<Chat> findChatsByMembersIn(List<User> user);

    @Query("select new com.newcode.meeting.domain.dto.ChatDto(" +
            " ch,  sum (case  when mes.author = :user and mes.viewed <> true then 1 else 0 end)" +
            ")from Chat ch  " +
            "left join ch.messages mes  " +
            "where ch = :chat")
    ChatDto findChatDto(@Param("chat") Chat chat, @Param("user") User user);

    @Query("select new com.newcode.meeting.domain.dto.ChatDto(" +
            " ch,  sum (case  when mes.author <> :user and mes.viewed <> true then 1 else 0 end)" +
            ")from Chat ch  " +
            "left join ch.members mem " +
            "left join ch.messages mes  " +
            "where mem IN :user and ch NOT IN :chats group by ch  order by ch.updatedDate DESC ")
    List<ChatDto> findChatsAndNumber(@Param("user") User user, @Param("chats") List<Chat> chats);

    @Query("select new com.newcode.meeting.domain.dto.ChatDto(" +
            " ch,  sum (case  when mes.author <> :user and mes.viewed <> true then 1 else 0 end)" +
            ")from Chat ch  " +
            "left join ch.members mem " +
            "left join ch.messages mes  " +
            "where mem IN :user and ch  IN :chats  group by ch  order by ch.updatedDate DESC ")
    List<ChatDto> findChatsBlock(@Param("user") User user, @Param("chats") List<Chat> chats);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Message mes SET mes.delivered = true  where mes.chat IN :chats AND  mes.author <> :user")
    void updateMessageDelivered(@Param("chats") List<Chat> chats, @Param("user") User user);

    @Transactional
    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("UPDATE Message mes SET mes.viewedPage = true  where mes.chat IN :chats AND mes.author <> :user and mes.viewed = true ")
    void updateMessageViewedPage(@Param("chats") List<Chat> chats, @Param("user") User user);


    @Query("SELECT distinct ch  FROM Chat ch left join ch.members mem left join ch.messages mes " +
            "where mem IN :user and mes.author <> : user AND mes.delivered <> true AND mes.author.isOnline = true ")
    List<Chat> findChatsAndMessages(@Param("user") User user);


    Chat findChatByMembersContainsAndMembersContains(User userFrom, User userTo);

    Chat findChatById(Long id);
}

