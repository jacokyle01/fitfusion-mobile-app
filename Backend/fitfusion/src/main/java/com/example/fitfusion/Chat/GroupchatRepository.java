package com.example.fitfusion.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupchatRepository extends JpaRepository<Groupchat, Long> {

    Groupchat findByGroupname(String groupname);

}
