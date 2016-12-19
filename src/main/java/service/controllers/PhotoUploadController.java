package service.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.Storage;
import service.entity.User;
import service.repository.DialogRepository;
import service.repository.MessageRepository;
import service.repository.TokenRepository;
import service.repository.UserRepository;

import org.springframework.core.io.Resource;

@Controller("/{auth_token}/avatar")
public class PhotoUploadController extends BaseController {

    @Autowired
    private Storage storage;


    @GetMapping("/{dialog_id}")
    public ResponseEntity<Resource> kek(
            @PathVariable String auth_token,
            @PathVariable String dialog_id
    ) {
        Resource file = storage.loadAsResource(dialog_id);
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION)
                .body(file);
    }

    @PostMapping
    public ResponseEntity<?> handleFileUpload(
            @RequestParam("file") MultipartFile file
    ) {
        storage.store(file);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }


    @Autowired
    public PhotoUploadController(UserRepository userRepository, DialogRepository dialogRepository, TokenRepository tokenRepository, MessageRepository messageRepository) {
        super(userRepository, dialogRepository, tokenRepository, messageRepository);
    }
}
