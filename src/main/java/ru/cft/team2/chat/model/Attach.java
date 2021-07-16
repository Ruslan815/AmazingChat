package ru.cft.team2.chat.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

public class Attach {

    private Integer userId;
    private Integer chatId;
    private MultipartFile file;

    public Attach(Integer userId, Integer chatId, MultipartFile file) {
        this.userId = userId;
        this.chatId = chatId;
        this.file = file;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Attach{" +
                "userId=" + userId +
                ", chatId=" + chatId +
                ", file=" + file +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attach attach = (Attach) o;
        return Objects.equals(userId, attach.userId) && Objects.equals(chatId, attach.chatId) && Objects.equals(file, attach.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, chatId, file);
    }
}
