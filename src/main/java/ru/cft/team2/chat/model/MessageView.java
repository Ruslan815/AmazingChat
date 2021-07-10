package ru.cft.team2.chat.model;

import java.util.Objects;

public class MessageView {
    private Integer userId;
    private String text;
    private String time;

    public MessageView(Message message) {
        this.setUserId(message.getUserId());
        this.setText(message.getText());
        this.setTime(message.getTime());
    }

    public MessageView(Integer userId, String text, String time) {
        this.userId = userId;
        this.text = text;
        this.time = time;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageView that = (MessageView) o;
        return Objects.equals(userId, that.userId) && Objects.equals(text, that.text) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, text, time);
    }
}
