package ru.cft.team2.chat.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Objects;

@ApiModel(description = "Отображение сообщения")
public class MessageView {

    @ApiModelProperty(
            value = "Идентификатор автора сообщения",
            required = true,
            example = "1"
    )
    private Integer userId;

    @ApiModelProperty(
            value = "Текст сообщения",
            required = true,
            example = "Gravity falls"
    )
    private String text;

    @ApiModelProperty(
            value = "Время отправки сообщения",
            required = true,
            example = "2021-07-12 17:50:00"
    )
    private String sendTime;

    public MessageView(Message message) {
        this.setUserId(message.getUserId());
        this.setText(message.getText());
        this.setSendTime(message.getSendTime());
    }

    public MessageView(Integer userId, String text, String sendTime) {
        this.userId = userId;
        this.text = text;
        this.sendTime = sendTime;
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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    @Override
    public String toString() {
        return "MessageView{" +
                "userId=" + userId +
                ", text='" + text + '\'' +
                ", sendTime='" + sendTime + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageView that = (MessageView) o;
        return Objects.equals(userId, that.userId) && Objects.equals(text, that.text) && Objects.equals(sendTime, that.sendTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, text, sendTime);
    }
}
