package ru.cft.team2.chat.model;

public class AttachView {

    private String fileName;

    public AttachView(AttachRequest attachRequest) {
        this.setFileName(attachRequest.getFile().getOriginalFilename());
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
