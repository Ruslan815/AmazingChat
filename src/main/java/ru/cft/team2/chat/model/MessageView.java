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
