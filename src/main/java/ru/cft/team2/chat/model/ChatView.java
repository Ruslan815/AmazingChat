package ru.cft.team2.chat.model;

public class ChatView {
    private Integer chatId;
    private String name;

    public ChatView(Chat chat) {
        this.setChatId(chat.getChatId());
        this.setName(chat.getName());
    }

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
}
