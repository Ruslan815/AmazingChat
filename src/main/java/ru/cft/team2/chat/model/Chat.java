package ru.cft.team2.chat.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer chatId;

    @Column
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<User> chatMembers = new HashSet<>();

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getChatMembers() {
        return chatMembers;
    }

    public void setChatMembers(Set<User> chatMembers) {
        this.chatMembers = chatMembers;
    }
}
