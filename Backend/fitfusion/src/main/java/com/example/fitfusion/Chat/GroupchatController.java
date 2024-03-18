package com.example.fitfusion.Chat;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupchatController {
    @Autowired
    GroupchatRepository groupchatRepository;

    @GetMapping("/blacklists/{groupname}")
    Set<String> getBlacklisted(@PathVariable String groupname) {
        Groupchat groupchat = groupchatRepository.findByGroupname(groupname);
        return groupchat.getBlacklist();
    }

    @PostMapping("/blacklists/{groupname}/{username}")
    String addToBlacklist(@PathVariable String groupname, @PathVariable String username) {
        Groupchat groupchat = groupchatRepository.findByGroupname(groupname);
        groupchat.getBlacklist().add(username);
        groupchatRepository.save(groupchat);
        return username + " was added to " + groupname + "'s blacklist";
    }

    @DeleteMapping("/blacklists/{groupname}/{username}")
    String removeFromBlacklist(@PathVariable String groupname, @PathVariable String username) {
        Groupchat groupchat = groupchatRepository.findByGroupname(groupname);
        groupchat.getBlacklist().remove(username);
        groupchatRepository.save(groupchat);
        return username + " was removed from " + groupname + "'s blacklist";
    }
}
