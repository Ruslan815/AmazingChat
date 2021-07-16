package ru.cft.team2.chat.service;

import org.springframework.stereotype.Service;
import ru.cft.team2.chat.model.Attach;
import ru.cft.team2.chat.model.AttachRequest;
import ru.cft.team2.chat.model.AttachView;
import ru.cft.team2.chat.repository.AttachRepository;

import java.io.IOException;

@Service
public class AttachService {
    private final AttachRepository attachRepository;

    public AttachService(AttachRepository attachRepository) {
        this.attachRepository = attachRepository;
    }

    public AttachView create(AttachRequest attachRequest) throws IOException {
        String fileName = attachRequest.getFile().getOriginalFilename();
        String fileEnding = ".anyType";
        try {
            fileEnding = fileName.substring(fileName.lastIndexOf('.'));
        } catch (Exception ignored) {}
        String newName = attachRepository.count() + fileEnding;

        byte[] fileByteArr = attachRequest.getFile().getBytes();
        Attach attach = new Attach(attachRequest.getUserId(), attachRequest.getChatId(),
                fileByteArr, newName, attachRequest.getFile().getContentType());
        attachRepository.save(attach);

        return new AttachView(newName);
    }

    public Attach getByFileName(String fileName) {
        return attachRepository.findByFileName(fileName);
    }
}
