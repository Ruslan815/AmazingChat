package ru.cft.team2.chat.model;

public class AttachView {

    private String fileName;

    public AttachView(Attach attach) {
        this.setFileName(attach.getFileName());
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
