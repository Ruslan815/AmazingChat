package ru.cft.team2.chat.model;

public class MessageView {
    private Integer userId;
    private String text;
    private String time;

    public MessageView(Message message) {
        this.setUserId(message.getUserId());
        this.setText(message.getText());
        this.setTime(message.getTime());
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
}
