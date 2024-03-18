package com.example.fitfusion.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationGroupRepository extends JpaRepository<ConversationGroup, Long> {

    @Query("SELECT g FROM ConversationGroup g WHERE g.groupchat.groupname = :groupname")
    ConversationGroup findConversationByGroupname(@Param("groupname") String groupname);



}
