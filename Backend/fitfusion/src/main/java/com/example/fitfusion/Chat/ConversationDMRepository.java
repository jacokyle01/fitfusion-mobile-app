package com.example.fitfusion.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationDMRepository extends JpaRepository <ConversationDM, Long> {
    @Query("SELECT c FROM ConversationDM c WHERE (c.person1.username = :username1 AND c.person2.username = :username2) OR (c.person1.username = :username2 AND c.person2.username = :username1)")
    ConversationDM findConversationByUsernames(@Param("username1") String username1, @Param("username2") String username2);

}
