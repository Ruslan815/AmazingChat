package ru.cft.team2.chat.model;

import javax.persistence.*;

@Entity(name = "messages")
public class Message {

    @Column(nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String text;

    @Id
    private String time;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
