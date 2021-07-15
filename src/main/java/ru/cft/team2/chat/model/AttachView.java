package ru.cft.team2.chat.model;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class AttachView {

    private String fileName;

    public AttachView(Attach attach) throws UnsupportedEncodingException {
        this.setFileName(attach.getFile().getOriginalFilename());
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
