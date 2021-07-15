package ru.cft.team2.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.cft.team2.chat.model.Attach;
import ru.cft.team2.chat.model.AttachView;

@Controller
public class FileUploadController {

    @PostMapping("/attach")
    public ResponseEntity<?> handleFileUpload(@ModelAttribute Attach attach) {
        //attach.file.
        return ResponseEntity.ok(new AttachView(attach));
    }

}
